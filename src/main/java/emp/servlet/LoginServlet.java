package emp.servlet;

import emp.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for admin login and logout.
 * URL mappings registered in web.xml:
 *   POST /LoginServlet  → Authenticate user; redirect to DashboardServlet or back with error
 *   GET  /LogoutServlet → Invalidate session, redirect to login.jsp
 */
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    // ── GET: Redirect to login page (or handle logout) ───────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        String path = req.getServletPath();

        if ("/LogoutServlet".equals(path)) {
            // Invalidate session and redirect to login
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            res.sendRedirect(req.getContextPath() + "/login.jsp");
        } else {
            res.sendRedirect(req.getContextPath() + "/login.jsp");
        }
    }

    // ── POST: Authenticate user ──────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // ── Server-side validation ──────────────────────────────────────────
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "Username and password are required.");
            req.getRequestDispatcher("/login.jsp").forward(req, res);
            return;
        }

        try {
            boolean valid = userDAO.validate(username.trim(), password);
            if (valid) {
                // Create session, store username
                HttpSession session = req.getSession(true);
                session.setAttribute("user", username.trim());
                session.setMaxInactiveInterval(30 * 60); // 30 minutes

                res.sendRedirect(req.getContextPath() + "/DashboardServlet");
            } else {
                req.setAttribute("error", "Invalid username or password. Please try again.");
                req.getRequestDispatcher("/login.jsp").forward(req, res);
            }
        } catch (SQLException e) {
            // Log the actual exception (server-side) and show generic error to user
            getServletContext().log("Login error: " + e.getMessage(), e);
            req.setAttribute("error", "A server error occurred. Please try again later.");
            req.getRequestDispatcher("/login.jsp").forward(req, res);
        }
    }
}
