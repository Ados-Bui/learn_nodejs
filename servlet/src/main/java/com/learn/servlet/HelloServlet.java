package com.learn.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ============================================================
 * HelloServlet - Servlet cơ bản nhất
 * ============================================================
 * 
 * Đây là ví dụ đơn giản nhất về một Servlet.
 * 
 * GIẢI THÍCH:
 * - @WebServlet("/hello"): Annotation đăng ký servlet với URL pattern "/hello"
 *   (Cách hiện đại thay thế cho khai báo trong web.xml)
 *   ⚠️ Trong project này, ta đã khai báo trong web.xml rồi nên KHÔNG cần annotation.
 *   Nhưng để minh họa, ta vẫn giữ ở đây (web.xml sẽ override).
 * 
 * - HttpServlet: Class cha cho tất cả HTTP servlet
 *   Cung cấp các phương thức: doGet(), doPost(), doPut(), doDelete()...
 * 
 * - HttpServletRequest: Chứa thông tin request từ client
 *   (URL, parameters, headers, cookies, session...)
 * 
 * - HttpServletResponse: Dùng để gửi response về client
 *   (status code, headers, body content...)
 * 
 * FLOW: Client → Request → Server → Servlet Container → HelloServlet.doGet() → Response → Client
 */
public class HelloServlet extends HttpServlet {

    /**
     * doGet() - Xử lý HTTP GET request
     * 
     * Được gọi khi:
     * - Người dùng nhập URL trên trình duyệt
     * - Click vào link <a href="/hello">
     * - JavaScript fetch("/hello", {method: "GET"})
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Set kiểu nội dung response (Content-Type)
        response.setContentType("text/html");
        // 2. Set encoding UTF-8 để hỗ trợ tiếng Việt
        response.setCharacterEncoding("UTF-8");

        // 3. Lấy tham số từ URL query string
        // Ví dụ: /hello?name=Minh → name = "Minh"
        String name = request.getParameter("name");
        if (name == null || name.trim().isEmpty()) {
            name = "World";
        }

        // 4. Lấy thời gian hiện tại
        String currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        // 5. Lấy thông tin từ request
        String clientIP = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String method = request.getMethod();

        // 6. Ghi response HTML
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='vi'>");
        out.println("<head>");
        out.println("  <meta charset='UTF-8'>");
        out.println("  <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("  <title>Hello Servlet</title>");
        out.println("  <link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("  <div class='container'>");
        out.println("    <div class='card'>");
        out.println("      <h1>👋 Xin chào, " + escapeHtml(name) + "!</h1>");
        out.println("      <p class='subtitle'>Đây là response từ <code>HelloServlet.doGet()</code></p>");
        out.println("      <div class='info-box'>");
        out.println("        <h3>📋 Thông tin Request:</h3>");
        out.println("        <ul>");
        out.println("          <li><strong>HTTP Method:</strong> " + method + "</li>");
        out.println("          <li><strong>Client IP:</strong> " + clientIP + "</li>");
        out.println("          <li><strong>User-Agent:</strong> " + escapeHtml(userAgent) + "</li>");
        out.println("          <li><strong>Thời gian:</strong> " + currentTime + "</li>");
        out.println("          <li><strong>Request URI:</strong> " + request.getRequestURI() + "</li>");
        out.println("          <li><strong>Query String:</strong> " + (request.getQueryString() != null ? escapeHtml(request.getQueryString()) : "null") + "</li>");
        out.println("        </ul>");
        out.println("      </div>");
        out.println("      <div class='try-it'>");
        out.println("        <h3>🧪 Thử nghiệm:</h3>");
        out.println("        <p>Thêm <code>?name=TenCuaBan</code> vào URL để thay đổi tên!</p>");
        out.println("        <p>Ví dụ: <a href='/hello?name=Minh'>/hello?name=Minh</a></p>");
        out.println("      </div>");
        out.println("      <a href='/' class='btn btn-back'>← Về trang chủ</a>");
        out.println("    </div>");
        out.println("  </div>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Hàm helper để escape HTML, tránh XSS attack
     * Luôn escape dữ liệu từ user trước khi hiển thị!
     */
    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                     .replace("<", "&lt;")
                     .replace(">", "&gt;")
                     .replace("\"", "&quot;")
                     .replace("'", "&#39;");
    }
}
