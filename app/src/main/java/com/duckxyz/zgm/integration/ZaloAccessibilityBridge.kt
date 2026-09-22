package com.duckxyz.zgm.integration

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ZaloAccessibilitySnapshot(
    val capturedAt: Long = 0L,
    val candidates: List<ZaloUiCandidate> = emptyList()
)

enum class ZaloUiActionStage {
    FIND_CONVERSATION,
    WAIT_MENU
}

data class PendingZaloUiAction(
    val requestId: Long,
    val request: ZaloUiActionRequest,
    val stage: ZaloUiActionStage,
    val expiresAt: Long
)

object ZaloAccessibilityBridge {
    private const val ACTION_TIMEOUT_MS = 8_000L

    private val _snapshot =
        MutableStateFlow(ZaloAccessibilitySnapshot())
    val snapshot: StateFlow<ZaloAccessibilitySnapshot> =
        _snapshot.asStateFlow()

    private val _status =
        MutableStateFlow("Chưa có yêu cầu điều khiển UI.")
    val status: StateFlow<String> = _status.asStateFlow()

    @Volatile
    private var pending: PendingZaloUiAction? = null

    fun publishCandidates(candidates: List<ZaloUiCandidate>) {
        _snapshot.value = ZaloAccessibilitySnapshot(
            capturedAt = System.currentTimeMillis(),
            candidates = candidates
        )
    }

    @Synchronized
    fun requestAction(request: ZaloUiActionRequest) {
        val id = System.nanoTime()
        pending = PendingZaloUiAction(
            requestId = id,
            request = request,
            stage = ZaloUiActionStage.FIND_CONVERSATION,
            expiresAt = System.currentTimeMillis() + ACTION_TIMEOUT_MS
        )
        _status.value =
            "Đang chờ Zalo: ${request.conversationTitle}"
    }

    @Synchronized
    fun currentAction(): PendingZaloUiAction? {
        val action = pending ?: return null
        if (System.currentTimeMillis() > action.expiresAt) {
            pending = null
            _status.value =
                "Đã hủy: hết thời gian chờ, không thao tác Zalo."
            return null
        }
        return action
    }

    @Synchronized
    fun advanceToMenu(requestId: Long) {
        val action = pending ?: return
        if (action.requestId != requestId) return

        pending = action.copy(
            stage = ZaloUiActionStage.WAIT_MENU,
            expiresAt = System.currentTimeMillis() + ACTION_TIMEOUT_MS
        )
        _status.value = "Đã mở menu, đang tìm đúng lệnh."
    }

    @Synchronized
    fun complete(requestId: Long, message: String) {
        if (pending?.requestId != requestId) return
        pending = null
        _status.value = message
    }

    @Synchronized
    fun fail(requestId: Long, message: String) {
        if (pending?.requestId != requestId) return
        pending = null
        _status.value = "Đã hủy: $message"
    }

    @Synchronized
    fun cancelAll(message: String) {
        pending = null
        _status.value = message
    }
}
