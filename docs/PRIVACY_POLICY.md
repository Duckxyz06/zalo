# Chính sách quyền riêng tư — ZGM

_Cập nhật: 22/09/2026_

## Phạm vi
ZGM là ứng dụng quản lý cá nhân hoạt động như một lớp tổ chức bên ngoài Zalo. ZGM không phải sản phẩm của Zalo/VNG và không đăng nhập thay người dùng vào tài khoản Zalo.

## Dữ liệu Zalo Companion xử lý
Khi người dùng chủ động đồng ý và cấp Notification Access, ZGM có thể xử lý dữ liệu mà Zalo đưa vào thông báo Android, gồm tên cuộc trò chuyện, nội dung xem trước, thời điểm thông báo và mã notification do Android cung cấp.

ZGM không đọc database riêng của Zalo và không lấy toàn bộ lịch sử trò chuyện từ máy chủ Zalo.

## Mục đích sử dụng
Dữ liệu notification chỉ dùng để hiển thị các cuộc trò chuyện đã phát hiện, ước lượng tín hiệu chưa đọc, liên kết với nhóm quản lý trong ZGM và mở lại Zalo từ thao tác của người dùng.

## Lưu trữ và truyền dữ liệu
Bản Play-ready lưu dữ liệu Zalo Companion cục bộ bằng Room trên thiết bị. Bản này không khai báo quyền INTERNET để truyền nội dung notification lên máy chủ. Nội dung notification không được bán hoặc chia sẻ. Android backup bị tắt.

## Quyền kiểm soát
Người dùng có thể không đồng ý kích hoạt tính năng, không cấp/thu hồi Notification Access trong Android Settings, hoặc dùng nút "Thu hồi đồng ý và xóa dữ liệu Zalo" để ngừng xử lý và xóa dữ liệu đã lưu.

## Accessibility
Bản Google Play không bao gồm Accessibility automation để điều khiển giao diện Zalo. Tính năng Accessibility thử nghiệm được tách khỏi bản Play.

## Liên hệ
Trước khi phát hành Production, nhà phát triển cần bổ sung địa chỉ email hỗ trợ và URL chính sách quyền riêng tư công khai theo yêu cầu của Google Play.
