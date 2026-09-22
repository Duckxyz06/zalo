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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duckxyz.zgm.privacy.ZaloNotificationConsent

private val AccessPanel = Color(0xFF0D1D2A)
private val AccessAccent = Color(0xFF51E6F5)
private val AccessMuted = Color(0xFF9BB0C0)

@Composable
fun ZaloNotificationAccessPanel() {
    val context = LocalContext.current
    var systemAccess by remember {
        mutableStateOf(hasNotificationAccess(context))
    }
    var consented by remember {
        mutableStateOf(ZaloNotificationConsent.hasConsent(context))
    }
    var showDisclosure by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 7.dp),
        colors = CardDefaults.cardColors(containerColor = AccessPanel),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(
                "Kết nối thông báo Zalo",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                when {
                    consented && systemAccess ->
                        "Đã đồng ý và quyền hệ thống đang bật."
                    consented ->
                        "Đã đồng ý. Bạn vẫn cần bật quyền truy cập thông báo trong Android."
                    systemAccess ->
                        "Quyền Android còn bật nhưng ZGM không xử lý dữ liệu vì bạn chưa đồng ý trong ứng dụng."
                    else ->
                        "Chưa kết nối."
                },
                color = if (consented && systemAccess) AccessAccent else AccessMuted
            )

            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        if (consented) {
                            openNotificationAccessSettings(context)
                        } else {
                            showDisclosure = true
                        }
                    }
                ) {
                    Text(
                        if (consented) {
                            "Mở cài đặt quyền"
                        } else {
                            "Xem & cấp quyền"
                        }
                    )
                }

                OutlinedButton(
                    onClick = {
                        systemAccess = hasNotificationAccess(context)
                        consented =
                            ZaloNotificationConsent.hasConsent(context)
                    }
                ) {
                    Text("Kiểm tra lại")
                }
            }

            if (consented) {
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        ZaloNotificationConsent.revoke(context)
                        consented = false
                    }
                ) {
                    Text("Thu hồi đồng ý trong ZGM")
                }
            }
        }
    }

    if (showDisclosure) {
        AlertDialog(
            onDismissRequest = { showDisclosure = false },
            title = {
                Text("Trước khi bật quyền đọc thông báo")
            },
            text = {
                Text(
                    "ZGM sẽ truy cập các dữ liệu mà Zalo hiển thị trong notification: " +
                        "tên cuộc trò chuyện, nội dung xem trước, thời điểm nhận và trạng thái notification. " +
                        "ZGM dùng các dữ liệu này để tạo danh sách tín hiệu mới, ước lượng mục chưa đọc " +
                        "và mở lại Zalo khi bạn yêu cầu. Dữ liệu hiện chỉ được lưu cục bộ trên thiết bị, " +
                        "không được bán, không dùng cho quảng cáo và không được gửi lên máy chủ của ZGM. " +
                        "Bạn có thể thu hồi đồng ý và xóa dữ liệu đã lưu bất cứ lúc nào."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        ZaloNotificationConsent.grant(context)
                        consented = true
                        showDisclosure = false
                        openNotificationAccessSettings(context)
                    }
                ) {
                    Text("Tôi đồng ý & tiếp tục")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDisclosure = false }
                ) {
                    Text("Không đồng ý")
                }
            }
        )
    }
}

private fun openNotificationAccessSettings(context: Context) {
    context.startActivity(
        Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
    )
}

private fun hasNotificationAccess(context: Context): Boolean {
    val enabled = Settings.Secure.getString(
        context.contentResolver,
        "enabled_notification_listeners"
    ).orEmpty()

    return enabled.contains(context.packageName)
}
