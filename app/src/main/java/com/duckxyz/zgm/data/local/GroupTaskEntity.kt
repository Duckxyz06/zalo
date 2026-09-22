package com.duckxyz.zgm.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("groupId")]
)
data class GroupTaskEntity(
    @PrimaryKey val id: Long,
    val groupId: Long,
    val title: String,
    val dueLabel: String,
    val completed: Boolean
)
