# ZGM Google Play release checklist

## Build
- [ ] `testPlayDebugUnitTest` pass
- [ ] `assemblePlayDebug` pass
- [ ] `bundlePlayRelease` pass
- [ ] Manifest guard xác nhận không có AccessibilityService
- [ ] Kiểm tra applicationId `com.duckxyz.zgm`
- [ ] Tăng versionCode/versionName

## Permission UX
- [ ] Disclosure hiển thị trước khi mở Notification Access Settings
- [ ] Disclosure nêu rõ dữ liệu truy cập
- [ ] Disclosure nêu rõ mục đích sử dụng/chia sẻ
- [ ] Có hành động đồng ý rõ ràng
- [ ] Không đồng ý vẫn dùng được các phần ZGM không cần notification
- [ ] Thu hồi consent làm service ngừng xử lý
- [ ] Có nút xóa dữ liệu cục bộ

## Store / Policy
- [ ] Privacy Policy có URL công khai
- [ ] Data Safety khớp code thực tế
- [ ] Store listing mô tả Zalo Companion chính xác
- [ ] Không tuyên bố ZGM là sản phẩm chính thức của Zalo
- [ ] Không dùng logo/tài sản Zalo trái phép
- [ ] Không dùng `isAccessibilityTool=true`
- [ ] Nếu sau này Play build có AccessibilityService, phải thực hiện declaration/review tương ứng trước phát hành

## Runtime QA
- [ ] Android 13+
- [ ] Thiết bị OPPO/ColorOS
- [ ] Samsung One UI
- [ ] Pixel/AOSP
- [ ] Notification Access bật/tắt
- [ ] Consent grant/revoke
- [ ] Xóa dữ liệu Room
- [ ] Không crash khi Zalo chưa cài
