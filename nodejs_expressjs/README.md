# Mini Express Notes API

Một REST API đơn giản để quản lý ghi chú, được xây dựng bằng Express.js. Dữ liệu được lưu trữ trong bộ nhớ (in-memory).

## Cài đặt và Chạy

1.  **Cài đặt dependencies:**
    ```bash
    npm install
    ```

2.  **Chạy server ở chế độ development (với nodemon):**
    ```bash
    npm run dev
    ```
    Server sẽ chạy tại `http://localhost:3000`.

3.  **Chạy server ở chế độ production:**
    ```bash
    npm start
    ```

## API Endpoints

Tất cả các endpoint đều có tiền tố là `/api/notes`.

-   `GET /health`: Kiểm tra "sức khỏe" của server.
    -   **Response:** `200 OK`
        ```json
        {
          "status": "ok"
        }
        ```

-   `GET /api/notes`: Lấy danh sách tất cả ghi chú.
    -   **Hỗ trợ query:** `?q=<từ khóa>` để tìm kiếm theo tiêu đề.
    -   **Response:** `200 OK`
        ```json
        {
          "data": [
            {
              "id": 1,
              "title": "Học Express",
              "content": "Nắm vững routing, middleware.",
              "createdAt": "2026-03-15T10:00:00.000Z",
              "updatedAt": "2026-03-15T10:00:00.000Z"
            }
          ]
        }
        ```

-   `GET /api/notes/:id`: Lấy một ghi chú theo ID.
    -   **Response:** `200 OK` hoặc `404 Not Found` nếu không tìm thấy.

-   `POST /api/notes`: Tạo một ghi chú mới.
    -   **Request Body:**
        ```json
        {
          "title": "Tiêu đề mới",
          "content": "Nội dung tùy chọn"
        }
        ```
    -   **Validation:** `title` là bắt buộc và không được rỗng.
    -   **Response:** `201 Created`

-   `PUT /api/notes/:id`: Cập nhật một ghi chú đã có.
    -   **Request Body:** Tương tự như `POST`.
    -   **Response:** `200 OK` hoặc `404 Not Found`.

-   `DELETE /api/notes/:id`: Xóa một ghi chú.
    -   **Response:** `204 No Content` hoặc `404 Not Found`.