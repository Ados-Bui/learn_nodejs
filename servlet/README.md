# ☕ Servlet Learning Project

Dự án học tập Java Servlet với các ví dụ thực hành minh họa các khái niệm quan trọng.

---

## 📁 Cấu trúc dự án

```
servlet/
├── pom.xml                              ← Maven config (quản lý dependencies)
├── README.md                            ← File này
└── src/main/
    ├── java/com/learn/servlet/          ← Source code Java
    │   ├── HelloServlet.java            ← [1] GET request cơ bản
    │   ├── FormServlet.java             ← [2] Xử lý form (GET/POST)
    │   ├── SessionServlet.java          ← [3] Quản lý HTTP Session
    │   ├── LifecycleServlet.java        ← [4] Vòng đời Servlet
    │   ├── ApiServlet.java              ← [5] REST API trả JSON
    │   └── filter/
    │       └── LoggingFilter.java       ← [6] Filter (Middleware)
    └── webapp/                          ← Web resources
        ├── WEB-INF/
        │   └── web.xml                  ← Deployment Descriptor
        ├── index.html                   ← Trang chủ
        ├── api-test.html                ← Giao diện test API
        ├── css/style.css                ← Stylesheet
        └── error/
            ├── 404.html                 ← Trang lỗi 404
            └── 500.html                 ← Trang lỗi 500
```

---

## 🚀 Cách chạy dự án

### Yêu cầu
- **Java JDK 11+** (khuyến nghị JDK 17)
- **Apache Maven 3.6+**

### Chạy bằng Maven Tomcat Plugin (khuyến nghị)
```bash
cd servlet
mvn tomcat7:run
```
→ Truy cập: **http://localhost:8080**

### Hoặc build WAR file và deploy lên Tomcat
```bash
cd servlet
mvn clean package
```
→ Copy file `target/servlet-learn.war` vào thư mục `webapps/` của Tomcat.

---

## 📚 Giải thích chi tiết từng thành phần

### 1. Servlet là gì?

**Servlet** là một Java class chạy trên server, dùng để xử lý HTTP request và trả về HTTP response.

```
┌──────────┐     HTTP Request      ┌──────────────────┐     ┌─────────────┐
│  Browser  │ ──────────────────→  │  Servlet Container │ ──→ │   Servlet   │
│ (Client)  │ ←──────────────────  │    (Tomcat)        │ ←── │ (Java code) │
└──────────┘     HTTP Response     └──────────────────┘     └─────────────┘
```

**So sánh với Node.js/Express:**
| Concept | Node.js/Express | Java Servlet |
|---------|----------------|-------------|
| Server | Express app | Tomcat (Servlet Container) |
| Route handler | `app.get('/hello', ...)` | `HelloServlet.doGet()` |
| Middleware | `app.use(middleware)` | `Filter` |
| Request object | `req` | `HttpServletRequest` |
| Response object | `res` | `HttpServletResponse` |
| Session | `req.session` | `request.getSession()` |

---

### 2. Các file trong dự án

#### 📄 `pom.xml` - Maven Project Object Model
File cấu hình Maven, quản lý:
- **Dependencies** (thư viện): Servlet API, JSTL, Gson
- **Plugins**: WAR packaging, embedded Tomcat
- **Build settings**: Java version, encoding

```xml
<!-- Dependency quan trọng nhất -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
    <scope>provided</scope>  <!-- Tomcat đã có sẵn, không cần đóng gói -->
</dependency>
```

#### 📄 `web.xml` - Deployment Descriptor
File cấu hình cho Servlet Container (Tomcat), định nghĩa:
- **Servlet declarations**: Đăng ký servlet và URL mapping
- **Filter declarations**: Đăng ký filter
- **Session config**: Timeout session
- **Error pages**: Trang hiển thị khi có lỗi
- **Welcome file**: File mặc định khi truy cập root `/`

> **Lưu ý:** Từ Servlet 3.0+, có thể dùng `@WebServlet` annotation thay thế cho web.xml.

---

### 3. Các Servlet

#### 🟢 `HelloServlet.java` — GET Request cơ bản
**URL:** `GET /hello` hoặc `GET /hello?name=TenBan`

**Kiến thức:**
- Cách tạo một servlet đơn giản nhất
- Override `doGet()` để xử lý GET request
- `request.getParameter("name")` → đọc query parameter
- `response.getWriter()` → ghi HTML response
- `response.setContentType("text/html")` → set Content-Type header
- `response.setCharacterEncoding("UTF-8")` → hỗ trợ tiếng Việt
- Escape HTML để tránh XSS attack

