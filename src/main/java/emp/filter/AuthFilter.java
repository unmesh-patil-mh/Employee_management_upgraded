//package emp.filter;
//
//import javax.servlet.*;
//import javax.servlet.http.*;
//import java.io.IOException;
//
///**
// * Authentication Filter — guards all protected application pages.
// * URL mapping registered in web.xml: /*
// */
//public class AuthFilter implements Filter {
//
//    @Override
//    public void init(FilterConfig config) throws ServletException {}
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest  req = (HttpServletRequest)  request;
//        HttpServletResponse res = (HttpServletResponse) response;
//
//        String contextPath = req.getContextPath();
//        String requestURI  = req.getRequestURI();
//
//        // Strip context path to get the relative URI
//        String uri = requestURI.substring(contextPath.length());
//
//        // ── Public resources — always allow ──────────────────────────────────
//        if (isPublicResource(uri)) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        // ── Check for valid session ───────────────────────────────────────────
//        HttpSession session  = req.getSession(false);
//        boolean     loggedIn = (session != null && session.getAttribute("username") != null);
//
//        if (loggedIn) {
//            // Prevent browser from caching protected pages
//            res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//            res.setHeader("Pragma",         "no-cache");
//            res.setDateHeader("Expires",    0);
//            chain.doFilter(request, response);
//        } else {
//            // Not logged in — redirect to login page
//            res.sendRedirect(contextPath + "/login.jsp");
//        }
//    }
//
//    @Override
//    public void destroy() {}
//
//    // ── Helper: determine if a URI is publicly accessible ────────────────────
//
//    private boolean isPublicResource(String uri) {
//        return uri.equals("/login.jsp")
//            || uri.equals("/register.jsp")
//            || uri.equals("/LoginServlet")
//            || uri.equals("/RegisterServlet")
//            || uri.equals("/error.jsp")
//            || uri.startsWith("/css/")
//            || uri.startsWith("/js/")
//            || uri.startsWith("/images/")
//            || uri.startsWith("/WEB-INF/");
//    }
//}

package emp.filter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String contextPath = req.getContextPath();
        String requestURI  = req.getRequestURI();
        String uri = requestURI.substring(contextPath.length());

        // 🔍 DEBUG (remove later)
        System.out.println("AuthFilter URI: " + uri);

        // ✅ Allow public resources
        if (isPublicResource(uri)) {
            chain.doFilter(request, response);
            return;
        }

        // 🔐 Check session
        HttpSession session = req.getSession(false);

        boolean loggedIn = (session != null && session.getAttribute("user") != null);
        // 👆 IMPORTANT: use "user" (match your LoginServlet)

        if (loggedIn) {
            // Prevent caching
            res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            res.setHeader("Pragma", "no-cache");
            res.setDateHeader("Expires", 0);

            chain.doFilter(request, response);
        } else {
            // Redirect to login
            res.sendRedirect(contextPath + "/login.jsp");
        }
    }

    @Override
    public void destroy() {}

    private boolean isPublicResource(String uri) {
        return uri.equals("/login.jsp")
            || uri.equals("/register.jsp")
            || uri.equals("/LoginServlet")
            || uri.equals("/RegisterServlet")
            || uri.equals("/error.jsp")
            || uri.startsWith("/css/")
            || uri.startsWith("/js/")
            || uri.startsWith("/images/");
            // ❌ REMOVED: /WEB-INF/
    }
}