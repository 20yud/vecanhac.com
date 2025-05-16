# 🎫 Vecanhac.com – Concert Ticket Booking Platform

**Vecanhac.com** là một nền tảng đặt vé sự kiện âm nhạc hiện đại, được thiết kế để xử lý **lưu lượng cao**, tích hợp hệ thống **thanh toán**, **mã giảm giá**, **xác thực người dùng**, và hỗ trợ tổ chức đăng sự kiện. Giao diện tối ưu trên mobile và desktop, trải nghiệm người dùng mượt mà.

---

## ⚙️ Tech Stack

### 🔹 Frontend
- **Next.js (App Router)** + **TailwindCSS**
- UI động, responsive toàn bộ
- Component hóa: `EventCard`, `AuthModal`, `TicketSelector`, `OrderSummary`,...
- Tích hợp countdown timer, mã giảm giá, xác thực OTP
- GitHub: [vecanhacUI](https://github.com/20yud/vecanhacUI)

### 🔹 Backend
- **Java Spring Boot**, theo mô hình **DDD** chuẩn:
  - `domain`: Entity, Repository
  - `application`: Service, DTO, Mapper
  - `controller`: REST APIs
- **Spring Security**: JWT + Cookie Auth
- **MySQL**, **Redis** (Locking vé), **Lombok**, (dự kiến: MapStruct)
- Hỗ trợ:
  - Tạo/sửa/xóa sự kiện
  - Order vé có timeout 15 phút
  - API động kiểu PATCH
  - Upload ảnh, QR Code
  - Lưu lịch sử đơn hàng, trạng thái vé

---

## 🔥 Các Tính Năng Chính

### 🏠 Home Page
- Lọc sự kiện theo **ngày**, **thành phố**, **danh mục**
- “Tải thêm” phân trang
- UI tối ưu trải nghiệm trên mobile

### 📄 Event Detail Page
- Banner sự kiện, đơn vị tổ chức
- Mô tả chương trình (HTML safe render)
- Thông tin thời gian – địa điểm rõ ràng

### 🎟️ Ticket + Showing
- Vé được nhóm theo lịch diễn (showing)
- Trạng thái vé: `AVAILABLE`, `SOLD OUT`
- Hiển thị giá giảm / gốc
- Tương tác mượt, hiệu ứng dropdown vé chi tiết

### 🛒 Booking Flow
1. **Chọn vé**
2. **Điền thông tin** + mã giảm giá
3. **Thanh toán VNPay** (có timeout 15 phút)
4. Xem vé trong trang **“Vé của tôi”**, gồm QR code

### 👤 Auth Flow
- Đăng ký OTP qua email (chưa lưu DB khi chưa xác thực)
- Xác thực qua cookie JWT
- Quên mật khẩu = OTP + đổi pass

---

## 🧪 Tính Toán Cho Scale Lớn
- Redis Lock chống overbook vé
- Hủy đơn chưa thanh toán sau 15 phút
- Rate limit chống spam
- Phân biệt người dùng cá nhân & tổ chức
- Scheduler + Kafka (planned)

---

## 🚀 Mục Tiêu
> Xây dựng hệ thống booking sự kiện âm nhạc có thể mở rộng, hướng đến sản phẩm thực tế, tích hợp thanh toán, an toàn và dễ sử dụng cho cả người dùng lẫn đơn vị tổ chức.

---
