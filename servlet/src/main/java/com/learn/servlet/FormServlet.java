package com.learn.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * ============================================================
 * FormServlet - Xử lý Form (GET & POST)
 * ============================================================
 * 
 * GIẢI THÍCH:
 * Servlet này minh họa cách xử lý dữ liệu từ HTML form.
 * 
 * SỰ KHÁC BIỆT GIỮA GET VÀ POST:
 * 
 * GET:
 * - Dữ liệu nằm trên URL query string (?key=value&key2=value2)
 * - Dữ liệu visible trên thanh address
 * - Có giới hạn kích thước (~2048 ký tự)
 * - Có thể bookmark được
 * - KHÔNG nên dùng cho dữ liệu nhạy cảm (password)
 * - Dùng cho: tìm kiếm, filter, phân trang
 * 
 * POST:
 * - Dữ liệu nằm trong request body
 * - Dữ liệu KHÔNG hiển thị trên URL
 * - Không giới hạn kích thước
 * - Không thể bookmark
 * - Dùng cho: đăng ký, đăng nhập, tạo/sửa dữ liệu
 */
@WebServlet("/form")
public class FormServlet extends HttpServlet {

    /**
     * doGet() - Hiển thị form HTML
     * Khi user truy cập /form bằng GET → hiển thị form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='vi'>");
        out.println("<head>");
        out.println("  <meta charset='UTF-8'>");
        out.println("  <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("  <title>Form Servlet</title>");
        out.println("  <link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("  <div class='container'>");
        out.println("    <div class='card'>");
        out.println("      <h1>📝 Form Servlet</h1>");
        out.println("      <p class='subtitle'>Minh họa xử lý form với POST method</p>");
        out.println("      <form method='POST' action='/form' class='form'>");
        out.println("        <div class='form-group'>");
        out.println("          <label for='fullname'>Họ và tên:</label>");
        out.println("          <input type='text' id='fullname' name='fullname' placeholder='Nhập họ tên...' required>");
        out.println("        </div>");
        out.println("        <div class='form-group'>");
        out.println("          <label for='email'>Email:</label>");
        out.println("          <input type='email' id='email' name='email' placeholder='example@email.com' required>");
        out.println("        </div>");
        out.println("        <div class='form-group'>");
        out.println("          <label for='age'>Tuổi:</label>");
        out.println("          <input type='number' id='age' name='age' min='1' max='150' placeholder='Nhập tuổi...'>");
        out.println("        </div>");
        out.println("        <div class='form-group'>");
        out.println("          <label for='language'>Ngôn ngữ lập trình yêu thích:</label>");
        out.println("          <select id='language' name='language'>");
        out.println("            <option value='java'>Java</option>");
        out.println("            <option value='python'>Python</option>");
        out.println("            <option value='javascript'>JavaScript</option>");
        out.println("            <option value='csharp'>C#</option>");
        out.println("            <option value='go'>Go</option>");
        out.println("          </select>");
        out.println("        </div>");
        out.println("        <div class='form-group'>");
        out.println("          <label for='message'>Lời nhắn:</label>");
        out.println("          <textarea id='message' name='message' rows='4' placeholder='Viết gì đó...'></textarea>");
        out.println("        </div>");
        out.println("        <button type='submit' class='btn btn-primary'>Gửi Form</button>");
        out.println("      </form>");
        out.println("      <a href='/' class='btn btn-back'>← Về trang chủ</a>");
        out.println("    </div>");
        out.println("  </div>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * doPost() - Xử lý dữ liệu form khi submit
     * 
     * Khi user click "Gửi Form" → browser gửi POST request
     * → doPost() được gọi
     * → Lấy dữ liệu bằng request.getParameter("tên_field")
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ⚠️ QUAN TRỌNG: Set encoding cho request TRƯỚC khi đọc parameter
        // Nếu không, tiếng Việt sẽ bị lỗi ký tự
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        // Lấy dữ liệu từ form
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String age = request.getParameter("age");
        String language = request.getParameter("language");
        String message = request.getParameter("message");

        // Log ra console server (kiểm tra trong terminal)
        System.out.println("=== FORM DATA RECEIVED ===");
        System.out.println("Fullname: " + fullname);
        System.out.println("Email: " + email);
        System.out.println("Age: " + age);
        System.out.println("Language: " + language);
        System.out.println("Message: " + message);

        // In ra tất cả parameters (cách generic)
        System.out.println("\n--- All Parameters ---");
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            for (String val : paramValues) {
                System.out.println(paramName + " = " + val);
            }
        }

        // Gửi response
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='vi'>");
        out.println("<head>");
        out.println("  <meta charset='UTF-8'>");
        out.println("  <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("  <title>Kết quả Form</title>");
        out.println("  <link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("  <div class='container'>");
        out.println("    <div class='card'>");
        out.println("      <h1>✅ Dữ liệu đã nhận!</h1>");
        out.println("      <p class='subtitle'>Đây là dữ liệu từ <code>FormServlet.doPost()</code></p>");
        out.println("      <div class='info-box success'>");
        out.println("        <h3>📦 Dữ liệu Form:</h3>");
        out.println("        <table class='data-table'>");
        out.println("          <tr><th>Field</th><th>Value</th></tr>");
        out.println("          <tr><td>Họ tên</td><td>" + escapeHtml(fullname) + "</td></tr>");
        out.println("          <tr><td>Email</td><td>" + escapeHtml(email) + "</td></tr>");
        out.println("          <tr><td>Tuổi</td><td>" + escapeHtml(age) + "</td></tr>");
        out.println("          <tr><td>Ngôn ngữ</td><td>" + escapeHtml(language) + "</td></tr>");
        out.println("          <tr><td>Lời nhắn</td><td>" + escapeHtml(message) + "</td></tr>");
        out.println("        </table>");
        out.println("      </div>");
        out.println("      <div class='info-box'>");
        out.println("        <h3>💡 Giải thích:</h3>");
        out.println("        <ul>");
        out.println("          <li>Form dùng <code>method='POST'</code> → data nằm trong request body</li>");
        out.println("          <li>Servlet dùng <code>request.getParameter(\"name\")</code> để lấy data</li>");
        out.println("          <li>Data KHÔNG hiển thị trên URL (an toàn hơn GET)</li>");
        out.println("        </ul>");
        out.println("      </div>");
        out.println("      <a href='/form' class='btn btn-primary'>← Quay lại Form</a>");
        out.println("      <a href='/' class='btn btn-back'>← Về trang chủ</a>");
        out.println("    </div>");
        out.println("  </div>");
        out.println("</body>");
        out.println("</html>");
    }

    private String escapeHtml(String input) {
        if (input == null) return "(trống)";
        return input.replace("&", "&amp;")
                     .replace("<", "&lt;")
                     .replace(">", "&gt;")
                     .replace("\"", "&quot;")
                     .replace("'", "&#39;");
    }
}
