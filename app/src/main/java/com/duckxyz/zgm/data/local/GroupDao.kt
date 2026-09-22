package com.duckxyz.zgm.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("SELECT * FROM groups")
    fun observeAll(): Flow<List<GroupEntity>>

    @Query("SELECT COUNT(*) FROM groups")
    suspend fun count(): Int

    @Upsert
    suspend fun upsert(group: GroupEntity)

    @Upsert
    suspend fun upsertAll(groups: List<GroupEntity>)

    @Delete
    suspend fun delete(group: GroupEntity)
}
