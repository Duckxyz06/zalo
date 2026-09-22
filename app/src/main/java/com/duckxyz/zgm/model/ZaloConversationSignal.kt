package com.duckxyz.zgm.model

data class ZaloConversationSignal(
    val conversationKey: String,
    val conversationTitle: String,
    val lastMessage: String,
    val lastPostedAt: Long,
    val unreadEstimate: Int = 1,
    val notificationKey: String? = null,
    val notificationActive: Boolean = true
)
