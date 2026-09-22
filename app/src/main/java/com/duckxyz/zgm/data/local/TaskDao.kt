package com.duckxyz.zgm.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY completed ASC, id ASC")
    fun observeAll(): Flow<List<GroupTaskEntity>>

    @Upsert
    suspend fun upsert(task: GroupTaskEntity)

    @Upsert
    suspend fun upsertAll(tasks: List<GroupTaskEntity>)

    @Delete
    suspend fun delete(task: GroupTaskEntity)
}
