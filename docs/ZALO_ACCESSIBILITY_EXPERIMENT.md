# ZGM — Zalo Accessibility Experiment

## Mục tiêu

Bổ sung một lớp thử nghiệm để ZGM có thể quan sát các phần tử đang hiển thị trong Zalo và thực hiện một số thao tác UI hữu hạn do người dùng khởi tạo.

## Phạm vi

- Chỉ package `com.zing.zalo`.
- Chỉ chạy khi người dùng bật Accessibility trong Android Settings.
- Còn có một công tắc opt-in riêng bên trong ZGM.
- Snapshot chỉ giữ trong RAM; không ghi nội dung UI vào Room.
- Mỗi action có thời hạn 8 giây.
- Không có vòng lặp tự chủ hoặc thao tác nền định kỳ.

## Hành động thử nghiệm

### Mở cuộc trò chuyện
ZGM tìm một tiêu đề khớp duy nhất rồi bấm phần tử clickable gần nhất.

### Ghim / Bỏ ghim
1. Tìm tiêu đề cuộc trò chuyện khớp duy nhất.
2. Bấm giữ ancestor long-clickable.
3. Chờ menu.
4. Chỉ bấm một nhãn chính xác trong whitelist:
   - Ghim
   - Ghim trò chuyện
   - Bỏ ghim
   - Bỏ ghim trò chuyện
5. Nếu UI mơ hồ, thiếu nhãn, hoặc nhiều mục trùng nhau thì không thao tác.

## Giới hạn

Đây không phải API chính thức của Zalo. Accessibility tree có thể thay đổi giữa các phiên bản, theme hoặc thiết bị. Vì vậy PR này cần được kiểm tra trên điện thoại thật trước khi merge vào main.
