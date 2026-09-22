# ZGM distribution model

## playRelease

Mục tiêu: bản dành cho Google Play.

- Application ID: `com.duckxyz.zgm`
- Không chứa AccessibilityService automation.
- Chỉ dùng NotificationListenerService cho Zalo Companion.
- Người dùng phải đọc disclosure riêng và bấm đồng ý trong ZGM trước khi service xử lý notification.
- Nếu người dùng thu hồi đồng ý trong ZGM, notification service ngừng xử lý dữ liệu ngay cả khi quyền Android vẫn còn bật.
- Dữ liệu notification hiện chỉ được lưu trong Room trên thiết bị.
- Android backup bị tắt.
- CI kiểm tra merged manifest và fail nếu Accessibility capability lọt vào playRelease.

## internalDebug

Mục tiêu: QA / nghiên cứu / thử nghiệm UI trên thiết bị riêng.

- Application ID: `com.duckxyz.zgm.internal`
- Có BuildConfig `ENABLE_ACCESSIBILITY_AUTOMATION=true` để dành cho tính năng thử nghiệm.
- Không được phát hành lên Google Play khi còn chứa automation thử nghiệm chưa được review.
- PR Accessibility Experimental phải được chuyển vào source set `src/internal` trước khi hợp nhất.

## Lưu ý

Restricted Settings trên Android đối với APK sideload không thể được "né" bằng code. Bản Play phải đi theo luồng phân phối Google Play và tuân thủ các chính sách về dữ liệu/quyền.
