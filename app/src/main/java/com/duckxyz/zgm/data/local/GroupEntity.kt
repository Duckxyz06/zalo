package com.duckxyz.zgm.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val tagsEncoded: String,
    val priority: Int,
    val status: String,
    val pinnedRank: Int?,
    val note: String,
    val zaloUrl: String?
)
