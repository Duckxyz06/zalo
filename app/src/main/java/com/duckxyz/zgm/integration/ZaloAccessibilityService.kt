package com.duckxyz.zgm.integration

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class ZaloAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (!ZaloAccessibilityPreferences.isEnabled(this)) return
        if (event?.packageName?.toString() != ZALO_PACKAGE_NAME) return

        val root = rootInActiveWindow ?: return
        val snapshot = root.toSnapshot()
        val candidates = extractZaloUiCandidates(snapshot)

        ZaloAccessibilityBridge.publishCandidates(candidates)
        processPendingAction(root, candidates)
    }

    override fun onInterrupt() {
        ZaloAccessibilityBridge.cancelAll(
            "Dịch vụ Trợ năng bị gián đoạn."
        )
    }

    private fun processPendingAction(
        root: AccessibilityNodeInfo,
        candidates: List<ZaloUiCandidate>
    ) {
        val pending =
            ZaloAccessibilityBridge.currentAction() ?: return

        when (pending.stage) {
            ZaloUiActionStage.FIND_CONVERSATION -> {
                val plan = planConversationAction(
                    pending.request,
                    candidates
                ) ?: run {
                    val exactCount = candidates.count {
                        normalizeConversationKey(it.title) ==
                            normalizeConversationKey(
                                pending.request.conversationTitle
                            )
                    }

                    if (exactCount > 1) {
                        ZaloAccessibilityBridge.fail(
                            pending.requestId,
                            "tên cuộc trò chuyện xuất hiện nhiều lần."
                        )
                    }
                    return
                }

                when (plan) {
                    is ZaloUiActionPlan.ClickConversation -> {
                        val target = findUniqueActionableTextNode(
                            root = root,
                            text = plan.title,
                            requireLongClick = false
                        ) ?: return

                        if (
                            target.performAction(
                                AccessibilityNodeInfo.ACTION_CLICK
                            )
                        ) {
                            ZaloAccessibilityBridge.complete(
                                pending.requestId,
                                "Đã yêu cầu Zalo mở cuộc trò chuyện."
                            )
                        } else {
                            ZaloAccessibilityBridge.fail(
                                pending.requestId,
                                "Zalo từ chối thao tác mở."
                            )
                        }
                    }

                    is ZaloUiActionPlan.LongPressConversation -> {
                        val target = findUniqueActionableTextNode(
                            root = root,
                            text = plan.title,
                            requireLongClick = true
                        ) ?: return

                        if (
                            target.performAction(
                                AccessibilityNodeInfo.ACTION_LONG_CLICK
                            )
                        ) {
                            ZaloAccessibilityBridge.advanceToMenu(
                                pending.requestId
                            )
                        } else {
                            ZaloAccessibilityBridge.fail(
                                pending.requestId,
                                "không thể bấm giữ đúng cuộc trò chuyện."
                            )
                        }
                    }
                }
            }

            ZaloUiActionStage.WAIT_MENU -> {
                when (pending.request) {
                    is ZaloUiActionRequest.Pin -> {
                        clickUniqueMenuAction(
                            root = root,
                            pending = pending,
                            chooser = ::choosePinMenuLabel,
                            successMessage = "Đã gửi lệnh Ghim cho Zalo."
                        )
                    }

                    is ZaloUiActionRequest.Unpin -> {
                        clickUniqueMenuAction(
                            root = root,
                            pending = pending,
                            chooser = ::chooseUnpinMenuLabel,
                            successMessage = "Đã gửi lệnh Bỏ ghim cho Zalo."
                        )
                    }

                    is ZaloUiActionRequest.Open -> {
                        ZaloAccessibilityBridge.fail(
                            pending.requestId,
                            "trạng thái thao tác không hợp lệ."
                        )
                    }
                }
            }
        }
    }

    private fun clickUniqueMenuAction(
        root: AccessibilityNodeInfo,
        pending: PendingZaloUiAction,
        chooser: (List<String>) -> String?,
        successMessage: String
    ) {
        val visibleTexts = collectVisibleTexts(root)
        val chosenLabel = chooser(visibleTexts) ?: return

        val actionNode = findUniqueActionableTextNode(
            root = root,
            text = chosenLabel,
            requireLongClick = false
        ) ?: return

        if (
            actionNode.performAction(
                AccessibilityNodeInfo.ACTION_CLICK
            )
        ) {
            ZaloAccessibilityBridge.complete(
                pending.requestId,
                successMessage
            )
        } else {
            ZaloAccessibilityBridge.fail(
                pending.requestId,
                "không thể bấm đúng mục $chosenLabel."
            )
        }
    }

    private fun findUniqueActionableTextNode(
        root: AccessibilityNodeInfo,
        text: String,
        requireLongClick: Boolean
    ): AccessibilityNodeInfo? {
        val exactNodes = mutableListOf<AccessibilityNodeInfo>()
        traverse(root) { node ->
            if (
                normalizeConversationKey(
                    node.text?.toString().orEmpty()
                ) == normalizeConversationKey(text)
            ) {
                exactNodes += node
            }
        }

        val actionable = exactNodes.mapNotNull { node ->
            findActionableAncestor(node, requireLongClick)
        }.distinctBy { node ->
            buildString {
                append(node.viewIdResourceName.orEmpty())
                append('|')
                append(node.className?.toString().orEmpty())
                append('|')
                append(node.hashCode())
            }
        }

        return actionable.singleOrNull()
    }

    private fun findActionableAncestor(
        start: AccessibilityNodeInfo,
        requireLongClick: Boolean
    ): AccessibilityNodeInfo? {
        var current: AccessibilityNodeInfo? = start
        repeat(7) {
            val node = current ?: return null
            val acceptable =
                if (requireLongClick) node.isLongClickable
                else node.isClickable

            if (acceptable) return node
            current = node.parent
        }
        return null
    }

    private fun collectVisibleTexts(
        root: AccessibilityNodeInfo
    ): List<String> {
        val values = mutableListOf<String>()
        traverse(root) { node ->
            node.text
                ?.toString()
                ?.trim()
                ?.takeIf { it.isNotBlank() }
                ?.let(values::add)
        }
        return values
    }

    private fun AccessibilityNodeInfo.toSnapshot(): UiNodeSnapshot {
        var visited = 0

        fun build(
            node: AccessibilityNodeInfo,
            depth: Int
        ): UiNodeSnapshot {
            visited += 1
            if (depth >= 8 || visited >= 600) {
                return UiNodeSnapshot(
                    text = node.text?.toString(),
                    clickable = node.isClickable,
                    longClickable = node.isLongClickable
                )
            }

            val children = buildList {
                for (index in 0 until node.childCount) {
                    node.getChild(index)?.let { child ->
                        add(build(child, depth + 1))
                    }
                    if (visited >= 600) break
                }
            }

            return UiNodeSnapshot(
                text = node.text?.toString(),
                clickable = node.isClickable,
                longClickable = node.isLongClickable,
                children = children
            )
        }

        return build(this, 0)
    }

    private fun traverse(
        root: AccessibilityNodeInfo,
        visitor: (AccessibilityNodeInfo) -> Unit
    ) {
        var visited = 0

        fun walk(node: AccessibilityNodeInfo, depth: Int) {
            if (depth > 8 || visited >= 600) return
            visited += 1
            visitor(node)

            for (index in 0 until node.childCount) {
                node.getChild(index)?.let { child ->
                    walk(child, depth + 1)
                }
                if (visited >= 600) break
            }
        }

        walk(root, 0)
    }
}
