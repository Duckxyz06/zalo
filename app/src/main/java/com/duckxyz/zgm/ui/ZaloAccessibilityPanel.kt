package com.duckxyz.zgm.ui

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duckxyz.zgm.integration.ZALO_PACKAGE_NAME
import com.duckxyz.zgm.integration.ZaloAccessibilityBridge
import com.duckxyz.zgm.integration.ZaloAccessibilityPreferences
import com.duckxyz.zgm.integration.ZaloUiActionRequest

private val AccessibilityPanelColor = Color(0xFF0D1D2A)
private val AccessibilityAccent = Color(0xFF51E6F5)
private val AccessibilityMuted = Color(0xFF9BB0C0)

@Composable
fun ZaloAccessibilityPanel() {
    val context = LocalContext.current
    val snapshot by ZaloAccessibilityBridge.snapshot.collectAsState()
    val status by ZaloAccessibilityBridge.status.collectAsState()
    var enabled by remember {
        mutableStateOf(
            ZaloAccessibilityPreferences.isEnabled(context)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 7.dp),
        colors = CardDefaults.cardColors(
            containerColor = AccessibilityPanelColor
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "Điều khiển Zalo bằng Trợ năng",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Thử nghiệm • chỉ chạy khi bạn bật",
                        color = AccessibilityMuted
                    )
                }
                Switch(
                    checked = enabled,
                    onCheckedChange = { checked ->
                        enabled = checked
                        ZaloAccessibilityPreferences.setEnabled(
                            context,
                            checked
                        )
                        if (!checked) {
                            ZaloAccessibilityBridge.cancelAll(
                                "Đã tắt điều khiển UI thử nghiệm."
                            )
                        }
                    }
                )
            }

            Spacer(Modifier.height(10.dp))
            Text(
                "Quyền Trợ năng có thể nhìn thấy nội dung Zalo đang hiển thị " +
                    "và thực hiện thao tác trên màn hình. ZGM chỉ quét package Zalo, " +
                    "giữ snapshot trong RAM và chỉ thao tác sau khi bạn bấm lệnh.",
                color = AccessibilityMuted
            )

            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        context.startActivity(
                            Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                        )
                    }
                ) {
                    Text("Cài đặt Trợ năng")
                }

                OutlinedButton(
                    enabled = enabled,
                    onClick = { launchZalo(context) }
                ) {
                    Text("Mở Zalo để quét")
                }
            }

            Spacer(Modifier.height(10.dp))
            Text(
                status,
                color = AccessibilityAccent
            )

            if (snapshot.candidates.isNotEmpty()) {
                Spacer(Modifier.height(14.dp))
                Text(
                    "Mục Zalo nhìn thấy gần nhất",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )

                snapshot.candidates.take(20).forEach { candidate ->
                    Spacer(Modifier.height(10.dp))
                    Text(
                        candidate.title,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                    if (candidate.preview.isNotBlank()) {
                        Text(
                            candidate.preview,
                            color = AccessibilityMuted
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedButton(
                            enabled = enabled,
                            onClick = {
                                queueAndLaunch(
                                    context,
                                    ZaloUiActionRequest.Open(
                                        candidate.title
                                    )
                                )
                            }
                        ) {
                            Text("Mở")
                        }
                        OutlinedButton(
                            enabled = enabled,
                            onClick = {
                                queueAndLaunch(
                                    context,
                                    ZaloUiActionRequest.Pin(
                                        candidate.title
                                    )
                                )
                            }
                        ) {
                            Text("Ghim")
                        }
                        OutlinedButton(
                            enabled = enabled,
                            onClick = {
                                queueAndLaunch(
                                    context,
                                    ZaloUiActionRequest.Unpin(
                                        candidate.title
                                    )
                                )
                            }
                        ) {
                            Text("Bỏ ghim")
                        }
                    }
                }
            }
        }
    }
}

private fun queueAndLaunch(
    context: Context,
    request: ZaloUiActionRequest
) {
    if (!ZaloAccessibilityPreferences.isEnabled(context)) {
        ZaloAccessibilityBridge.cancelAll(
            "Chưa bật điều khiển UI thử nghiệm."
        )
        return
    }

    ZaloAccessibilityBridge.requestAction(request)
    if (!launchZalo(context)) {
        ZaloAccessibilityBridge.cancelAll(
            "Không tìm thấy ứng dụng Zalo trên thiết bị."
        )
    }
}

private fun launchZalo(context: Context): Boolean {
    val intent = context.packageManager
        .getLaunchIntentForPackage(ZALO_PACKAGE_NAME)
        ?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ?: return false

    context.startActivity(intent)
    return true
}
