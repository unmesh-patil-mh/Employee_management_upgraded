package emp.dao;

import emp.model.User;
import emp.util.DBConnection;

import java.sql.*;

/**
 * Data Access Object for User authentication operations.
 * Handles registration and login validation.
 */
public class UserDAO {

    // ── Save (Register) ──────────────────────────────────────────────────────

    /**
     * Registers a new user.
     *
     * @param user User object with username and password
     * @return  1 if registration successful
     *         -1 if username already exists
     *          0 on failure
     * @throws SQLException on DB error
     */
    public int save(User user) throws SQLException {
        // Check if username already exists
        String checkSql = "SELECT id FROM users WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement checkPs = con.prepareStatement(checkSql)) {

            checkPs.setString(1, user.getUsername());
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next()) {
                    return -1; // Username already taken
                }
            }

            // Insert new user
            String insertSql = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement insertPs = con.prepareStatement(insertSql)) {
                insertPs.setString(1, user.getUsername());
                insertPs.setString(2, user.getPassword());
                return insertPs.executeUpdate();
            }
        }
    }

    // ── Validate (Login) ─────────────────────────────────────────────────────

    /**
     * Validates a user's login credentials.
     *
     * @param username User's email / username
     * @param password Plain-text password
     * @return true if credentials are valid, false otherwise
     * @throws SQLException on DB error
     */
    public boolean validate(String username, String password) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
