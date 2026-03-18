package com.learn.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ============================================================
 * ApiServlet - REST API đơn giản với Servlet
 * ============================================================
 * 
 * GIẢI THÍCH:
 * Servlet này minh họa cách tạo một REST API đơn giản.
 * Thay vì trả về HTML, servlet trả về JSON.
 * 
 * REST API PATTERN:
 * - GET    /api/todos      → Lấy danh sách todos
 * - POST   /api/todos      → Tạo todo mới
 * - DELETE  /api/todos?id=1 → Xóa todo
 * 
 * RESPONSE FORMAT: JSON (application/json)
 * 
 * LƯU Ý: Trong thực tế, bạn sẽ dùng Spring Boot hoặc JAX-RS
 * để xây dựng REST API. Đây chỉ là minh họa cách servlet hoạt động.
 */
@WebServlet("/api/todos")
public class ApiServlet extends HttpServlet {

    // "Database" tạm thời (lưu trong memory)
    private final Map<Integer, Map<String, Object>> todos = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(0);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void init() throws ServletException {
        // Thêm dữ liệu mẫu
        addTodo("Học Java Servlet", false);
        addTodo("Tìm hiểu về HTTP", true);
        addTodo("Thực hành REST API", false);
        System.out.println("📋 [API] ApiServlet initialized with sample data");
    }

    /**
     * GET /api/todos → Trả về danh sách todos dạng JSON
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set response headers cho JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // CORS headers (cho phép frontend khác domain gọi API)
        response.setHeader("Access-Control-Allow-Origin", "*");

        String idParam = request.getParameter("id");

        PrintWriter out = response.getWriter();

        if (idParam != null) {
            // GET /api/todos?id=1 → Lấy 1 todo cụ thể
            try {
                int id = Integer.parseInt(idParam);
                Map<String, Object> todo = todos.get(id);
                if (todo != null) {
                    out.print(gson.toJson(createResponse(true, "Thành công", todo)));
                } else {
                    response.setStatus(404);
                    out.print(gson.toJson(createResponse(false, "Todo không tồn tại", null)));
                }
            } catch (NumberFormatException e) {
                response.setStatus(400);
                out.print(gson.toJson(createResponse(false, "ID không hợp lệ", null)));
            }
        } else {
            // GET /api/todos → Lấy tất cả
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("success", true);
            result.put("count", todos.size());
            result.put("data", new ArrayList<>(todos.values()));
            out.print(gson.toJson(result));
        }
    }

    /**
     * POST /api/todos → Tạo todo mới
     * Request body: { "title": "Todo mới", "completed": false }
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        // Đọc request body (JSON)
        StringBuilder body = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> input = gson.fromJson(body.toString(), Map.class);
            String title = (String) input.get("title");

            if (title == null || title.trim().isEmpty()) {
                response.setStatus(400);
                PrintWriter out = response.getWriter();
                out.print(gson.toJson(createResponse(false, "Title không được trống", null)));
                return;
            }

            boolean completed = input.get("completed") != null && (Boolean) input.get("completed");
            Map<String, Object> newTodo = addTodo(title.trim(), completed);

            response.setStatus(201); // 201 Created
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(createResponse(true, "Tạo todo thành công", newTodo)));

        } catch (Exception e) {
            response.setStatus(400);
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(createResponse(false, "Dữ liệu không hợp lệ: " + e.getMessage(), null)));
        }
    }

    /**
     * DELETE /api/todos?id=1 → Xóa todo
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        String idParam = request.getParameter("id");
        PrintWriter out = response.getWriter();

        if (idParam == null) {
            response.setStatus(400);
            out.print(gson.toJson(createResponse(false, "Thiếu parameter 'id'", null)));
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Map<String, Object> removed = todos.remove(id);
            if (removed != null) {
                out.print(gson.toJson(createResponse(true, "Xóa thành công", removed)));
            } else {
                response.setStatus(404);
                out.print(gson.toJson(createResponse(false, "Todo không tồn tại", null)));
            }
        } catch (NumberFormatException e) {
            response.setStatus(400);
            out.print(gson.toJson(createResponse(false, "ID không hợp lệ", null)));
        }
    }

    // ====== Helper Methods ======

    private Map<String, Object> addTodo(String title, boolean completed) {
        int id = idCounter.incrementAndGet();
        Map<String, Object> todo = new LinkedHashMap<>();
        todo.put("id", id);
        todo.put("title", title);
        todo.put("completed", completed);
        todo.put("createdAt", new Date().toString());
        todos.put(id, todo);
        return todo;
    }

    private Map<String, Object> createResponse(boolean success, String message, Object data) {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("success", success);
        resp.put("message", message);
        if (data != null) {
            resp.put("data", data);
        }
        return resp;
    }
}
