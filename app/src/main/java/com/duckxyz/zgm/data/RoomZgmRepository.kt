package com.duckxyz.zgm.data

import com.duckxyz.zgm.data.local.ZgmDatabase
import com.duckxyz.zgm.data.local.toDomain
import com.duckxyz.zgm.data.local.toEntity
import com.duckxyz.zgm.model.GroupTask
import com.duckxyz.zgm.model.ZaloConversationSignal
import com.duckxyz.zgm.model.ZaloGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomZgmRepository(
    private val database: ZgmDatabase
) : ZgmRepository {

    override val groups: Flow<List<ZaloGroup>> =
        database.groupDao().observeAll().map { entities ->
            entities.map { it.toDomain() }
        }

    override val tasks: Flow<List<GroupTask>> =
        database.taskDao().observeAll().map { entities ->
            entities.map { it.toDomain() }
        }

    override val zaloSignals: Flow<List<ZaloConversationSignal>> =
        database.zaloSignalDao().observeAll().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun upsertGroup(group: ZaloGroup) {
        database.groupDao().upsert(group.toEntity())
    }

    override suspend fun deleteGroup(group: ZaloGroup) {
        database.groupDao().delete(group.toEntity())
    }

    override suspend fun upsertTask(task: GroupTask) {
        database.taskDao().upsert(task.toEntity())
    }

    override suspend fun deleteTask(task: GroupTask) {
        database.taskDao().delete(task.toEntity())
    }

    override suspend fun markZaloConversationRead(conversationKey: String) {
        database.zaloSignalDao().markRead(conversationKey)
    }

    override suspend fun seedIfEmpty(
        groups: List<ZaloGroup>,
        tasks: List<GroupTask>
    ) {
        if (database.groupDao().count() == 0) {
            database.groupDao().upsertAll(groups.map { it.toEntity() })
            database.taskDao().upsertAll(tasks.map { it.toEntity() })
        }
    }
}
