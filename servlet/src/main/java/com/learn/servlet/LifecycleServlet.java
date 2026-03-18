package com.learn.servlet;

import javax.servlet.ServletConfig;
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
 * LifecycleServlet - Vòng đời của một Servlet
 * ============================================================
 * 
 * VÒNG ĐỜI SERVLET (Servlet Lifecycle):
 * 
 * 1. LOADING & INSTANTIATION (Tải và Khởi tạo)
 *    → Servlet Container (Tomcat) tải class và tạo instance
 *    → Constructor được gọi (chỉ 1 lần)
 * 
 * 2. INITIALIZATION (Khởi tạo)
 *    → init(ServletConfig) được gọi (chỉ 1 lần)
 *    → Dùng để setup resources (DB connection, config...)
 * 
 * 3. REQUEST HANDLING (Xử lý request)
 *    → service() → doGet()/doPost()/... được gọi (MỖI request)
 *    → Đây là nơi xử lý logic chính
 * 
 * 4. DESTRUCTION (Hủy)
 *    → destroy() được gọi (chỉ 1 lần, khi server shutdown)
 *    → Dùng để cleanup resources
 * 
 * QUAN TRỌNG:
 * - Servlet là SINGLETON → chỉ có 1 instance cho tất cả request
 * - Nhiều thread có thể gọi doGet()/doPost() ĐỒNG THỜI
 * - → Phải cẩn thận với shared state (biến instance)!
 */
@WebServlet("/lifecycle")
public class LifecycleServlet extends HttpServlet {

    // Biến instance - CHIA SẺ giữa tất cả request
    // ⚠️ CẢNH BÁO: Không thread-safe!
    private int totalRequests = 0;
    private String initTime;
    private String servletName;

    /**
     * CONSTRUCTOR - Bước 1: Tạo instance
     * Chỉ được gọi 1 lần khi servlet được load lần đầu
     */
    public LifecycleServlet() {
        super();
        System.out.println("🔨 [LIFECYCLE] Constructor called - Servlet instance created!");
    }

    /**
     * INIT - Bước 2: Khởi tạo
     * Chỉ được gọi 1 lần SAU constructor
     * Dùng để:
     * - Đọc config parameters
     * - Kết nối database
     * - Load resources
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.initTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        this.servletName = config.getServletName();
        System.out.println("🚀 [LIFECYCLE] init() called at " + initTime);
        System.out.println("   Servlet Name: " + servletName);
    }

    /**
     * SERVICE - Bước 3: Xử lý request
     * Được gọi MỖI lần có request đến
     * 
     * Flow: service() phân loại request → gọi doGet()/doPost()/...
     * Thường ta KHÔNG override service(), mà override doGet()/doPost()
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Tăng counter (⚠️ Không thread-safe - chỉ là demo)
        totalRequests++;
        int currentRequest = totalRequests;

        System.out.println("📨 [LIFECYCLE] doGet() called - Request #" + currentRequest
                + " from " + request.getRemoteAddr());

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='vi'>");
        out.println("<head>");
        out.println("  <meta charset='UTF-8'>");
        out.println("  <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("  <title>Servlet Lifecycle</title>");
        out.println("  <link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("  <div class='container'>");
        out.println("    <div class='card'>");
        out.println("      <h1>🔄 Servlet Lifecycle</h1>");
        out.println("      <p class='subtitle'>Minh họa vòng đời của một Servlet</p>");

        // Lifecycle diagram
        out.println("      <div class='info-box lifecycle'>");
        out.println("        <h3>📐 Vòng đời Servlet:</h3>");
        out.println("        <div class='lifecycle-diagram'>");
        out.println("          <div class='lifecycle-step done'>");
        out.println("            <span class='step-num'>1</span>");
        out.println("            <span class='step-name'>Constructor</span>");
        out.println("            <span class='step-desc'>Tạo instance (1 lần)</span>");
        out.println("          </div>");
        out.println("          <div class='lifecycle-arrow'>→</div>");
        out.println("          <div class='lifecycle-step done'>");
        out.println("            <span class='step-num'>2</span>");
        out.println("            <span class='step-name'>init()</span>");
        out.println("            <span class='step-desc'>Khởi tạo (1 lần)</span>");
        out.println("          </div>");
        out.println("          <div class='lifecycle-arrow'>→</div>");
        out.println("          <div class='lifecycle-step active'>");
        out.println("            <span class='step-num'>3</span>");
        out.println("            <span class='step-name'>service()</span>");
        out.println("            <span class='step-desc'>Xử lý request (mỗi lần)</span>");
        out.println("          </div>");
        out.println("          <div class='lifecycle-arrow'>→</div>");
        out.println("          <div class='lifecycle-step pending'>");
        out.println("            <span class='step-num'>4</span>");
        out.println("            <span class='step-name'>destroy()</span>");
        out.println("            <span class='step-desc'>Cleanup (khi shutdown)</span>");
        out.println("          </div>");
        out.println("        </div>");
        out.println("      </div>");

        // Stats
        out.println("      <div class='info-box'>");
        out.println("        <h3>📊 Thống kê Servlet:</h3>");
        out.println("        <table class='data-table'>");
        out.println("          <tr><th>Thông tin</th><th>Giá trị</th></tr>");
        out.println("          <tr><td>Servlet Name</td><td>" + servletName + "</td></tr>");
        out.println("          <tr><td>Init Time</td><td>" + initTime + "</td></tr>");
        out.println("          <tr><td>Current Time</td><td>" + currentTime + "</td></tr>");
        out.println("          <tr><td>Total Requests</td><td><strong>" + currentRequest + "</strong></td></tr>");
        out.println("          <tr><td>Hash Code (Instance ID)</td><td>" + this.hashCode() + "</td></tr>");
        out.println("        </table>");
        out.println("      </div>");

        out.println("      <div class='info-box warning'>");
        out.println("        <h3>⚠️ Lưu ý quan trọng:</h3>");
        out.println("        <ul>");
        out.println("          <li><strong>Singleton:</strong> Chỉ có 1 instance → Hash Code luôn giống nhau</li>");
        out.println("          <li><strong>totalRequests tăng:</strong> Vì là biến instance, chia sẻ giữa tất cả request</li>");
        out.println("          <li><strong>Thread Safety:</strong> totalRequests++ không thread-safe, cần dùng AtomicInteger trong production</li>");
        out.println("          <li><strong>init() chỉ 1 lần:</strong> Init Time không thay đổi dù reload nhiều lần</li>");
        out.println("        </ul>");
        out.println("      </div>");

        out.println("      <div class='btn-group'>");
        out.println("        <a href='/lifecycle' class='btn btn-primary'>🔄 Reload (tăng counter)</a>");
        out.println("        <a href='/' class='btn btn-back'>← Về trang chủ</a>");
        out.println("      </div>");
        out.println("    </div>");
        out.println("  </div>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * DESTROY - Bước 4: Hủy
     * Được gọi 1 lần duy nhất khi:
     * - Server shutdown
     * - Ứng dụng bị undeploy
     * - Servlet container quyết định unload
     * 
     * Dùng để:
     * - Đóng kết nối database
     * - Giải phóng resources
     * - Lưu trạng thái
     */
    @Override
    public void destroy() {
        System.out.println("💀 [LIFECYCLE] destroy() called - Servlet is being destroyed!");
        System.out.println("   Total requests served: " + totalRequests);
        super.destroy();
    }
}
