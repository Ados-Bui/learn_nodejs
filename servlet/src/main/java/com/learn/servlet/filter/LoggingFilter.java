package com.learn.servlet.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ============================================================
 * LoggingFilter - Servlet Filter
 * ============================================================
 * 
 * FILTER LÀ GÌ?
 * Filter là một component chạy TRƯỚC và SAU khi request đến servlet.
 * Nó hoạt động như một "middleware" (nếu bạn biết Express.js/Node.js).
 * 
 * FLOW:
 * Client → Filter 1 → Filter 2 → ... → Servlet → ... → Filter 2 → Filter 1 → Client
 * 
 * ỨNG DỤNG THỰC TẾ:
 * - Logging (ghi log request/response)
 * - Authentication (kiểm tra đăng nhập)
 * - CORS headers
 * - Compression (nén response)
 * - Encoding (set UTF-8)
 * - Rate limiting
 * 
 * CHUỖI FILTER (Filter Chain):
 * Nhiều filter có thể chạy tuần tự. Thứ tự được quyết định bởi:
 * 1. Thứ tự khai báo trong web.xml
 * 2. Hoặc @WebFilter annotation
 * 
 * MỖI filter PHẢI gọi chain.doFilter() để chuyển request sang filter/servlet tiếp theo.
 * Nếu KHÔNG gọi chain.doFilter() → request bị chặn tại đây!
 */
public class LoggingFilter implements Filter {

    private FilterConfig filterConfig;

    /**
     * init() - Khởi tạo filter (chỉ 1 lần)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        System.out.println("🔍 [FILTER] LoggingFilter initialized!");
    }

    /**
     * doFilter() - Xử lý chính của filter
     * Được gọi MỖI lần có request match URL pattern
     * 
     * @param request  - Request từ client
     * @param response - Response gửi về client
     * @param chain    - Chuỗi filter tiếp theo
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Cast sang HTTP-specific types
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // ============================================
        // TRƯỚC khi request đến Servlet (PRE-PROCESSING)
        // ============================================
        long startTime = System.currentTimeMillis();
        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();
        String queryString = httpRequest.getQueryString();
        String clientIP = httpRequest.getRemoteAddr();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        System.out.println();
        System.out.println("═══════════════════════════════════════════════════");
        System.out.printf("🔍 [FILTER] [%s] %s %s%s%n",
                timestamp,
                method,
                uri,
                queryString != null ? "?" + queryString : "");
        System.out.printf("   Client: %s | User-Agent: %s%n",
                clientIP,
                httpRequest.getHeader("User-Agent"));
        System.out.println("───────────────────────────────────────────────────");

        // ============================================
        // CHUYỂN REQUEST CHO FILTER/SERVLET TIẾP THEO
        // ⚠️ BẮT BUỘC phải gọi này, nếu không request bị chặn!
        // ============================================
        chain.doFilter(request, response);

        // ============================================
        // SAU khi Servlet xử lý xong (POST-PROCESSING)
        // ============================================
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("───────────────────────────────────────────────────");
        System.out.printf("✅ [FILTER] Response: %d | Time: %dms%n",
                httpResponse.getStatus(),
                duration);
        System.out.println("═══════════════════════════════════════════════════");
    }

    /**
     * destroy() - Cleanup filter (khi server shutdown)
     */
    @Override
    public void destroy() {
        System.out.println("🔍 [FILTER] LoggingFilter destroyed!");
    }
}