```java
// Lấy parameter từ URL: /hello?name=Minh
String name = request.getParameter("name"); // → "Minh"
```

---

#### 🟡 `FormServlet.java` — Xử lý Form (GET & POST)
**URL:** `GET /form` (hiển thị form) | `POST /form` (xử lý form)

**Kiến thức:**
- **GET vs POST:**

| | GET | POST |
|---|-----|------|
| Data ở đâu? | URL query string | Request body |
| Hiển thị trên URL? | ✅ Có | ❌ Không |
| Giới hạn kích thước | ~2048 chars | Không giới hạn |
| Bookmark được? | ✅ Có | ❌ Không |
| An toàn cho password? | ❌ Không | ✅ Hơn GET |
| Dùng cho | Tìm kiếm, filter | Form đăng ký, login |

- `doGet()` → hiển thị form HTML
- `doPost()` → xử lý data khi submit
- `request.setCharacterEncoding("UTF-8")` → **PHẢI set TRƯỚC khi đọc parameter**
- `request.getParameterNames()` → lấy tất cả parameter names

---

#### 🔵 `SessionServlet.java` — HTTP Session
**URL:** `GET/POST /session`

**Kiến thức:**
- HTTP là **stateless** → Session giúp "nhớ" client giữa các request
- `request.getSession(true)` → tạo session mới nếu chưa có
- `session.setAttribute("key", value)` → lưu data vào session
- `session.getAttribute("key")` → đọc data từ session
- `session.invalidate()` → hủy session (logout)
- `session.getId()` → lấy Session ID (JSESSIONID cookie)
- **PRG Pattern (Post/Redirect/Get):** Sau POST, redirect sang GET để tránh duplicate submit

```java
// Lưu vào session
session.setAttribute("visitCount", 5);

// Đọc từ session  
Integer count = (Integer) session.getAttribute("visitCount"); // → 5

// Hủy session
session.invalidate();
```

**Session hoạt động như thế nào?**
```
Lần đầu:
Browser ──GET /session──→ Server
                          Server tạo Session (ID: abc123)
Browser ←── Set-Cookie: JSESSIONID=abc123 ──── Server

Lần sau:
Browser ──GET /session + Cookie: JSESSIONID=abc123──→ Server
                                                      Server tìm Session abc123
                                                      → Có data trước đó!
```

---

#### 🟣 `LifecycleServlet.java` — Vòng đời Servlet
**URL:** `GET /lifecycle`

**Kiến thức:**
- Vòng đời gồm 4 giai đoạn:

```
┌─────────────┐    ┌──────────┐    ┌───────────┐    ┌───────────┐
│ Constructor  │ →  │  init()  │ →  │ service() │ →  │ destroy() │
│  (1 lần)     │    │ (1 lần)  │    │(mỗi req)  │    │  (1 lần)  │
└─────────────┘    └──────────┘    └───────────┘    └───────────┘
```

1. **Constructor**: Tạo instance servlet (chỉ 1 lần)
2. **init(ServletConfig)**: Khởi tạo resources (chỉ 1 lần)
3. **service()**: Phân loại request → gọi `doGet()`/`doPost()` (mỗi request)
4. **destroy()**: Cleanup resources (khi server shutdown)

**QUAN TRỌNG:** Servlet là **Singleton** → chỉ có 1 instance cho TẤT CẢ request → phải cẩn thận với biến instance (thread safety)!

---

#### 🔴 `ApiServlet.java` — REST API
**URL:** `GET/POST/DELETE /api/todos`

**Kiến thức:**
- Trả về **JSON** thay vì HTML (`response.setContentType("application/json")`)
- Đọc **request body** bằng `request.getReader()`
- Dùng **Gson** để convert Java object ↔ JSON
- HTTP Status codes: `200 OK`, `201 Created`, `400 Bad Request`, `404 Not Found`
- `ConcurrentHashMap` + `AtomicInteger` cho thread safety
- CORS headers: `Access-Control-Allow-Origin: *`

**API Endpoints:**
| Method | URL | Body | Mô tả |
|--------|-----|------|-------|
| GET | `/api/todos` | - | Lấy tất cả todos |
| GET | `/api/todos?id=1` | - | Lấy 1 todo |
| POST | `/api/todos` | `{"title":"..."}` | Tạo todo mới |
| DELETE | `/api/todos?id=1` | - | Xóa todo |

