# Google Play — Data Safety draft

Đây là checklist kỹ thuật, không thay thế việc điền biểu mẫu Play Console tại thời điểm phát hành.

- Notification content của Zalo được xử lý và lưu cục bộ.
- Không có INTERNET permission trong manifest.
- Không có SDK quảng cáo/analytics.
- Không bán hoặc chia sẻ notification content.
- Có thao tác trong app để xóa dữ liệu Zalo Companion.
- Android backup bị tắt.

Trước khi phát hành:
1. Đối chiếu định nghĩa "collected" và "shared" trong Data Safety form hiện hành.
2. Khai báo chính xác mọi loại dữ liệu mà biểu mẫu yêu cầu.
3. Đăng Privacy Policy bằng URL HTTPS công khai, không yêu cầu đăng nhập.
4. Kiểm tra lại nếu thêm SDK hoặc backend mới.
