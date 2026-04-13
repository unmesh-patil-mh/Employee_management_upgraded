package emp.servlet;

import emp.dao.EmployeeDAO;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for the Dashboard page.
 * URL mapping registered in web.xml: /DashboardServlet
 */
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() {
        employeeDAO = new EmployeeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        try {
            // Total employees
            int totalEmployees = employeeDAO.getTotalCount("", "");

            // Total distinct departments
            int totalDepts = employeeDAO.getDepartments().size();

            // Employees added today
            int addedToday = employeeDAO.getAddedTodayCount();

            req.setAttribute("totalEmployees", totalEmployees);
            req.setAttribute("totalDepts",     totalDepts);
            req.setAttribute("addedToday",     addedToday);

        } catch (SQLException e) {
            getServletContext().log("Error loading dashboard stats: " + e.getMessage(), e);
            // Graceful degradation — show zeros instead of crashing
            req.setAttribute("totalEmployees", 0);
            req.setAttribute("totalDepts",     0);
            req.setAttribute("addedToday",     0);
        }

        req.getRequestDispatcher("/dashboard.jsp").forward(req, res);
    }
}
