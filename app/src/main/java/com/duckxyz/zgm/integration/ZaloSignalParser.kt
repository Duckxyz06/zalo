package com.duckxyz.zgm.integration

import com.duckxyz.zgm.model.ZaloConversationSignal
import java.util.Locale

const val ZALO_PACKAGE_NAME = "com.zing.zalo"

private val whitespace = Regex("\\s+")

fun normalizeConversationKey(title: String): String =
    title.trim()
        .replace(whitespace, " ")
        .lowercase(Locale.ROOT)

fun buildZaloSignal(
    title: String?,
    text: String?,
    bigText: String?,
    postedAt: Long
): ZaloConversationSignal? {
    val cleanTitle = title?.trim()?.replace(whitespace, " ").orEmpty()
    if (cleanTitle.isBlank()) return null

    val message = bigText
        ?.takeIf { it.isNotBlank() }
        ?: text
            ?.takeIf { it.isNotBlank() }
            .orEmpty()

    return ZaloConversationSignal(
        conversationKey = normalizeConversationKey(cleanTitle),
        conversationTitle = cleanTitle,
        lastMessage = message.trim(),
        lastPostedAt = postedAt
    )
}
