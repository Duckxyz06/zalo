package com.duckxyz.zgm.data.local

import com.duckxyz.zgm.model.GroupStatus
import com.duckxyz.zgm.model.GroupTask
import com.duckxyz.zgm.model.ZaloGroup

private const val TAG_SEPARATOR = "\u001F"

fun ZaloGroup.toEntity() = GroupEntity(
    id = id,
    name = name,
    tagsEncoded = tags.joinToString(TAG_SEPARATOR),
    priority = priority,
    status = status.name,
    pinnedRank = pinnedRank,
    note = note,
    zaloUrl = zaloUrl
)

fun GroupEntity.toDomain() = ZaloGroup(
    id = id,
    name = name,
    tags = tagsEncoded
        .split(TAG_SEPARATOR)
        .filter { it.isNotBlank() }
        .toSet(),
    priority = priority,
    status = runCatching { GroupStatus.valueOf(status) }
        .getOrDefault(GroupStatus.WATCHING),
    pinnedRank = pinnedRank,
    note = note,
    zaloUrl = zaloUrl
)

fun GroupTask.toEntity() = GroupTaskEntity(
    id = id,
    groupId = groupId,
    title = title,
    dueLabel = dueLabel,
    completed = completed
)

fun GroupTaskEntity.toDomain() = GroupTask(
    id = id,
    groupId = groupId,
    title = title,
    dueLabel = dueLabel,
    completed = completed
)
