package com.duckxyz.zgm.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duckxyz.zgm.data.ZgmRepository
import com.duckxyz.zgm.domain.GroupQuery
import com.duckxyz.zgm.domain.filterAndSortGroups
import com.duckxyz.zgm.integration.ZaloNotificationOpenRegistry
import com.duckxyz.zgm.integration.normalizeConversationKey
import com.duckxyz.zgm.model.GroupTask
import com.duckxyz.zgm.model.ZaloConversationSignal
import com.duckxyz.zgm.model.ZaloGroup
import kotlinx.coroutines.launch

private val Midnight = Color(0xFF071019)
private val Panel = Color(0xFF0D1D2A)
private val Cyan = Color(0xFF51E6F5)
private val TextMuted = Color(0xFF9BB0C0)

@Composable
fun ZgmApp(repository: ZgmRepository) {
    val groups by repository.groups.collectAsState(initial = emptyList())
    val tasks by repository.tasks.collectAsState(initial = emptyList())
    val zaloSignals by repository.zaloSignals.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Midnight) {
            var tab by remember { mutableIntStateOf(0) }
            Scaffold(
                containerColor = Midnight,
                bottomBar = {
                    NavigationBar(containerColor = Panel) {
                        NavigationBarItem(
                            selected = tab == 0,
                            onClick = { tab = 0 },
                            icon = { Icon(Icons.Default.Home, null) },
                            label = { Text("Tổng quan") }
                        )
                        NavigationBarItem(
                            selected = tab == 1,
                            onClick = { tab = 1 },
                            icon = { Icon(Icons.Default.Label, null) },
                            label = { Text("Nhóm") }
                        )
                        NavigationBarItem(
                            selected = tab == 2,
                            onClick = { tab = 2 },
                            icon = { Icon(Icons.Default.CheckCircle, null) },
                            label = { Text("Công việc") }
                        )
                        NavigationBarItem(
                            selected = tab == 3,
                            onClick = { tab = 3 },
                            icon = { Icon(Icons.Default.Chat, null) },
                            label = { Text("Zalo") }
                        )
                    }
                }
            ) { padding ->
                Box(Modifier.padding(padding)) {
                    when (tab) {
                        0 -> DashboardScreen(groups, tasks, zaloSignals)
                        1 -> GroupsScreen(groups)
                        2 -> TasksScreen(
                            tasks = tasks,
                            groups = groups,
                            onToggle = { task, checked ->
                                scope.launch {
                                    repository.upsertTask(
                                        task.copy(completed = checked)
                                    )
                                }
                            }
                        )
                        else -> ZaloCompanionScreen(
                            signals = zaloSignals,
                            groups = groups,
                            onMarkRead = { conversationKey ->
                                scope.launch {
                                    repository.markZaloConversationRead(
                                        conversationKey
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(title: String, subtitle: String) {
    Column(Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
        Text(
            title,
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text(subtitle, color = TextMuted)
    }
}

@Composable
private fun DashboardScreen(
    groups: List<ZaloGroup>,
    tasks: List<GroupTask>,
    signals: List<ZaloConversationSignal>
) {
    val pinned = groups
        .filter { it.pinnedRank != null }
        .sortedBy { it.pinnedRank }

    LazyColumn(Modifier.fillMaxSize()) {
        item { Header("ZGM", "Nhóm gọn. Việc rõ.") }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Metric("Nhóm", groups.size.toString(), Modifier.weight(1f))
                Metric("Đã ghim", pinned.size.toString(), Modifier.weight(1f))
                Metric(
                    "Tín hiệu mới",
                    signals.sumOf { it.unreadEstimate }.toString(),
                    Modifier.weight(1f)
                )
            }
        }
        item {
            Text(
                "Ưu tiên hôm nay",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(20.dp)
            )
        }
        items(pinned) { GroupCard(it) }
    }
}

@Composable
private fun Metric(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                value,
                color = Cyan,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(label, color = TextMuted)
        }
    }
}

@Composable
private fun GroupsScreen(groups: List<ZaloGroup>) {
    var keyword by remember { mutableStateOf("") }
    val filtered = filterAndSortGroups(
        groups,
        GroupQuery(keyword = keyword)
    )

    Column(Modifier.fillMaxSize()) {
        Header("Nhóm của tôi", "Tìm, lọc và ghim độc lập với Zalo")
        OutlinedTextField(
            value = keyword,
            onValueChange = { keyword = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            label = { Text("Tìm nhóm hoặc thẻ") },
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(filtered) { GroupCard(it) }
        }
    }
}

@Composable
private fun GroupCard(group: ZaloGroup) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 7.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .clickable(enabled = group.zaloUrl != null) {
                    group.zaloUrl?.let {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(it))
                        )
                    }
                }
                .padding(18.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    group.name,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
                if (group.pinnedRank != null) {
                    Icon(Icons.Default.PushPin, null, tint = Cyan)
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(group.tags.joinToString(" • "), color = Cyan)
            Spacer(Modifier.height(8.dp))
            Text(group.status.label, color = TextMuted)
            if (group.note.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(group.note, color = TextMuted)
            }
        }
    }
}

@Composable
private fun TasksScreen(
    tasks: List<GroupTask>,
    groups: List<ZaloGroup>,
    onToggle: (GroupTask, Boolean) -> Unit
) {
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            Header(
                "Công việc",
                "Deadline được lưu trực tiếp trên thiết bị"
            )
        }

        items(tasks, key = { it.id }) { task ->
            val groupName =
                groups.firstOrNull { it.id == task.groupId }?.name
                    ?: "Nhóm #${task.groupId}"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 7.dp),
                colors = CardDefaults.cardColors(containerColor = Panel),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Checkbox(
                        checked = task.completed,
                        onCheckedChange = { checked ->
                            onToggle(task, checked)
                        }
                    )
                    Column(Modifier.weight(1f)) {
                        Text(
                            task.title,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(groupName, color = Cyan)
                        Text(
                            "Hạn: ${task.dueLabel}",
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ZaloCompanionScreen(
    signals: List<ZaloConversationSignal>,
    groups: List<ZaloGroup>,
    onMarkRead: (String) -> Unit
) {
    val context = LocalContext.current
    var accessEnabled by remember {
        mutableStateOf(hasNotificationAccess(context))
    }

    LazyColumn(Modifier.fillMaxSize()) {
        item {
            Header(
                "Zalo Companion",
                "Phát hiện chat từ notification Zalo"
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 7.dp),
                colors = CardDefaults.cardColors(containerColor = Panel),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(
                        if (accessEnabled) {
                            "Notification Access: Đã bật"
                        } else {
                            "Notification Access: Chưa bật"
                        },
                        color = if (accessEnabled) Cyan else Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "ZGM chỉ thấy nội dung mà Zalo đưa vào notification. " +
                            "Đây không phải quyền đọc database hoặc toàn bộ lịch sử chat.",
                        color = TextMuted
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                context.startActivity(
                                    Intent(
                                        Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
                                    )
                                )
                            }
                        ) {
                            Text("Cấp quyền")
                        }
                        OutlinedButton(
                            onClick = {
                                accessEnabled =
                                    hasNotificationAccess(context)
                            }
                        ) {
                            Text("Kiểm tra lại")
                        }
                    }
                }
            }
        }

        item {
            ZaloAccessibilityPanel()
        }

        if (signals.isEmpty()) {
            item {
                Text(
                    "Chưa phát hiện cuộc trò chuyện nào. " +
                        "Sau khi bật quyền, chat có notification mới sẽ xuất hiện ở đây.",
                    color = TextMuted,
                    modifier = Modifier.padding(20.dp)
                )
            }
        } else {
            items(
                items = signals,
                key = { it.conversationKey }
            ) { signal ->
                val matchedGroup = groups.firstOrNull {
                    normalizeConversationKey(it.name) ==
                        signal.conversationKey
                }

                ZaloSignalCard(
                    signal = signal,
                    matchedGroup = matchedGroup,
                    onOpen = {
                        ZaloNotificationOpenRegistry
                            .openConversationOrZalo(
                                context,
                                signal.conversationKey
                            )
                    },
                    onMarkRead = {
                        onMarkRead(signal.conversationKey)
                    }
                )
            }
        }
    }
}

@Composable
private fun ZaloSignalCard(
    signal: ZaloConversationSignal,
    matchedGroup: ZaloGroup?,
    onOpen: () -> Unit,
    onMarkRead: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 7.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(
                signal.conversationTitle,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(5.dp))
            Text(
                signal.lastMessage.ifBlank { "Tin nhắn mới" },
                color = TextMuted
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Tín hiệu chưa đọc: ${signal.unreadEstimate}",
                color = Cyan
            )

            matchedGroup?.let {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Khớp nhóm ZGM: ${it.name}",
                    color = TextMuted
                )
            }

            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onOpen) {
                    Text("Mở Zalo")
                }
                if (signal.unreadEstimate > 0) {
                    OutlinedButton(onClick = onMarkRead) {
                        Text("Đã đọc")
                    }
                }
            }
        }
    }
}

private fun hasNotificationAccess(context: Context): Boolean {
    val enabled = Settings.Secure.getString(
        context.contentResolver,
        "enabled_notification_listeners"
    ).orEmpty()

    return enabled.contains(context.packageName)
}
