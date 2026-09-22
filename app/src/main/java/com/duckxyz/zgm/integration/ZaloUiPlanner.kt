package com.duckxyz.zgm.integration

data class UiNodeSnapshot(
    val text: String? = null,
    val clickable: Boolean = false,
    val longClickable: Boolean = false,
    val children: List<UiNodeSnapshot> = emptyList()
)

data class ZaloUiCandidate(
    val title: String,
    val preview: String = ""
)

sealed interface ZaloUiActionRequest {
    val conversationTitle: String

    data class Open(
        override val conversationTitle: String
    ) : ZaloUiActionRequest

    data class Pin(
        override val conversationTitle: String
    ) : ZaloUiActionRequest

    data class Unpin(
        override val conversationTitle: String
    ) : ZaloUiActionRequest
}

sealed interface ZaloUiActionPlan {
    data class ClickConversation(
        val title: String
    ) : ZaloUiActionPlan

    data class LongPressConversation(
        val title: String
    ) : ZaloUiActionPlan
}

private val ignoredCandidateTitles = setOf(
    "tin nhắn",
    "danh bạ",
    "khám phá",
    "cá nhân",
    "tìm kiếm",
    "tạo nhóm",
    "ghim",
    "ghim trò chuyện",
    "bỏ ghim",
    "bỏ ghim trò chuyện",
    "tắt thông báo",
    "xóa"
)

private val pinMenuLabels = setOf(
    "ghim",
    "ghim trò chuyện"
)

private val unpinMenuLabels = setOf(
    "bỏ ghim",
    "bỏ ghim trò chuyện"
)

fun extractZaloUiCandidates(
    root: UiNodeSnapshot
): List<ZaloUiCandidate> {
    val result = mutableListOf<ZaloUiCandidate>()

    fun collectTexts(node: UiNodeSnapshot): List<String> {
        val values = mutableListOf<String>()
        node.text
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?.let(values::add)

        node.children.forEach { child ->
            values += collectTexts(child)
        }

        return values.distinct()
    }

    fun visit(node: UiNodeSnapshot) {
        if (node.clickable || node.longClickable) {
            val texts = collectTexts(node)
            val title = texts.firstOrNull()

            if (
                title != null &&
                title.length in 2..120 &&
                normalizeConversationKey(title) !in ignoredCandidateTitles
            ) {
                result += ZaloUiCandidate(
                    title = title,
                    preview = texts.drop(1).firstOrNull().orEmpty()
                )
            }
            return
        }

        node.children.forEach(::visit)
    }

    visit(root)
    return result.distinct()
}

fun planConversationAction(
    request: ZaloUiActionRequest,
    candidates: List<ZaloUiCandidate>
): ZaloUiActionPlan? {
    val wanted = normalizeConversationKey(request.conversationTitle)
    val matches = candidates.filter {
        normalizeConversationKey(it.title) == wanted
    }

    if (matches.size != 1) return null

    return when (request) {
        is ZaloUiActionRequest.Open ->
            ZaloUiActionPlan.ClickConversation(matches.single().title)

        is ZaloUiActionRequest.Pin,
        is ZaloUiActionRequest.Unpin ->
            ZaloUiActionPlan.LongPressConversation(matches.single().title)
    }
}

fun choosePinMenuLabel(labels: List<String>): String? =
    chooseExactMenuLabel(labels, pinMenuLabels)

fun chooseUnpinMenuLabel(labels: List<String>): String? =
    chooseExactMenuLabel(labels, unpinMenuLabels)

private fun chooseExactMenuLabel(
    labels: List<String>,
    accepted: Set<String>
): String? {
    val matches = labels.filter {
        normalizeConversationKey(it) in accepted
    }
    return matches.singleOrNull()
}
