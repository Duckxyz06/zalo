package com.duckxyz.zgm.model

enum class GroupStatus(val label: String) {
    NEEDS_ACTION("Cần xử lý"),
    IN_PROGRESS("Đang xử lý"),
    WATCHING("Đang theo dõi"),
    DONE("Đã xử lý"),
    ARCHIVED("Lưu trữ")
}

data class ZaloGroup(
    val id: Long,
    val name: String,
    val tags: Set<String>,
    val priority: Int,
    val status: GroupStatus,
    val pinnedRank: Int? = null,
    val note: String = "",
    val zaloUrl: String? = null
)

data class GroupTask(
    val id: Long,
    val groupId: Long,
    val title: String,
    val dueLabel: String,
    val completed: Boolean = false
)
