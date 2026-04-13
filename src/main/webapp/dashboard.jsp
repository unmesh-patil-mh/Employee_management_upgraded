<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard — Employee Management System</title>
    <meta name="description" content="Admin dashboard for the Employee Management System.">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%-- Navbar --%>
<jsp:include page="/navbar.jsp"/>

<div class="ems-page">

    <%-- Page header --%>
    <div class="page-header">
        <div>
            <h1>Dashboard</h1>
            <p>Welcome back, <strong>${sessionScope.username}</strong> &mdash; here&rsquo;s what&rsquo;s happening.</p>
        </div>
        <a href="${pageContext.request.contextPath}/EmployeeServlet?action=add"
           class="btn-primary-ems">
           &#43; Add Employee
        </a>
    </div>

    <%-- Stat cards (backed by DashboardServlet) --%>
    <div class="stat-grid">
        <div class="stat-card">
            <div class="icon icon-blue">&#128100;</div>
            <div>
                <div class="value">${totalEmployees}</div>
                <div class="label">Total Employees</div>
            </div>
        </div>
        <div class="stat-card">
            <div class="icon icon-purple">&#127970;</div>
            <div>
                <div class="value">${totalDepts}</div>
                <div class="label">Departments</div>
            </div>
        </div>
        <div class="stat-card">
            <div class="icon icon-green">&#9989;</div>
            <div>
                <div class="value">Active</div>
                <div class="label">System Status</div>
            </div>
        </div>
        <div class="stat-card">
            <div class="icon icon-orange">&#128197;</div>
            <div>
                <div class="value">${addedToday}</div>
                <div class="label">Added Today</div>
            </div>
        </div>
    </div>

    <%-- Quick actions --%>
    <div class="ems-card" style="margin-bottom:1.5rem;">
        <h2 style="font-size:1.1rem;font-weight:700;margin:0 0 1rem;">Quick Actions</h2>
        <div style="display:flex;gap:0.75rem;flex-wrap:wrap;">
            <a href="${pageContext.request.contextPath}/EmployeeServlet?action=list"
               class="btn-primary-ems">
               &#128203; View All Employees
            </a>
            <a href="${pageContext.request.contextPath}/EmployeeServlet?action=add"
               class="btn-secondary-ems">
               &#43; Add New Employee
            </a>
            <a href="${pageContext.request.contextPath}/LogoutServlet"
               class="btn-danger-ems" style="padding:0.55rem 1.2rem;">
               &#x23FB; Logout
            </a>
        </div>
    </div>

    <%-- Info / Tech-stack card --%>
    <div class="ems-card">
        <h2 style="font-size:1.1rem;font-weight:700;margin:0 0 0.75rem;">About This System</h2>
        <p style="color:#64748b;font-size:0.875rem;margin:0;line-height:1.7;">
            This <strong>Employee Management System</strong> is built using
            <strong>Java Servlets + JSP + JDBC</strong> following the MVC pattern.
            It supports adding, editing, deleting, searching, filtering by department,
            and paginating through employee records. All data is stored in a
            MySQL database.<br><br>
            Tech Stack: <code>Java EE &nbsp;&middot;&nbsp; MySQL &nbsp;&middot;&nbsp; JSTL/EL &nbsp;&middot;&nbsp; Bootstrap 5</code>
        </p>
    </div>

</div>

<footer class="ems-footer">
    &copy; 2024 Employee Management System &mdash; Built with Java Servlets &amp; JSP
</footer>

</body>
</html>
