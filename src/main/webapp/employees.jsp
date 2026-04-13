<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employees — Employee Management System</title>
    <meta name="description" content="View, search, and manage all employees.">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/navbar.jsp"/>

<div class="ems-page">

    <%-- Page header --%>
    <div class="page-header">
        <div>
            <h1>Employees</h1>
            <p>
                <c:choose>
                    <c:when test="${totalCount == 0}">No employees found.</c:when>
                    <c:otherwise>Showing ${totalCount} employee(s).</c:otherwise>
                </c:choose>
            </p>
        </div>
        <a href="${pageContext.request.contextPath}/EmployeeServlet?action=add"
           class="btn-primary-ems">&#43; Add Employee</a>
    </div>

    <%-- Flash messages (success / error from redirect params) --%>
    <c:if test="${not empty param.success}">
        <div class="alert-ems alert-success">&#10003;&nbsp;${param.success}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert-ems alert-error">&#9888;&nbsp;${param.error}</div>
    </c:if>

    <%-- Search & Filter bar --%>
    <form method="get" action="${pageContext.request.contextPath}/EmployeeServlet" class="search-bar" id="searchForm">
        <input type="hidden" name="action" value="list">

        <input type="text"
               name="keyword"
               id="keyword"
               placeholder="&#128269; Search by name or email..."
               value="<c:out value='${keyword}'/>"
               style="flex:2;">

        <select name="department" id="department">
            <option value="">All Departments</option>
            <c:forEach var="dept" items="${departments}">
                <option value="${dept}"
                    <c:if test="${dept == department}">selected</c:if>>
                    ${dept}
                </option>
            </c:forEach>
        </select>

        <button type="submit" class="btn-primary-ems">Filter</button>

        <c:if test="${not empty keyword or not empty department}">
            <a href="${pageContext.request.contextPath}/EmployeeServlet?action=list"
               class="btn-secondary-ems">Clear</a>
        </c:if>
    </form>

    <%-- Employee Table --%>
    <div class="ems-card" style="padding:0;">
        <div class="ems-table-wrapper">
            <table class="ems-table" id="employeeTable">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Department</th>
                        <th>Role</th>
                        <th>Country</th>
                        <th>Joined</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty employees}">
                            <tr class="no-data">
                                <td colspan="9">
                                    &#128566; No employees found.
                                    <a href="${pageContext.request.contextPath}/EmployeeServlet?action=add">Add one now</a>.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="emp" items="${employees}" varStatus="loop">
                                <tr>
                                    <td style="color:#94a3b8;font-size:0.8rem;">${(currentPage - 1) * pageSize + loop.count}</td>
                                    <td><strong>${emp.name}</strong></td>
                                    <td style="color:#4f46e5;">${emp.email}</td>
                                    <td>${not empty emp.phone ? emp.phone : '—'}</td>
                                    <td>
                                        <c:if test="${not empty emp.department}">
                                            <span class="badge-dept">${emp.department}</span>
                                        </c:if>
                                        <c:if test="${empty emp.department}">—</c:if>
                                    </td>
                                    <td>
                                        <c:if test="${not empty emp.role}">
                                            <span class="badge-role">${emp.role}</span>
                                        </c:if>
                                        <c:if test="${empty emp.role}">—</c:if>
                                    </td>
                                    <td>${emp.country}</td>
                                    <td style="color:#94a3b8;font-size:0.8rem;">${emp.createdAt}</td>
                                    <td>
                                        <div class="action-btns">
                                            <a href="${pageContext.request.contextPath}/EmployeeServlet?action=edit&id=${emp.id}"
                                               class="btn-edit-ems">&#9998; Edit</a>
                                            <a href="${pageContext.request.contextPath}/DeleteServlet?id=${emp.id}"
                                               class="btn-danger-ems"
                                               onclick="return confirmDelete('${emp.name}')">
                                               &#128465; Delete
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>

        <%-- Pagination --%>
        <c:if test="${totalPages > 1}">
        <div class="pagination-bar" style="padding:1rem 1.25rem;">
            <div class="page-info">
                Page ${currentPage} of ${totalPages} &mdash; ${totalCount} records
            </div>
            <div class="pagination-links">
                <%-- Previous --%>
                <c:choose>
                    <c:when test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/EmployeeServlet?action=list&page=${currentPage-1}&keyword=<c:out value='${keyword}'/>&department=<c:out value='${department}'/>">&#8592;</a>
                    </c:when>
                    <c:otherwise><span style="opacity:0.3;">&#8592;</span></c:otherwise>
                </c:choose>

                <%-- Page numbers --%>
                <c:forEach begin="1" end="${totalPages}" var="p">
                    <c:choose>
                        <c:when test="${p == currentPage}">
                            <span class="active">${p}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/EmployeeServlet?action=list&page=${p}&keyword=<c:out value='${keyword}'/>&department=<c:out value='${department}'/>">${p}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <%-- Next --%>
                <c:choose>
                    <c:when test="${currentPage < totalPages}">
                        <a href="${pageContext.request.contextPath}/EmployeeServlet?action=list&page=${currentPage+1}&keyword=<c:out value='${keyword}'/>&department=<c:out value='${department}'/>">&#8594;</a>
                    </c:when>
                    <c:otherwise><span style="opacity:0.3;">&#8594;</span></c:otherwise>
                </c:choose>
            </div>
        </div>
        </c:if>
    </div>

</div>

<footer class="ems-footer">
    &copy; 2024 Employee Management System &mdash; Built with Java Servlets &amp; JSP
</footer>

<script>
/**
 * Confirm before deleting an employee.
 * Returns false to cancel the navigation if user clicks Cancel.
 */
function confirmDelete(name) {
    return window.confirm('Are you sure you want to delete employee "' + name + '"?\nThis action cannot be undone.');
}

/* Auto-submit filter form when department dropdown changes */
document.getElementById('department').addEventListener('change', function() {
    document.getElementById('searchForm').submit();
});
</script>

</body>
</html>
