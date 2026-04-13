package emp.dao;

import emp.model.Employee;
import emp.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Employee CRUD operations.
 * Uses PreparedStatement everywhere to prevent SQL injection.
 * Uses try-with-resources to ensure connections are always closed.
 */
public class EmployeeDAO {

    // ── Save (INSERT) ────────────────────────────────────────────────────────

    /**
     * Inserts a new employee record into the database.
     *
     * @param emp Employee object to save
     * @return number of rows affected (1 = success, 0 = failure)
     * @throws SQLException on DB error
     */
    public int save(Employee emp) throws SQLException {
        String sql = "INSERT INTO employees (name, email, phone, department, role, country) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, emp.getName());
            ps.setString(2, emp.getEmail());
            ps.setString(3, emp.getPhone());
            ps.setString(4, emp.getDepartment());
            ps.setString(5, emp.getRole());
            ps.setString(6, emp.getCountry());

            return ps.executeUpdate();
        }
    }

    // ── Update ───────────────────────────────────────────────────────────────

    /**
     * Updates an existing employee record.
     *
     * @param emp Employee object with updated values (must have valid id)
     * @return number of rows affected
     * @throws SQLException on DB error
     */
    public int update(Employee emp) throws SQLException {
        String sql = "UPDATE employees SET name=?, email=?, phone=?, department=?, role=?, country=? "
                   + "WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, emp.getName());
            ps.setString(2, emp.getEmail());
            ps.setString(3, emp.getPhone());
            ps.setString(4, emp.getDepartment());
            ps.setString(5, emp.getRole());
            ps.setString(6, emp.getCountry());
            ps.setInt(7, emp.getId());

            return ps.executeUpdate();
        }
    }

    // ── Delete ───────────────────────────────────────────────────────────────

    /**
     * Deletes an employee by ID.
     *
     * @param id Employee ID to delete
     * @return number of rows affected
     * @throws SQLException on DB error
     */
    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    // ── Get by ID ────────────────────────────────────────────────────────────

    /**
     * Retrieves a single employee by their ID.
     *
     * @param id Employee ID
     * @return Employee object, or null if not found
     * @throws SQLException on DB error
     */
    public Employee getById(int id) throws SQLException {
        String sql = "SELECT id, name, email, phone, department, role, country, created_at "
                   + "FROM employees WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    // ── Get All (paginated + search + filter) ────────────────────────────────

    /**
     * Returns a page of employees, optionally filtered by a search keyword
     * (matches name or email) and/or department.
     *
     * @param keyword    Search term for name/email (empty string = no filter)
     * @param department Department filter (empty string = all departments)
     * @param offset     Pagination offset (0-based)
     * @param pageSize   Number of records per page
     * @return List of matching Employee objects
     * @throws SQLException on DB error
     */
    public List<Employee> getAll(String keyword, String department,
                                 int offset, int pageSize) throws SQLException {

        StringBuilder sql = new StringBuilder(
            "SELECT id, name, email, phone, department, role, country, created_at "
          + "FROM employees WHERE 1=1 ");

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (name LIKE ? OR email LIKE ?) ");
        }
        if (department != null && !department.trim().isEmpty()) {
            sql.append("AND department = ? ");
        }
        sql.append("ORDER BY created_at DESC LIMIT ? OFFSET ?");

        List<Employee> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int idx = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                String like = "%" + keyword.trim() + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }
            if (department != null && !department.trim().isEmpty()) {
                ps.setString(idx++, department.trim());
            }
            ps.setInt(idx++, pageSize);
            ps.setInt(idx, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    // ── Count (for pagination) ────────────────────────────────────────────────

    /**
     * Returns the total number of employees matching the given filters.
     * Used to calculate the number of pages for pagination.
     *
     * @param keyword    Search keyword (empty = all)
     * @param department Department filter (empty = all)
     * @return total matching row count
     * @throws SQLException on DB error
     */
    public int getTotalCount(String keyword, String department) throws SQLException {

        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) FROM employees WHERE 1=1 ");

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (name LIKE ? OR email LIKE ?) ");
        }
        if (department != null && !department.trim().isEmpty()) {
            sql.append("AND department = ? ");
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int idx = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                String like = "%" + keyword.trim() + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }
            if (department != null && !department.trim().isEmpty()) {
                ps.setString(idx, department.trim());
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    // ── Get distinct departments ──────────────────────────────────────────────

    /**
     * Returns all distinct department names for the filter dropdown.
     *
     * @return List of department name strings
     * @throws SQLException on DB error
     */
    public List<String> getDepartments() throws SQLException {
        String sql = "SELECT DISTINCT department FROM employees "
                   + "WHERE department IS NOT NULL AND department != '' ORDER BY department";
        List<String> departments = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                departments.add(rs.getString("department"));
            }
        }
        return departments;
    }

    // ── Count employees added today ───────────────────────────────────────────

    /**
     * Returns the number of employees added on today's date.
     *
     * @return count of employees added today
     * @throws SQLException on DB error
     */
    public int getAddedTodayCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM employees WHERE DATE(created_at) = CURDATE()";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    // ── Helper: Map ResultSet row → Employee ─────────────────────────────────

    private Employee mapRow(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setId(rs.getInt("id"));
        emp.setName(rs.getString("name"));
        emp.setEmail(rs.getString("email"));
        emp.setPhone(rs.getString("phone"));
        emp.setDepartment(rs.getString("department"));
        emp.setRole(rs.getString("role"));
        emp.setCountry(rs.getString("country"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            emp.setCreatedAt(ts.toString().substring(0, 10)); // yyyy-MM-dd
        }
        return emp;
    }
}
