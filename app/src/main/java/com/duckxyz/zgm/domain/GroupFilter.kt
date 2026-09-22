package com.duckxyz.zgm.domain

import com.duckxyz.zgm.model.GroupStatus
import com.duckxyz.zgm.model.ZaloGroup

data class GroupQuery(
    val keyword: String = "",
    val tags: Set<String> = emptySet(),
    val status: GroupStatus? = null,
    val pinnedOnly: Boolean = false
)

fun filterAndSortGroups(
    groups: List<ZaloGroup>,
    query: GroupQuery
): List<ZaloGroup> {
    val keyword = query.keyword.trim()
    return groups
        .asSequence()
        .filter { group ->
            keyword.isBlank() ||
                group.name.contains(keyword, ignoreCase = true) ||
                group.tags.any { it.contains(keyword, ignoreCase = true) }
        }
        .filter { group -> query.tags.isEmpty() || group.tags.containsAll(query.tags) }
        .filter { group -> query.status == null || group.status == query.status }
        .filter { group -> !query.pinnedOnly || group.pinnedRank != null }
        .sortedWith(
            compareBy<ZaloGroup> { it.pinnedRank == null }
                .thenBy { it.pinnedRank ?: Int.MAX_VALUE }
                .thenByDescending { it.priority }
                .thenBy { it.name.lowercase() }
        )
        .toList()
}
