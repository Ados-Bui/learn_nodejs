# Lộ trình học Node.js (sắp xếp lại)

> Mục tiêu: học đủ nền tảng để xây dựng, triển khai và vận hành ứng dụng Node.js thực tế.

## Tổng quan thứ tự học
1. Linux base
2. Nginx
3. Node.js 14.x
4. Express.js
5. MySQL / PostgreSQL / SQL Server / Oracle (cơ chế + cách dùng)
6. Let's Encrypt
7. Serverless
8. Webix

---

## Giai đoạn 1: Nền tảng hệ thống (Tuần 1-2)
Tài liệu chi tiết: [Giai đoạn 1 - Nền tảng hệ thống](./giai-doan-1-nen-tang-he-thong.md)

### 1) Linux base
- Nắm command line: `cd`, `ls`, `cp`, `mv`, `rm`, `cat`, `grep`, `find`, `chmod`, `chown`
- Quản lý process: `ps`, `top`, `kill`, `systemctl`
- Quản lý network cơ bản: `curl`, `netstat`/`ss`, `ping`
- Quản lý package (Ubuntu): `apt update`, `apt install`
- Mục tiêu đầu ra: tự setup được 1 server Linux cơ bản cho Node.js

### 2) Nginx
- Hiểu reverse proxy là gì
- Cấu hình Nginx trỏ vào app Node.js (port 3000)
- Static file + gzip + basic cache
- Log và xử lý lỗi phổ biến
- Mục tiêu đầu ra: chạy Node.js sau Nginx ổn định

---

## Giai đoạn 2: Backend Node.js cốt lõi (Tuần 3-5)
Tài liệu chi tiết: [Giai đoạn 2 - Backend Node.js cốt lõi](./giai-doan-2-backend-nodejs.md)

### 3) Node.js 14.x
- Event loop, async/await, Promise
- Module system (`require`, `module.exports`)
- File system, stream, env config
- Xử lý lỗi và logging cơ bản
- Mục tiêu đầu ra: viết được app Node.js có cấu trúc rõ ràng

### 4) Express.js
- Routing, middleware, error middleware
- REST API CRUD
- Validate input, phân tầng controller/service/repository
- Auth cơ bản (JWT/session)
- Mục tiêu đầu ra: hoàn thành 1 REST API nhỏ bằng Express

---

## Giai đoạn 3: Dữ liệu & tích hợp DB (Tuần 6-8)
Tài liệu chi tiết: [Giai đoạn 3 - Dữ liệu & tích hợp DB](./giai-doan-3-du-lieu-db.md)

### 5) MySQL / PostgreSQL / SQL Server / Oracle
#### A. Cơ chế cần tìm hiểu
- Khác nhau SQL dialect, transaction, isolation level
- Index, query plan, tối ưu truy vấn
- Connection pooling
- Migration và backup/restore cơ bản

#### B. Cách dùng với Node.js
- Dùng driver/ORM phù hợp (ví dụ: `knex`, `sequelize`, hoặc driver riêng)
- Viết CRUD + transaction
- Viết query tối ưu và chống SQL injection

#### C. Gợi ý thứ tự học DB
1. PostgreSQL hoặc MySQL (để nắm nền tảng nhanh)
2. SQL Server
3. Oracle

- Mục tiêu đầu ra: kết nối được ít nhất 2 hệ quản trị DB từ Node.js và viết API CRUD chuẩn

---

## Giai đoạn 4: Bảo mật & triển khai (Tuần 9-10)
Tài liệu chi tiết: [Giai đoạn 4 - Bảo mật & triển khai](./giai-doan-4-bao-mat-trien-khai.md)

### 6) Let's Encrypt
- Hiểu SSL/TLS và HTTPS
- Cấp chứng chỉ bằng Certbot
- Auto renew certificate
- Cấu hình HTTPS trên Nginx
- Mục tiêu đầu ra: domain chạy HTTPS hợp lệ

### 7) Serverless
- Khái niệm FaaS, cold start, stateless
- Triển khai API Node.js lên 1 nền tảng serverless (AWS Lambda/Azure Functions/Vercel)
- Quản lý biến môi trường, logging, monitoring
- Mục tiêu đầu ra: deploy 1 API Express (hoặc route tương đương) lên serverless

---

## Giai đoạn 5: Frontend tích hợp (Tuần 11)
Tài liệu chi tiết: [Giai đoạn 5 - Frontend tích hợp Webix](./giai-doan-5-frontend-webix.md)

### 8) Webix
- Nắm component cơ bản, data table, form
- Kết nối API Node.js đã xây
- Validate form + phân trang + lọc dữ liệu
- Mục tiêu đầu ra: 1 giao diện admin nhỏ dùng Webix gọi API backend

---

## Dự án thực hành cuối lộ trình (Tuần 12)
Xây 1 mini project full flow:
- Backend: Node.js 14.x + Express.js
- DB: PostgreSQL hoặc MySQL
- Reverse proxy: Nginx
- HTTPS: Let's Encrypt
- Triển khai: server Linux hoặc serverless
- Frontend: Webix dashboard

Kết quả mong muốn:
- Có repo hoàn chỉnh
- Có tài liệu setup (`README`)
- Có checklist vận hành cơ bản (deploy, log, backup DB)

---

## Cách học mỗi tuần (gợi ý)
- 40%: học lý thuyết có ghi chú
- 60%: code thực hành
- Cuối tuần: tổng kết + đẩy code lên Git

## Checklist tự đánh giá
- [ ] Tự dựng Linux + Nginx + Node app
- [ ] Viết API Express có auth + validate
- [ ] Kết nối và tối ưu query cho ít nhất 2 loại DB
- [ ] Cấu hình HTTPS bằng Let's Encrypt
- [ ] Deploy được 1 bản serverless
- [ ] Làm được UI Webix kết nối backend
