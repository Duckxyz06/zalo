# Google Play Data Safety — Working Draft

Đây là checklist kỹ thuật, không phải câu trả lời cuối cùng cho Play Console.

## playRelease hiện tại

ZGM xử lý notification Zalo trên thiết bị sau khi người dùng đồng ý. Mã hiện tại không có luồng tải nội dung notification lên backend và không khai báo INTERNET permission.

Trước khi submit Play Console:

1. Kiểm tra định nghĩa "collected" và "shared" hiện hành trong Data Safety.
2. Xác nhận bằng code review rằng không có SDK analytics/crash/ads nào truyền nội dung notification.
3. Khai báo chính xác mọi dữ liệu thực sự rời thiết bị nếu sau này thêm analytics, Auth0 hoặc cloud sync.
4. Privacy Policy và disclosure trong app phải khớp với Data Safety.
5. Nếu chức năng thay đổi, cập nhật biểu mẫu trước khi rollout.

## Không được làm

- Không đánh dấu "không thu thập" nếu một SDK hoặc backend thực tế truyền dữ liệu.
- Không thêm analytics vào preview/title notification mà không cập nhật disclosure và Data Safety.
- Không merge Accessibility automation vào playRelease.
