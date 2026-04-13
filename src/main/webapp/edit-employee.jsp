<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Employee — Employee Management System</title>
    <meta name="description" content="Update an existing employee record.">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/navbar.jsp"/>

<div class="ems-page">

    <div class="page-header">
        <div>
            <h1>Edit Employee</h1>
            <p>Update the details for <strong>${employee.name}</strong>.</p>
        </div>
        <a href="${pageContext.request.contextPath}/EmployeeServlet?action=list"
           class="btn-secondary-ems">&#8592; Back to List</a>
    </div>

    <%-- Validation error --%>
    <c:if test="${not empty error}">
        <div class="alert-ems alert-error">&#9888;&nbsp;${error}</div>
    </c:if>

    <div class="ems-card">
        <form action="${pageContext.request.contextPath}/EmployeeServlet?action=update"
              method="post" class="ems-form" id="editForm" novalidate>

            <%-- Hidden employee ID --%>
            <input type="hidden" name="id" value="${employee.id}">

            <div class="form-row">
                <div class="form-group">
                    <label for="name">Full Name <span class="req">*</span></label>
                    <input type="text" id="name" name="name"
                           value="<c:out value='${employee.name}'/>"
                           required maxlength="100">
                </div>
                <div class="form-group">
                    <label for="email">Email Address <span class="req">*</span></label>
                    <input type="email" id="email" name="email"
                           value="<c:out value='${employee.email}'/>"
                           required>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="phone">Phone Number</label>
                    <input type="tel" id="phone" name="phone"
                           value="<c:out value='${employee.phone}'/>"
                           pattern="[+\d\s\-()]{7,15}">
                </div>
                <div class="form-group">
                    <label for="country">Country <span class="req">*</span></label>
                    <select id="country" name="country" required>
                        <option value="">-- Select Country --</option>
                        <c:set var="countries" value="India,USA,UK,Australia,Canada,Germany,France,Singapore,UAE,Other"/>
                        <c:forTokens items="${countries}" delims="," var="c">
                            <option value="${c}"
                                <c:if test="${c == employee.country}">selected</c:if>>${c}</option>
                        </c:forTokens>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="department">Department <span class="req">*</span></label>
                    <select id="department" name="department" required>
                        <option value="">-- Select Department --</option>
                        <c:set var="depts" value="Engineering,Human Resources,Finance,Marketing,Operations,Sales,IT Support,Design,Legal,Management"/>
                        <c:forTokens items="${depts}" delims="," var="d">
                            <option value="${d}"
                                <c:if test="${d == employee.department}">selected</c:if>>${d}</option>
                        </c:forTokens>
                    </select>
                </div>
                <div class="form-group">
                    <label for="role">Role / Designation <span class="req">*</span></label>
                    <input type="text" id="role" name="role"
                           value="<c:out value='${employee.role}'/>"
                           required maxlength="100">
                </div>
            </div>

            <div style="display:flex;gap:0.75rem;margin-top:0.5rem;">
                <button type="submit" class="btn-primary-ems">
                    &#10004; Update Employee
                </button>
                <a href="${pageContext.request.contextPath}/EmployeeServlet?action=list"
                   class="btn-secondary-ems">Cancel</a>
            </div>

        </form>
    </div>
</div>

<footer class="ems-footer">
    &copy; 2024 Employee Management System &mdash; Built with Java Servlets &amp; JSP
</footer>

</body>
</html>
