package com.duckxyz.zgm.ui

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duckxyz.zgm.data.sampleGroups
import com.duckxyz.zgm.data.sampleTasks
import com.duckxyz.zgm.domain.GroupQuery
import com.duckxyz.zgm.domain.filterAndSortGroups
import com.duckxyz.zgm.model.ZaloGroup

private val Midnight = Color(0xFF071019)
private val Panel = Color(0xFF0D1D2A)
private val Cyan = Color(0xFF51E6F5)
private val TextMuted = Color(0xFF9BB0C0)

@Composable
fun ZgmApp() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Midnight) {
            var tab by remember { mutableIntStateOf(0) }
            Scaffold(
                containerColor = Midnight,
                bottomBar = {
                    NavigationBar(containerColor = Panel) {
                        NavigationBarItem(selected = tab == 0, onClick = { tab = 0 }, icon = { Icon(Icons.Default.Home, null) }, label = { Text("Tổng quan") })
                        NavigationBarItem(selected = tab == 1, onClick = { tab = 1 }, icon = { Icon(Icons.Default.Label, null) }, label = { Text("Nhóm") })
                        NavigationBarItem(selected = tab == 2, onClick = { tab = 2 }, icon = { Icon(Icons.Default.CheckCircle, null) }, label = { Text("Công việc") })
                    }
                }
            ) { padding ->
                Box(Modifier.padding(padding)) {
                    when (tab) {
                        0 -> DashboardScreen()
                        1 -> GroupsScreen()
                        else -> TasksScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(title: String, subtitle: String) {
    Column(Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
        Text(title, color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(subtitle, color = TextMuted)
    }
}

@Composable
private fun DashboardScreen() {
    val pinned = sampleGroups.filter { it.pinnedRank != null }.sortedBy { it.pinnedRank }
    LazyColumn(Modifier.fillMaxSize()) {
        item { Header("ZGM", "Nhóm gọn. Việc rõ.") }
        item {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Metric("Nhóm", sampleGroups.size.toString(), Modifier.weight(1f))
                Metric("Đã ghim", pinned.size.toString(), Modifier.weight(1f))
                Metric("Việc mở", sampleTasks.count { !it.completed }.toString(), Modifier.weight(1f))
            }
        }
        item { Text("Ưu tiên hôm nay", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(20.dp)) }
        items(pinned) { GroupCard(it) }
    }
}

@Composable
private fun Metric(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(value, color = Cyan, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(label, color = TextMuted)
        }
    }
}

@Composable
private fun GroupsScreen() {
    var keyword by remember { mutableStateOf("") }
    val groups = filterAndSortGroups(sampleGroups, GroupQuery(keyword = keyword))
    Column(Modifier.fillMaxSize()) {
        Header("Nhóm của tôi", "Tìm, lọc và ghim độc lập với Zalo")
        OutlinedTextField(
            value = keyword,
            onValueChange = { keyword = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            label = { Text("Tìm nhóm hoặc thẻ") },
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))
        LazyColumn { items(groups) { GroupCard(it) } }
    }
}

@Composable
private fun GroupCard(group: ZaloGroup) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 7.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .clickable(enabled = group.zaloUrl != null) {
                    group.zaloUrl?.let { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it))) }
                }
                .padding(18.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(group.name, color = Color.White, fontWeight = FontWeight.SemiBold)
                if (group.pinnedRank != null) Icon(Icons.Default.PushPin, null, tint = Cyan)
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
private fun TasksScreen() {
    LazyColumn(Modifier.fillMaxSize()) {
        item { Header("Công việc", "Deadline gắn trực tiếp với từng nhóm") }
        items(sampleTasks) { task ->
            val group = sampleGroups.first { it.id == task.groupId }
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 7.dp),
                colors = CardDefaults.cardColors(containerColor = Panel),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(task.title, color = Color.White, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(4.dp))
                    Text(group.name, color = Cyan)
                    Text("Hạn: ${task.dueLabel}", color = TextMuted)
                }
            }
        }
    }
}