---

#### 🟠 `LoggingFilter.java` — Servlet Filter
**URL Pattern:** `/*` (tất cả URL)

**Kiến thức:**
- Filter chạy **TRƯỚC** và **SAU** servlet (giống middleware trong Express.js)
- Implement interface `javax.servlet.Filter`
- 3 phương thức: `init()`, `doFilter()`, `destroy()`
- **BẮT BUỘC** gọi `chain.doFilter(request, response)` để chuyển tiếp request
- Nếu KHÔNG gọi `chain.doFilter()` → request bị chặn!

```
Request Flow:
Client → LoggingFilter (pre) → Servlet → LoggingFilter (post) → Client

Chuỗi nhiều Filter:
Client → Filter1 → Filter2 → Filter3 → Servlet → Filter3 → Filter2 → Filter1 → Client
```

**Ứng dụng thực tế của Filter:**
- 📝 Logging (ghi log)
- 🔐 Authentication (kiểm tra login)
- 🌐 CORS headers
- 🔤 Encoding (set UTF-8)
- 📦 Compression (gzip)
- ⏱️ Rate limiting

---

### 4. File cấu hình & Web resources

#### `webapp/index.html` — Trang chủ
- File HTML tĩnh, là **welcome-file** (hiển thị khi truy cập `/`)
- Chứa menu điều hướng đến các servlet

#### `webapp/css/style.css` — Stylesheet
- Dark theme hiện đại
- CSS Variables cho design tokens
- Responsive layout
- Animations

#### `webapp/error/404.html` & `500.html`
- Trang lỗi custom, được cấu hình trong `web.xml`
- Thay thế trang lỗi mặc định của Tomcat

---

## 🔑 Các khái niệm quan trọng

### Servlet Container (Tomcat)
- Quản lý vòng đời servlet
- Nhận HTTP request, tìm servlet phù hợp, gọi method tương ứng
- Multi-threading: mỗi request chạy trên 1 thread riêng

### URL Mapping
Có 2 cách đăng ký URL cho servlet:

**Cách 1: web.xml (truyền thống)**
```xml
<servlet>
    <servlet-name>HelloServlet</servlet-name>
    <servlet-class>com.learn.servlet.HelloServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>HelloServlet</servlet-name>
    <url-pattern>/hello</url-pattern>
</servlet-mapping>
```

**Cách 2: @WebServlet annotation (hiện đại, từ Servlet 3.0+)**
```java
@WebServlet("/hello")
public class HelloServlet extends HttpServlet { ... }
```

### Request/Response
```java
// REQUEST - Đọc data từ client
request.getParameter("name");       // Query param hoặc form param
request.getHeader("User-Agent");    // HTTP header
request.getMethod();                // GET, POST, PUT, DELETE
request.getRemoteAddr();            // Client IP
request.getSession();               // HTTP Session
request.getReader();                // Request body (cho JSON)

// RESPONSE - Gửi data về client
response.setContentType("text/html");    // Content type
response.setCharacterEncoding("UTF-8");  // Encoding
response.setStatus(200);                 // Status code
response.setHeader("key", "value");      // Custom header
response.getWriter();                    // Ghi response body
response.sendRedirect("/path");          // Redirect
```

---

## 📖 Tài liệu tham khảo

- [Oracle Java Servlet Tutorial](https://docs.oracle.com/javaee/7/tutorial/servlets.htm)
- [Servlet API JavaDoc](https://javaee.github.io/javaee-spec/javadocs/javax/servlet/package-summary.html)
- [Maven WAR Plugin](https://maven.apache.org/plugins/maven-war-plugin/)
- [Tomcat Documentation](https://tomcat.apache.org/tomcat-9.0-doc/)

---

## 🎯 Bài tập gợi ý

1. **Thêm LoginServlet**: Tạo form đăng nhập, kiểm tra username/password, lưu trạng thái login vào session
2. **Thêm AuthFilter**: Tạo filter kiểm tra session, nếu chưa login thì redirect về trang login
3. **Todo API nâng cao**: Thêm PUT method để update todo
4. **JSP**: Tạo file `.jsp` thay vì viết HTML trong Java code
5. **Forward vs Redirect**: Thử `request.getRequestDispatcher("/page").forward(request, response)` vs `response.sendRedirect("/page")`

---

*Happy coding! 🚀*
