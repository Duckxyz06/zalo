package com.duckxyz.zgm.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ZaloSignalDao {

    @Query(
        "SELECT * FROM zalo_conversation_signals " +
            "ORDER BY lastPostedAt DESC"
    )
    fun observeAll(): Flow<List<ZaloConversationSignalEntity>>

    @Query(
        "SELECT * FROM zalo_conversation_signals " +
            "WHERE conversationKey = :conversationKey LIMIT 1"
    )
    suspend fun getByConversationKey(
        conversationKey: String
    ): ZaloConversationSignalEntity?

    @Upsert
    suspend fun upsert(signal: ZaloConversationSignalEntity)

    @Transaction
    suspend fun recordPosted(signal: ZaloConversationSignalEntity) {
        val existing = getByConversationKey(signal.conversationKey)

        val isDuplicateUpdate =
            existing != null &&
                existing.notificationKey == signal.notificationKey &&
                existing.lastMessage == signal.lastMessage

        upsert(
            signal.copy(
                unreadEstimate = when {
                    existing == null -> 1
                    isDuplicateUpdate -> existing.unreadEstimate
                    else -> existing.unreadEstimate + 1
                },
                notificationActive = true
            )
        )
    }

    @Transaction
    suspend fun recordSnapshot(signal: ZaloConversationSignalEntity) {
        val existing = getByConversationKey(signal.conversationKey)
        upsert(
            signal.copy(
                unreadEstimate = maxOf(existing?.unreadEstimate ?: 0, 1),
                notificationActive = true
            )
        )
    }

    @Query(
        "UPDATE zalo_conversation_signals " +
            "SET unreadEstimate = 0 " +
            "WHERE conversationKey = :conversationKey"
    )
    suspend fun markRead(conversationKey: String)

    @Query(
        "UPDATE zalo_conversation_signals " +
            "SET unreadEstimate = 0, notificationActive = 0 " +
            "WHERE notificationKey = :notificationKey"
    )
    suspend fun markReadByNotificationKey(notificationKey: String)

    @Query(
        "UPDATE zalo_conversation_signals " +
            "SET notificationActive = 0 " +
            "WHERE notificationKey = :notificationKey"
    )
    suspend fun markInactive(notificationKey: String)

    @Query("DELETE FROM zalo_conversation_signals")
    suspend fun clearAll()
}
