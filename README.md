# ZGM — Nhóm gọn. Việc rõ.

Ứng dụng Android quản lý nhóm Zalo cá nhân theo hướng **offline-first**, hoạt động như một lớp tổ chức bên ngoài Zalo.

## MVP hiện có

- Kotlin + Jetpack Compose.
- Room database.
- Dashboard nhóm / ghim / công việc.
- Tìm nhóm theo tên hoặc thẻ.
- Pin nội bộ theo thứ tự riêng.
- Task/deadline gắn với nhóm.
- Zalo Notification Companion.
- Disclosure + consent trước khi xử lý notification.
- Privacy Center để xóa dữ liệu Zalo cục bộ.
- GitHub Actions build + test.

## Hai kênh phân phối

### play

Dành cho Google Play. Bản này không chứa Accessibility automation. CI có manifest guard để chặn AccessibilityService lọt vào `playRelease`.

### internal

Dành cho QA/thử nghiệm. Application ID có hậu tố `.internal`. Các thử nghiệm Accessibility chỉ được phép đưa vào source set Internal.

## Build

Yêu cầu Android Studio mới, JDK 17, Android SDK 35.

```bash
gradle testPlayDebugUnitTest testInternalDebugUnitTest
gradle assemblePlayDebug assembleInternalDebug
gradle bundlePlayRelease
sh scripts/check_play_manifest.sh
```

## Tài liệu

- `docs/Zalo_Group_Manager_Project.md`
- `docs/ZALO_COMPANION.md`
- `docs/PLAY_DISTRIBUTION.md`
- `docs/PRIVACY_POLICY_DRAFT.md`
- `docs/DATA_SAFETY_DRAFT.md`
- `docs/PLAY_RELEASE_CHECKLIST.md`
