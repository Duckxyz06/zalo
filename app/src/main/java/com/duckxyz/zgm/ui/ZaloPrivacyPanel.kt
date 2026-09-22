package com.duckxyz.zgm.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duckxyz.zgm.BuildConfig

private val PrivacyPanel = Color(0xFF0D1D2A)
private val PrivacyMuted = Color(0xFF9BB0C0)

@Composable
fun ZaloPrivacyPanel(
    onClearLocalData: () -> Unit
) {
    var confirmClear by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 7.dp),
        colors = CardDefaults.cardColors(containerColor = PrivacyPanel),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(
                "Trung tâm quyền riêng tư",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Bản phân phối: " +
                    if (BuildConfig.DISTRIBUTION_CHANNEL == "play") {
                        "Google Play"
                    } else {
                        "Internal"
                    },
                color = PrivacyMuted
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Dữ liệu Zalo đang lưu cục bộ: tên cuộc trò chuyện, preview notification cuối, " +
                    "thời điểm nhận, khóa notification và số tín hiệu chưa đọc ước lượng. " +
                    "Bản hiện tại không tải nội dung này lên cloud và Android backup đã bị tắt.",
                color = PrivacyMuted
            )
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = { confirmClear = true }
            ) {
                Text("Xóa dữ liệu Zalo đã lưu")
            }
        }
    }

    if (confirmClear) {
        AlertDialog(
            onDismissRequest = { confirmClear = false },
            title = { Text("Xóa dữ liệu Zalo?") },
            text = {
                Text(
                    "Thao tác này xóa toàn bộ tín hiệu Zalo mà ZGM đã lưu trong Room trên thiết bị."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearLocalData()
                        confirmClear = false
                    }
                ) {
                    Text("Xóa")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { confirmClear = false }
                ) {
                    Text("Hủy")
                }
            }
        )
    }
}
