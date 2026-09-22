# ZGM — Zalo Group Manager

## Mục tiêu
ZGM là lớp quản lý cá nhân bên ngoài Zalo, không thay thế Zalo và không can thiệp trái phép vào dữ liệu/giao diện nội bộ của Zalo.

## MVP
- Quản lý danh sách nhóm.
- Gán nhiều thẻ cho một nhóm.
- Priority và status độc lập.
- Tìm kiếm theo tên hoặc thẻ.
- Pin nội bộ không phụ thuộc giới hạn của Zalo.
- Task/deadline gắn với group.
- Mở group bằng link Zalo khi người dùng cung cấp link.
- Backup/restore ở giai đoạn tiếp theo.
- Dark mode mặc định.

## Trạng thái
### Đã triển khai
- Android project Kotlin + Jetpack Compose.
- UI Midnight Glass.
- Dashboard.
- Danh sách nhóm + tìm kiếm.
- Pin ranking.
- Task list.
- Pure domain filtering/sorting + unit tests.

### Tiếp theo
1. Room database.
2. CRUD nhóm/thẻ/task.
3. Bộ lọc đa điều kiện và màn hình chi tiết.
4. Import/export JSON.
5. Backup/restore.
6. Deep link/open Zalo.
7. Auth0 + cloud sync sau khi MVP offline ổn định.
