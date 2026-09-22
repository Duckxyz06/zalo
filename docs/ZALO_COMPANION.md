# ZGM — Zalo Companion

## Mục tiêu
Zalo Companion dùng các cơ chế Android công khai để giúp ZGM nhận biết hoạt động Zalo trên chính thiết bị của người dùng.

## Đã triển khai
- NotificationListenerService chỉ xử lý package `com.zing.zalo`.
- Đọc title/text/bigText do Zalo chủ động đưa vào notification.
- Chỉ giữ preview cuối cùng cho mỗi conversation.
- Lưu tín hiệu vào Room.
- Ước lượng unread dựa trên notification mới.
- Notification được bấm mở sẽ reset unread estimate.
- Có thể mở PendingIntent của notification để quay lại Zalo khi còn hiệu lực.
- Fallback mở app Zalo.
- Tự khớp conversation title với tên nhóm ZGM.
- Người dùng phải tự cấp Notification Access trong Android Settings.

## Giới hạn
- Không đọc private storage/database của Zalo.
- Không lấy toàn bộ lịch sử chat nếu Zalo không đưa nó vào notification.
- Không đảm bảo unread estimate trùng số unread thật bên trong Zalo.
- Không tự động ghim/sửa cấu hình bên trong Zalo.
- Không có API công khai cho chat cá nhân tương đương Zalo OA OpenAPI.

## Bước thử nghiệm tiếp theo
Một AccessibilityService có thể hỗ trợ thao tác UI Zalo trên màn hình, nhưng phụ thuộc mạnh vào phiên bản/giao diện Zalo và phải được người dùng bật quyền rõ ràng. Không nên coi đây là API ổn định.
