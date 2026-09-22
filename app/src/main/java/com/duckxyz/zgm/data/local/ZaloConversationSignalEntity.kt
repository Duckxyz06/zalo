package com.duckxyz.zgm.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "zalo_conversation_signals")
data class ZaloConversationSignalEntity(
    @PrimaryKey val conversationKey: String,
    val conversationTitle: String,
    val lastMessage: String,
    val lastPostedAt: Long,
    val unreadEstimate: Int,
    val notificationKey: String?,
    val notificationActive: Boolean
)
