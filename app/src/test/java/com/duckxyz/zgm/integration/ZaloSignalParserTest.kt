package com.duckxyz.zgm.integration

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ZaloSignalParserTest {

    @Test
    fun normalizesConversationKeyWithoutLosingVietnameseCharacters() {
        assertEquals(
            "clb giọt máu yêu thương",
            normalizeConversationKey("  CLB   Giọt Máu Yêu Thương  ")
        )
    }

    @Test
    fun buildsSignalFromNotificationTitleAndText() {
        val signal = buildZaloSignal(
            title = "CLB Giọt Máu Yêu Thương",
            text = "Đức: Họp lúc 15:00",
            bigText = null,
            postedAt = 1234L
        )

        assertEquals("clb giọt máu yêu thương", signal?.conversationKey)
        assertEquals("CLB Giọt Máu Yêu Thương", signal?.conversationTitle)
        assertEquals("Đức: Họp lúc 15:00", signal?.lastMessage)
        assertEquals(1234L, signal?.lastPostedAt)
    }

    @Test
    fun prefersBigTextWhenPresent() {
        val signal = buildZaloSignal(
            title = "Nhóm học IELTS",
            text = "Tin nhắn ngắn",
            bigText = "Tin nhắn đầy đủ từ notification mở rộng",
            postedAt = 99L
        )

        assertEquals(
            "Tin nhắn đầy đủ từ notification mở rộng",
            signal?.lastMessage
        )
    }

    @Test
    fun ignoresBlankConversationTitles() {
        assertNull(
            buildZaloSignal(
                title = "   ",
                text = "Nội dung",
                bigText = null,
                postedAt = 1L
            )
        )
    }
}
