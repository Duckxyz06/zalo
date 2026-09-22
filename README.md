# ZGM — Nhóm gọn. Việc rõ.

Ứng dụng Android quản lý nhóm Zalo cá nhân theo hướng **offline-first**, hoạt động như một lớp tổ chức bên ngoài Zalo.

## MVP hiện có
- Kotlin + Jetpack Compose.
- Giao diện **Midnight Glass**.
- Dashboard nhóm / ghim / công việc.
- Tìm nhóm theo tên hoặc thẻ.
- Pin nội bộ theo thứ tự riêng.
- Trạng thái xử lý và mức ưu tiên.
- Task/deadline gắn với nhóm.
- Unit tests cho logic lọc và sắp xếp.
- GitHub Actions build + test Android.

## Nguyên tắc tích hợp Zalo
ZGM không chỉnh sửa dữ liệu nội bộ hoặc giao diện Zalo. Khi có link hợp lệ do người dùng lưu, app có thể mở cuộc trò chuyện/nhóm bằng Android intent.

## Chạy dự án
Yêu cầu Android Studio mới, JDK 17, Android SDK 35.

```bash
gradle testDebugUnitTest
gradle assembleDebug
```

Chi tiết dự án: `docs/Zalo_Group_Manager_Project.md`

Prompt thiết kế UI: `docs/UI_IMAGE_PROMPTS.md`
