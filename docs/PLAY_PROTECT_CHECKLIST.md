# ZGM — Play Protect / Google Play checklist

## Build Play-ready
- [x] Không có AccessibilityService trong manifest bản Play/main.
- [x] NotificationListenerService giới hạn theo package Zalo.
- [x] Consent gate trong app trước khi xử lý notification.
- [x] Prominent disclosure giải thích dữ liệu, mục đích và lưu trữ.
- [x] Có thu hồi consent và xóa dữ liệu.
- [x] Android backup tắt.
- [x] Cleartext traffic tắt.
- [x] Không có INTERNET permission.
- [x] Target Android 16 / API 36.
- [x] CI tạo APK và AAB.

## Play Console
- [ ] Play App Signing.
- [ ] Privacy Policy URL công khai.
- [ ] Data Safety.
- [ ] Content rating.
- [ ] Target audience.
- [ ] Store listing nói rõ ZGM là companion/organizer, không phải ứng dụng Zalo chính thức.
- [ ] Internal testing và Pre-launch report.

## Không đưa vào bản Play
- Accessibility automation điều khiển app khác.
- Đọc private storage/database của Zalo.
- Cơ chế né Restricted Settings hay tự cấp quyền.
- Tự cập nhật APK ngoài Google Play.
