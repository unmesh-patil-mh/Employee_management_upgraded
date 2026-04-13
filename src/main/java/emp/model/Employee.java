package emp.model;

/**
 * Model class representing an Employee.
 * Acts as a Java Bean (POJO) — only data, no logic.
 */
public class Employee {

    private int    id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private String role;
    private String country;
    private String createdAt;

    // ── Constructors ────────────────────────────────────────────────────────

    public Employee() {}

    public Employee(String name, String email, String phone,
                    String department, String role, String country) {
        this.name       = name;
        this.email      = email;
        this.phone      = phone;
        this.department = department;
        this.role       = role;
        this.country    = country;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────

    public int getId()                    { return id; }
    public void setId(int id)             { this.id = id; }

    public String getName()               { return name; }
    public void setName(String name)      { this.name = name; }

    public String getEmail()              { return email; }
    public void setEmail(String email)    { this.email = email; }

    public String getPhone()              { return phone; }
    public void setPhone(String phone)    { this.phone = phone; }

    public String getDepartment()                   { return department; }
    public void setDepartment(String department)    { this.department = department; }

    public String getRole()               { return role; }
    public void setRole(String role)      { this.role = role; }

    public String getCountry()                { return country; }
    public void setCountry(String country)    { this.country = country; }

    public String getCreatedAt()              { return createdAt; }
    public void setCreatedAt(String createdAt){ this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "', email='" + email
                + "', department='" + department + "', role='" + role
                + "', country='" + country + "'}";
    }
}
