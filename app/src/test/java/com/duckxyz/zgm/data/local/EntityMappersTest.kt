package com.duckxyz.zgm.data.local

import com.duckxyz.zgm.model.GroupStatus
import com.duckxyz.zgm.model.GroupTask
import com.duckxyz.zgm.model.ZaloGroup
import org.junit.Assert.assertEquals
import org.junit.Test

class EntityMappersTest {

    @Test
    fun groupRoundTripPreservesUserData() {
        val original = ZaloGroup(
            id = 7,
            name = "CLB Giọt Máu Yêu Thương",
            tags = setOf("CLB", "Tình nguyện"),
            priority = 5,
            status = GroupStatus.IN_PROGRESS,
            pinnedRank = 2,
            note = "Theo dõi lịch trực",
            zaloUrl = "https://zalo.me/g/example"
        )

        assertEquals(original, original.toEntity().toDomain())
    }

    @Test
    fun unknownStoredStatusFallsBackToWatching() {
        val entity = GroupEntity(
            id = 1,
            name = "Nhóm cũ",
            tagsEncoded = "",
            priority = 1,
            status = "REMOVED_STATUS",
            pinnedRank = null,
            note = "",
            zaloUrl = null
        )

        assertEquals(GroupStatus.WATCHING, entity.toDomain().status)
    }

    @Test
    fun taskRoundTripPreservesCompletionState() {
        val original = GroupTask(
            id = 3,
            groupId = 1,
            title = "Chốt danh sách",
            dueLabel = "Ngày mai",
            completed = true
        )

        assertEquals(original, original.toEntity().toDomain())
    }
}
