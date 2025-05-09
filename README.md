# 🎫 Vecanhac.com – Music Ticket Booking Platform

**Vecanhac.com** là một nền tảng đặt vé sự kiện âm nhạc, bao gồm hệ thống hiển thị danh sách sự kiện, chi tiết sự kiện, các loại vé, trạng thái vé và gợi ý sự kiện tương tự.

---

## 🧩 Công nghệ sử dụng

### Frontend (FE)
- **Next.js** (App Router)
- **TailwindCSS**
- Tối ưu cho mobile + desktop
- Component hóa từng phần như: `EventCard`, `FilterPanel`, `DateDropdown`,...
- Link: https://github.com/20yud/vecanhacUI

### Backend (BE)
- **Java Spring Boot**
- Theo mô hình **DDD (Domain-Driven Design)**:
  - `domain`: chứa `Entity`, `Repository`
  - `application`: chứa `DTO`, `Service`
  - `controller`: chứa các REST API

#### BE sử dụng thêm:
- **Spring Data JPA**
- **Lombok**
- **MySQL**
- *(Dự kiến)* MapStruct để mapping DTO ↔ Entity

---

## 🖼 Trang chủ (Home Page)

Hiển thị danh sách các sự kiện đặc biệt với:
- Lọc theo ngày, thành phố, danh mục
- Phân trang: “Tải thêm” sự kiện
- Responsive toàn giao diện

![image](https://github.com/user-attachments/assets/5fa18e32-dd08-44d8-a175-497d1a7c3306)


---

## 📄 Trang chi tiết sự kiện (Event Detail Page)

Gồm các phần:
- Tiêu đề, thời gian, địa điểm, banner sự kiện
- Mô tả chi tiết chương trình
- Đơn vị tổ chức: logo + thông tin
![image](https://github.com/user-attachments/assets/efd7fa0c-9aa8-42dd-bdb8-96e5cb33fd63)

---

## 🎟️ Ticket Section

- Các vé được **nhóm theo ngày diễn ra**
- Hiển thị trạng thái: `AVAILABLE` / `SOLD OUT`
- Nhấp vào nhóm ngày để **mở dropdown** các vé chi tiết
- Có nút **“Mua ngay”**, khi bấm sẽ tự scroll đến phần vé
![image](https://github.com/user-attachments/assets/aae4a639-bd06-4e02-a643-ca51e59c7208)


