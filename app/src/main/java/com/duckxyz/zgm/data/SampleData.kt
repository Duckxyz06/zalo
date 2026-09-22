package com.duckxyz.zgm.data

import com.duckxyz.zgm.model.GroupStatus
import com.duckxyz.zgm.model.GroupTask
import com.duckxyz.zgm.model.ZaloGroup

val sampleGroups = listOf(
    ZaloGroup(1, "BCH Khoa Ngoại ngữ", setOf("Đoàn - Hội", "Quan trọng"), 5, GroupStatus.NEEDS_ACTION, 1, "Theo dõi thông báo và công việc cần phản hồi."),
    ZaloGroup(2, "CLB Giọt Máu Yêu Thương", setOf("CLB", "Tình nguyện"), 4, GroupStatus.IN_PROGRESS, 2, "Quản lý hoạt động, lịch trực và đầu việc."),
    ZaloGroup(3, "Chi đoàn ĐHANH", setOf("Chi đoàn", "Học tập"), 3, GroupStatus.WATCHING, note = "Theo dõi lịch và thông báo định kỳ."),
    ZaloGroup(4, "Nhóm học IELTS", setOf("Học tập"), 2, GroupStatus.DONE)
)

val sampleTasks = listOf(
    GroupTask(1, 1, "Phản hồi thông báo mới", "Hôm nay"),
    GroupTask(2, 2, "Chốt danh sách thành viên", "Ngày mai"),
    GroupTask(3, 3, "Kiểm tra lịch sinh hoạt", "26/09")
)
