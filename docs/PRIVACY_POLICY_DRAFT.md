# Privacy Policy Draft — ZGM

> Bản nháp phục vụ quá trình phát triển. Trước khi phát hành Google Play cần rà soát, công bố tại một URL công khai ổn định và cập nhật đúng với chức năng thực tế của phiên bản phát hành.

## Dữ liệu ZGM xử lý

Khi người dùng chủ động bật tính năng Zalo Companion và đồng ý với disclosure trong ứng dụng, ZGM có thể xử lý các dữ liệu mà ứng dụng Zalo hiển thị trong notification:

- tên cuộc trò chuyện;
- nội dung xem trước của notification;
- thời điểm notification;
- khóa/trạng thái notification;
- số tín hiệu chưa đọc ước lượng do ZGM tính cục bộ.

## Mục đích

Dữ liệu được dùng để:

- hiển thị tín hiệu tin nhắn Zalo mới trong ZGM;
- ước lượng những cuộc trò chuyện cần chú ý;
- khớp tên cuộc trò chuyện với nhóm do người dùng tạo trong ZGM;
- mở lại Zalo theo yêu cầu của người dùng.

## Lưu trữ và chia sẻ

Trong kiến trúc hiện tại của playRelease:

- dữ liệu trên được lưu cục bộ trong Room database trên thiết bị;
- Android backup của ứng dụng bị tắt;
- ZGM không tải nội dung notification lên máy chủ;
- ZGM không bán dữ liệu;
- ZGM không dùng dữ liệu notification cho quảng cáo.

Nếu sau này bổ sung cloud sync/Auth0, chính sách này và Data Safety phải được cập nhật trước khi phát hành phiên bản có chức năng đó.

## Quyền kiểm soát của người dùng

Người dùng có thể:

- không đồng ý với disclosure và không bật Notification Access;
- thu hồi đồng ý trong ZGM;
- tắt Notification Access trong Android Settings;
- xóa toàn bộ dữ liệu Zalo đã lưu bằng Privacy Center trong ZGM.

## Accessibility

Bản playRelease không chứa AccessibilityService automation. Các thử nghiệm Accessibility chỉ thuộc bản Internal và không phải một phần của bản Google Play.
