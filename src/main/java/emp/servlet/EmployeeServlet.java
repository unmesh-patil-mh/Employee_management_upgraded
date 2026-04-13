package emp.servlet;

import emp.dao.EmployeeDAO;
import emp.model.Employee;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Central controller for all Employee CRUD operations.
 * URL mapping registered in web.xml: /EmployeeServlet
 *
 * GET  /EmployeeServlet?action=list            → Paginated employee list
 * GET  /EmployeeServlet?action=edit&id=N       → Load edit form for employee N
 * GET  /EmployeeServlet?action=add             → Load add-employee form
 * POST /EmployeeServlet?action=save            → Insert new employee
 * POST /EmployeeServlet?action=update          → Update existing employee
 */
public class EmployeeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int  PAGE_SIZE = 5; // employees per page

    private EmployeeDAO employeeDAO;

    @Override
    public void init() {
        employeeDAO = new EmployeeDAO();
    }

    // ── GET Dispatcher ───────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":  handleList(req, res); break;
            case "edit":  handleEditForm(req, res); break;
            case "add":   req.getRequestDispatcher("/add-employee.jsp").forward(req, res); break;
            default:      res.sendRedirect(req.getContextPath() + "/EmployeeServlet?action=list");
        }
    }

    // ── POST Dispatcher ──────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "save":   handleSave(req, res); break;
            case "update": handleUpdate(req, res); break;
            default:       res.sendRedirect(req.getContextPath() + "/EmployeeServlet?action=list");
        }
    }

    // ── List employees (with search, filter, pagination) ─────────────────────

    private void handleList(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        try {
            // Pagination
            int page = 1;
            String pageParam = req.getParameter("page");
            if (pageParam != null && pageParam.matches("\\d+")) {
                page = Integer.parseInt(pageParam);
            }
            int offset = (page - 1) * PAGE_SIZE;

            // Search & filter params
            String keyword    = req.getParameter("keyword");
            String department = req.getParameter("department");
            if (keyword    == null) keyword    = "";
            if (department == null) department = "";

            // Fetch data
            List<Employee>  employees   = employeeDAO.getAll(keyword, department, offset, PAGE_SIZE);
            int             totalCount  = employeeDAO.getTotalCount(keyword, department);
            int             totalPages  = (int) Math.ceil((double) totalCount / PAGE_SIZE);
            List<String>    departments = employeeDAO.getDepartments();

            // Set attributes for the view
            req.setAttribute("employees",   employees);
            req.setAttribute("totalCount",  totalCount);
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages",  totalPages);
            req.setAttribute("keyword",     keyword);
            req.setAttribute("department",  department);
            req.setAttribute("departments", departments);
            req.setAttribute("pageSize",    PAGE_SIZE);

            req.getRequestDispatcher("/employees.jsp").forward(req, res);

        } catch (SQLException e) {
            getServletContext().log("Error loading employee list: " + e.getMessage(), e);
            req.setAttribute("errorCode", "500");
            req.setAttribute("errorMsg",  "Could not load employee data.");
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }

    // ── Load edit form ────────────────────────────────────────────────────────

    private void handleEditForm(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        String idParam = req.getParameter("id");
        if (idParam == null || !idParam.matches("\\d+")) {
            res.sendRedirect(req.getContextPath() + "/EmployeeServlet?action=list");
            return;
        }

        try {
            int      id  = Integer.parseInt(idParam);
            Employee emp = employeeDAO.getById(id);
            if (emp == null) {
                req.setAttribute("errorCode", "404");
                req.setAttribute("errorMsg",  "Employee not found.");
                req.getRequestDispatcher("/error.jsp").forward(req, res);
                return;
            }
            req.setAttribute("employee", emp);
            req.getRequestDispatcher("/edit-employee.jsp").forward(req, res);

        } catch (SQLException e) {
            getServletContext().log("Error loading employee for edit: " + e.getMessage(), e);
            req.setAttribute("errorCode", "500");
            req.setAttribute("errorMsg",  "Could not load employee.");
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }

    // ── Save new employee ────────────────────────────────────────────────────

    private void handleSave(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        Employee emp = buildFromRequest(req);
        String validationError = validate(emp, false);

        if (validationError != null) {
            req.setAttribute("error",    validationError);
            req.setAttribute("employee", emp);
            req.getRequestDispatcher("/add-employee.jsp").forward(req, res);
            return;
        }

        try {
            int rows = employeeDAO.save(emp);
            if (rows > 0) {
                res.sendRedirect(req.getContextPath()
                        + "/EmployeeServlet?action=list&success=Employee+added+successfully");
            } else {
                req.setAttribute("error", "Failed to save employee. Please try again.");
                req.setAttribute("employee", emp);
                req.getRequestDispatcher("/add-employee.jsp").forward(req, res);
            }
        } catch (SQLException e) {
            getServletContext().log("Error saving employee: " + e.getMessage(), e);
            String msg = e.getMessage() != null && e.getMessage().contains("Duplicate")
                    ? "An employee with this email already exists."
                    : "A server error occurred. Please try again.";
            req.setAttribute("error", msg);
            req.setAttribute("employee", emp);
            req.getRequestDispatcher("/add-employee.jsp").forward(req, res);
        }
    }

    // ── Update existing employee ──────────────────────────────────────────────

    private void handleUpdate(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        String idParam = req.getParameter("id");
        if (idParam == null || !idParam.matches("\\d+")) {
            res.sendRedirect(req.getContextPath() + "/EmployeeServlet?action=list");
            return;
        }

        Employee emp = buildFromRequest(req);
        emp.setId(Integer.parseInt(idParam));
        String validationError = validate(emp, true);

        if (validationError != null) {
            req.setAttribute("error",    validationError);
            req.setAttribute("employee", emp);
            req.getRequestDispatcher("/edit-employee.jsp").forward(req, res);
            return;
        }

        try {
            int rows = employeeDAO.update(emp);
            if (rows > 0) {
                res.sendRedirect(req.getContextPath()
                        + "/EmployeeServlet?action=list&success=Employee+updated+successfully");
            } else {
                req.setAttribute("error", "Update failed. Employee may not exist.");
                req.setAttribute("employee", emp);
                req.getRequestDispatcher("/edit-employee.jsp").forward(req, res);
            }
        } catch (SQLException e) {
            getServletContext().log("Error updating employee: " + e.getMessage(), e);
            String msg = e.getMessage() != null && e.getMessage().contains("Duplicate")
                    ? "Another employee already has this email."
                    : "A server error occurred. Please try again.";
            req.setAttribute("error", msg);
            req.setAttribute("employee", emp);
            req.getRequestDispatcher("/edit-employee.jsp").forward(req, res);
        }
    }

    // ── Build Employee from request parameters ────────────────────────────────

    private Employee buildFromRequest(HttpServletRequest req) {
        Employee emp = new Employee();
        emp.setName(       nullSafe(req.getParameter("name")));
        emp.setEmail(      nullSafe(req.getParameter("email")));
        emp.setPhone(      nullSafe(req.getParameter("phone")));
        emp.setDepartment( nullSafe(req.getParameter("department")));
        emp.setRole(       nullSafe(req.getParameter("role")));
        emp.setCountry(    nullSafe(req.getParameter("country")));
        return emp;
    }

    // ── Server-side validation ────────────────────────────────────────────────

    /**
     * Validates an employee object.
     *
     * @param emp     Employee to validate
     * @param isUpdate true if this is an update (relaxes unique-email check)
     * @return error message string, or null if valid
     */
    private String validate(Employee emp, boolean isUpdate) {
        if (emp.getName() == null || emp.getName().isEmpty()) {
            return "Name is required.";
        }
        if (emp.getName().length() < 2 || emp.getName().length() > 100) {
            return "Name must be between 2 and 100 characters.";
        }
        if (emp.getEmail() == null || emp.getEmail().isEmpty()) {
            return "Email is required.";
        }
        if (!emp.getEmail().matches("^[\\w.%+\\-]+@[\\w.\\-]+\\.[A-Za-z]{2,6}$")) {
            return "Please enter a valid email address.";
        }
        if (emp.getPhone() != null && !emp.getPhone().isEmpty()
                && !emp.getPhone().matches("^[+\\d\\s\\-()]{7,15}$")) {
            return "Please enter a valid phone number.";
        }
        if (emp.getDepartment() == null || emp.getDepartment().isEmpty()) {
            return "Department is required.";
        }
        if (emp.getRole() == null || emp.getRole().isEmpty()) {
            return "Role / designation is required.";
        }
        if (emp.getCountry() == null || emp.getCountry().isEmpty()) {
            return "Country is required.";
        }
        return null; // No errors
    }

    // ── Null-safe trim ────────────────────────────────────────────────────────

    private String nullSafe(String value) {
        return value == null ? "" : value.trim();
    }
}
