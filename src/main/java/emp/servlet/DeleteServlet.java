package emp.servlet;

import emp.dao.EmployeeDAO;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for deleting an employee.
 * URL mapping registered in web.xml: /DeleteServlet
 */
public class DeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() {
        employeeDAO = new EmployeeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        String idParam = req.getParameter("id");

        // Validate that id is a positive integer
        if (idParam == null || !idParam.matches("\\d+")) {
            res.sendRedirect(req.getContextPath()
                    + "/EmployeeServlet?action=list&error=Invalid+employee+ID");
            return;
        }

        int id = Integer.parseInt(idParam);

        try {
            int rows = employeeDAO.delete(id);
            if (rows > 0) {
                res.sendRedirect(req.getContextPath()
                        + "/EmployeeServlet?action=list&success=Employee+deleted+successfully");
            } else {
                res.sendRedirect(req.getContextPath()
                        + "/EmployeeServlet?action=list&error=Employee+not+found");
            }
        } catch (SQLException e) {
            getServletContext().log("Error deleting employee id=" + id + ": " + e.getMessage(), e);
            res.sendRedirect(req.getContextPath()
                    + "/EmployeeServlet?action=list&error=Server+error+while+deleting");
        }
    }
}
