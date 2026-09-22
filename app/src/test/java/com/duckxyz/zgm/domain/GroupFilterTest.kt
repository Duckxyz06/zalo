package com.duckxyz.zgm.domain

import com.duckxyz.zgm.model.GroupStatus
import com.duckxyz.zgm.model.ZaloGroup
import org.junit.Assert.assertEquals
import org.junit.Test

class GroupFilterTest {
    private val groups = listOf(
        ZaloGroup(1, "BCH Khoa Ngoại ngữ", setOf("Đoàn - Hội", "Quan trọng"), 5, GroupStatus.NEEDS_ACTION, 2),
        ZaloGroup(2, "CLB Giọt Máu Yêu Thương", setOf("CLB", "Tình nguyện"), 4, GroupStatus.IN_PROGRESS, 1),
        ZaloGroup(3, "Chi đoàn ĐHANH", setOf("Chi đoàn"), 3, GroupStatus.WATCHING)
    )

    @Test
    fun pinnedGroupsComeFirstInCustomRankOrder() {
        val result = filterAndSortGroups(groups, GroupQuery())
        assertEquals(listOf(2L, 1L, 3L), result.map { it.id })
    }

    @Test
    fun keywordMatchesGroupNameOrTagIgnoringCase() {
        val result = filterAndSortGroups(groups, GroupQuery(keyword = "tình NGUYỆN"))
        assertEquals(listOf(2L), result.map { it.id })
    }

    @Test
    fun multipleSelectedTagsRequireAllTags() {
        val result = filterAndSortGroups(groups, GroupQuery(tags = setOf("Đoàn - Hội", "Quan trọng")))
        assertEquals(listOf(1L), result.map { it.id })
    }

    @Test
    fun pinnedOnlyRemovesUnpinnedGroups() {
        val result = filterAndSortGroups(groups, GroupQuery(pinnedOnly = true))
        assertEquals(listOf(2L, 1L), result.map { it.id })
    }
}
