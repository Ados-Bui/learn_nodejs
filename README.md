# Dự án Học và Thực hành Node.js

Đây là kho lưu trữ tổng hợp các dự án và tài liệu cá nhân trong quá trình học tập và làm việc với Node.js cùng các công nghệ liên quan.

## Cấu trúc Thư mục

-   [**`learing_plan/`**](#lộ-trình-học-tập): Chứa lộ trình học tập chi tiết từ nền tảng đến nâng cao.
-   [**`Nginx_learn/`**](#thực-hành-nginx-với-docker): Ví dụ cấu hình Nginx làm Reverse Proxy cho 2 API Node.js sử dụng Docker Compose.
-   [**`nodejs_expressjs/`**](#api-ghi-chú-với-expressjs): Một REST API đơn giản để quản lý ghi chú được xây dựng bằng Express.js.
-   [**`nodejs_webix/`**](#ứng-dụng-ghi-chú-với-webix-và-nodejs): Một ứng dụng frontend đơn giản sử dụng thư viện Webix để tương tác với API ghi chú.

---

## Lộ trình Học tập (`learing_plan/`)

Thư mục này chứa file `nodejs-learning-roadmap.md`, mô tả chi tiết lộ trình học tập được cá nhân hóa, bao gồm:

-   **Nền tảng hệ thống:** Linux, Nginx.
-   **Node.js & Express.js:** Kiến thức cốt lõi về backend.
-   **Cơ sở dữ liệu:** SQL (PostgreSQL, MySQL,...) và cách tích hợp.
-   **Bảo mật & Triển khai:** Let's Encrypt, Serverless.
-   **Frontend:** Tích hợp với thư viện UI như Webix.

Xem chi tiết tại: [Lộ trình học Node.js](./learing_plan/nodejs-learning-roadmap.md).

---

## Thực hành Nginx với Docker (`Nginx_learn/`)

Một ví dụ điển hình về việc sử dụng Nginx làm **Reverse Proxy** và **Load Balancer** cho hai ứng dụng Node.js (`api1` và `api2`). Toàn bộ hệ thống được quản lý và triển khai dễ dàng thông qua `docker-compose`.

-   **Nginx:** Cấu hình tại `nginx/nginx.conf` để phân phối request đến 2 API.
-   **Node.js APIs:** Hai server Express đơn giản.
-   **SSL:** Script `generate-cert.sh` để tạo chứng chỉ tự ký (self-signed) cho môi trường development.
-   **Static Content:** Nginx phục vụ file `index.html` tĩnh.

Để chạy dự án:
```bash
docker-compose up -d --build
```

---

## API Ghi chú với Express.js (`nodejs_expressjs/`)

Một REST API hoàn chỉnh để quản lý các ghi chú, được xây dựng theo cấu trúc phân lớp rõ ràng (controllers, services, routes).

-   **Framework:** Express.js.
-   **Lưu trữ:** Dữ liệu được lưu trong bộ nhớ (in-memory) để đơn giản hóa.
-   **Endpoints:** Hỗ trợ đầy đủ các thao tác CRUD (Create, Read, Update, Delete) cho ghi chú.
-   **Validation:** Kiểm tra dữ liệu đầu vào.
-   **Error Handling:** Middleware xử lý lỗi tập trung.

Xem chi tiết các endpoints và cách chạy tại: [README của nodejs_expressjs](./nodejs_expressjs/README.md).

---

## Ứng dụng Ghi chú với Webix và Node.js (`nodejs_webix/`)

Một ứng dụng trang đơn (Single Page Application) sử dụng thư viện **Webix** để tạo giao diện người dùng. Giao diện này cho phép người dùng xem, tạo, sửa, xóa các ghi chú bằng cách gọi đến một API Node.js/Express đơn giản chạy ở backend.

-   **Frontend:** `index.html` và `noteDialog.js` sử dụng Webix UI.
-   **Backend:** `server.js` là một server Express cung cấp API cho frontend.
