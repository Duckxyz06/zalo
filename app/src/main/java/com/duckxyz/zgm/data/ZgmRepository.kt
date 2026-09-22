package com.duckxyz.zgm.data

import com.duckxyz.zgm.model.GroupTask
import com.duckxyz.zgm.model.ZaloConversationSignal
import com.duckxyz.zgm.model.ZaloGroup
import kotlinx.coroutines.flow.Flow

interface ZgmRepository {
    val groups: Flow<List<ZaloGroup>>
    val tasks: Flow<List<GroupTask>>
    val zaloSignals: Flow<List<ZaloConversationSignal>>

    suspend fun upsertGroup(group: ZaloGroup)
    suspend fun deleteGroup(group: ZaloGroup)
    suspend fun upsertTask(task: GroupTask)
    suspend fun deleteTask(task: GroupTask)
    suspend fun markZaloConversationRead(conversationKey: String)
    suspend fun seedIfEmpty(groups: List<ZaloGroup>, tasks: List<GroupTask>)
}
