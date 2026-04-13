package emp.servlet;

import emp.dao.UserDAO;
import emp.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for new user registration.
 * URL mapping registered in web.xml: POST /RegisterServlet
 */
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        res.sendRedirect(req.getContextPath() + "/register.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirm  = req.getParameter("confirmPassword");

        // ── Server-side validation ──────────────────────────────────────────
        StringBuilder errorMsg = new StringBuilder();

        if (username == null || username.trim().isEmpty()) {
            errorMsg.append("Email is required. ");
        } else if (!username.trim().matches("^[\\w.%+\\-]+@[\\w.\\-]+\\.[A-Za-z]{2,6}$")) {
            errorMsg.append("Please enter a valid email address. ");
        }
        if (password == null || password.trim().isEmpty()) {
            errorMsg.append("Password is required. ");
        } else if (password.length() < 6) {
            errorMsg.append("Password must be at least 6 characters. ");
        }
        if (confirm == null || !confirm.equals(password)) {
            errorMsg.append("Passwords do not match. ");
        }

        if (errorMsg.length() > 0) {
            req.setAttribute("error", errorMsg.toString().trim());
            req.setAttribute("username", username);
            req.getRequestDispatcher("/register.jsp").forward(req, res);
            return;
        }

        try {
            User user = new User(username.trim(), password);
            int status = userDAO.save(user);

            if (status == 1) {
                req.setAttribute("success", "Registration successful! Please log in.");
                req.getRequestDispatcher("/login.jsp").forward(req, res);
            } else if (status == -1) {
                req.setAttribute("error", "This email is already registered. Please log in.");
                req.setAttribute("username", username);
                req.getRequestDispatcher("/register.jsp").forward(req, res);
            } else {
                req.setAttribute("error", "Registration failed. Please try again.");
                req.getRequestDispatcher("/register.jsp").forward(req, res);
            }
        } catch (SQLException e) {
            getServletContext().log("Registration error: " + e.getMessage(), e);
            req.setAttribute("error", "A server error occurred. Please try again later.");
            req.getRequestDispatcher("/register.jsp").forward(req, res);
        }
    }
}
