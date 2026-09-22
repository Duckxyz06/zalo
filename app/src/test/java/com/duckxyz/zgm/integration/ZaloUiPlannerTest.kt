package com.duckxyz.zgm.integration

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ZaloUiPlannerTest {

    @Test
    fun extractsClickableConversationCandidateFromVisibleTree() {
        val root = UiNodeSnapshot(
            text = null,
            clickable = false,
            longClickable = false,
            children = listOf(
                UiNodeSnapshot(
                    text = null,
                    clickable = true,
                    longClickable = true,
                    children = listOf(
                        UiNodeSnapshot(
                            text = "CLB Giọt Máu Yêu Thương",
                            clickable = false,
                            longClickable = false
                        ),
                        UiNodeSnapshot(
                            text = "Đức: Họp lúc 15:00",
                            clickable = false,
                            longClickable = false
                        )
                    )
                )
            )
        )

        assertEquals(
            listOf(
                ZaloUiCandidate(
                    title = "CLB Giọt Máu Yêu Thương",
                    preview = "Đức: Họp lúc 15:00"
                )
            ),
            extractZaloUiCandidates(root)
        )
    }

    @Test
    fun ignoresNavigationAndActionLabelsAsConversationTitles() {
        val root = UiNodeSnapshot(
            children = listOf(
                UiNodeSnapshot(
                    clickable = true,
                    children = listOf(UiNodeSnapshot(text = "Tin nhắn"))
                ),
                UiNodeSnapshot(
                    clickable = true,
                    children = listOf(UiNodeSnapshot(text = "Danh bạ"))
                ),
                UiNodeSnapshot(
                    clickable = true,
                    children = listOf(UiNodeSnapshot(text = "Ghim"))
                )
            )
        )

        assertEquals(emptyList<ZaloUiCandidate>(), extractZaloUiCandidates(root))
    }

    @Test
    fun pinActionRequiresUniqueExactConversationTitle() {
        val candidates = listOf(
            ZaloUiCandidate("Nhóm A", "1"),
            ZaloUiCandidate("Nhóm B", "2")
        )

        assertEquals(
            ZaloUiActionPlan.LongPressConversation("Nhóm B"),
            planConversationAction(
                request = ZaloUiActionRequest.Pin("Nhóm B"),
                candidates = candidates
            )
        )
    }

    @Test
    fun ambiguousTargetAbortsInsteadOfGuessing() {
        val candidates = listOf(
            ZaloUiCandidate("Nhóm A", "một"),
            ZaloUiCandidate("Nhóm A", "hai")
        )

        assertNull(
            planConversationAction(
                request = ZaloUiActionRequest.Pin("Nhóm A"),
                candidates = candidates
            )
        )
    }

    @Test
    fun pinMenuAcceptsOnlyKnownExactLabels() {
        assertEquals(
            "Ghim",
            choosePinMenuLabel(listOf("Xóa", "Ghim", "Tắt thông báo"))
        )
        assertNull(
            choosePinMenuLabel(listOf("Ghim tin nhắn quan trọng", "Xóa"))
        )
    }
}
