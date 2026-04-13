package emp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for obtaining a JDBC connection.
 * All DAOs use this single point of configuration.
 *
 * Database: empmanagement
 */
public class DBConnection {

    // ── Connection constants ────────────────────────────────────────────────
    private static final String DRIVER   = "com.mysql.cj.jdbc.Driver";
    private static final String URL      = "jdbc:mysql://localhost:3306/empmanagement?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER  = "root";
    private static final String DB_PASS  = "Unmesh";

    // Prevent instantiation
    private DBConnection() {}

    /**
     * Returns a new JDBC Connection.
     * Caller is responsible for closing it (use try-with-resources).
     *
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Add mysql-connector-java to WEB-INF/lib.", e);
        }
        return DriverManager.getConnection(URL, DB_USER, DB_PASS);
    }
}
