package com.learn.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * ============================================================
 * SessionServlet - Quản lý Session
 * ============================================================
 * 
 * SESSION LÀ GÌ?
 * HTTP là giao thức "stateless" (không trạng thái).
 * Mỗi request là độc lập, server không "nhớ" client nào đã gửi request trước đó.
 * 
 * → Session giải quyết vấn đề này bằng cách:
 * 1. Server tạo một Session ID duy nhất cho mỗi client
 * 2. Session ID được lưu trong cookie trên browser (JSESSIONID)
 * 3. Browser tự động gửi cookie này với mỗi request
 * 4. Server dùng Session ID để tìm lại dữ liệu của client
 * 
 * FLOW:
 * Client lần đầu → Server tạo Session → Gửi JSESSIONID cookie → Client
 * Client lần sau  → Gửi JSESSIONID cookie → Server tìm Session → Trả dữ liệu
 * 
 * ỨNG DỤNG THỰC TẾ:
 * - Giỏ hàng (shopping cart)
 * - Đăng nhập / xác thực
 * - Lưu trạng thái user (ngôn ngữ, theme...)
 */
@WebServlet("/session")
public class SessionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        // ============================================
        // LẤY HOẶC TẠO SESSION
        // ============================================
        // getSession(true) → tạo session mới nếu chưa có
        // getSession(false) → trả về null nếu chưa có session
        HttpSession session = request.getSession(true);

        // Đếm số lần truy cập (lưu trong session)
        Integer visitCount = (Integer) session.getAttribute("visitCount");
        if (visitCount == null) {
            visitCount = 1;
        } else {
            visitCount++;
        }
        session.setAttribute("visitCount", visitCount);

        // Lưu lịch sử truy cập
        @SuppressWarnings("unchecked")
        List<String> visitHistory = (List<String>) session.getAttribute("visitHistory");
        if (visitHistory == null) {
            visitHistory = new ArrayList<>();
        }
        visitHistory.add(new Date().toString());
        // Chỉ giữ 10 lần gần nhất
        if (visitHistory.size() > 10) {
            visitHistory.remove(0);
        }
        session.setAttribute("visitHistory", visitHistory);

        // Lấy tên user (nếu đã set)
        String userName = (String) session.getAttribute("userName");

        // Xử lý action
        String action = request.getParameter("action");
        if ("clear".equals(action)) {
            // Hủy session hoàn toàn
            session.invalidate();
            response.sendRedirect("/session");
            return;
        }

        // Session info
        String sessionId = session.getId();
        long creationTime = session.getCreationTime();
        long lastAccessed = session.getLastAccessedTime();
        int maxInactive = session.getMaxInactiveInterval();
        boolean isNew = session.isNew();

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='vi'>");
        out.println("<head>");
        out.println("  <meta charset='UTF-8'>");
        out.println("  <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("  <title>Session Servlet</title>");
        out.println("  <link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("  <div class='container'>");
        out.println("    <div class='card'>");
        out.println("      <h1>🔐 Session Management</h1>");
        out.println("      <p class='subtitle'>Minh họa quản lý session trong Servlet</p>");

        // Hiển thị thông tin session
        out.println("      <div class='info-box'>");
        out.println("        <h3>📊 Thông tin Session:</h3>");
        out.println("        <table class='data-table'>");
        out.println("          <tr><th>Thuộc tính</th><th>Giá trị</th></tr>");
        out.println("          <tr><td>Session ID</td><td><code>" + sessionId + "</code></td></tr>");
        out.println("          <tr><td>Session mới?</td><td>" + (isNew ? "✅ Có" : "❌ Không") + "</td></tr>");
        out.println("          <tr><td>Lần truy cập thứ</td><td><strong>" + visitCount + "</strong></td></tr>");
        out.println("          <tr><td>Tạo lúc</td><td>" + new Date(creationTime) + "</td></tr>");
        out.println("          <tr><td>Truy cập cuối</td><td>" + new Date(lastAccessed) + "</td></tr>");
        out.println("          <tr><td>Timeout</td><td>" + maxInactive + " giây (" + (maxInactive / 60) + " phút)</td></tr>");
        if (userName != null) {
            out.println("          <tr><td>Tên user</td><td>👤 " + escapeHtml(userName) + "</td></tr>");
        }
        out.println("        </table>");
        out.println("      </div>");

        // Form đặt tên user vào session
        out.println("      <div class='info-box'>");
        out.println("        <h3>👤 Lưu tên vào Session:</h3>");
        out.println("        <form method='POST' action='/session' class='form inline-form'>");
        out.println("          <div class='form-group'>");
        out.println("            <input type='text' name='userName' placeholder='Nhập tên...' value='" + (userName != null ? escapeHtml(userName) : "") + "'>");
        out.println("          </div>");
        out.println("          <button type='submit' class='btn btn-primary'>Lưu vào Session</button>");
        out.println("        </form>");
        out.println("      </div>");

        // Lịch sử truy cập
        out.println("      <div class='info-box'>");
        out.println("        <h3>📜 Lịch sử truy cập (lưu trong Session):</h3>");
        out.println("        <ol>");
        for (String visit : visitHistory) {
            out.println("          <li>" + visit + "</li>");
        }
        out.println("        </ol>");
        out.println("      </div>");

        // Tất cả session attributes
        out.println("      <div class='info-box'>");
        out.println("        <h3>📦 Tất cả Session Attributes:</h3>");
        out.println("        <table class='data-table'>");
        out.println("          <tr><th>Key</th><th>Value</th><th>Type</th></tr>");
        Enumeration<String> attrNames = session.getAttributeNames();
        while (attrNames.hasMoreElements()) {
            String attrName = attrNames.nextElement();
            Object attrValue = session.getAttribute(attrName);
            out.println("          <tr><td>" + attrName + "</td><td>" + attrValue + "</td><td>" + attrValue.getClass().getSimpleName() + "</td></tr>");
        }
        out.println("        </table>");
        out.println("      </div>");

        // Buttons
        out.println("      <div class='btn-group'>");
        out.println("        <a href='/session' class='btn btn-primary'>🔄 Reload (tăng count)</a>");
        out.println("        <a href='/session?action=clear' class='btn btn-danger'>🗑️ Xóa Session</a>");
        out.println("        <a href='/' class='btn btn-back'>← Về trang chủ</a>");
        out.println("      </div>");
        out.println("    </div>");
        out.println("  </div>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * doPost() - Lưu tên user vào session
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String userName = request.getParameter("userName");
        HttpSession session = request.getSession(true);

        if (userName != null && !userName.trim().isEmpty()) {
            session.setAttribute("userName", userName.trim());
        }

        // Redirect về GET (PRG Pattern - Post/Redirect/Get)
        // Tránh việc user refresh trang → gửi lại POST
        response.sendRedirect("/session");
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                     .replace("<", "&lt;")
                     .replace(">", "&gt;")
                     .replace("\"", "&quot;")
                     .replace("'", "&#39;");
    }
}
