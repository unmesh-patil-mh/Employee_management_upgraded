<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
         isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error — Employee Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/navbar.jsp"/>

<div class="ems-page" style="display:flex;align-items:center;justify-content:center;min-height:70vh;">
    <div class="ems-card" style="max-width:500px;width:100%;text-align:center;padding:3rem 2rem;">

        <div style="font-size:4rem;margin-bottom:1rem;">
            <c:choose>
                <c:when test="${errorCode == '404'}">&#128269;</c:when>
                <c:otherwise>&#9888;</c:otherwise>
            </c:choose>
        </div>

        <h1 style="font-size:1.75rem;font-weight:800;color:#1e293b;margin-bottom:0.5rem;">
            <c:choose>
                <c:when test="${errorCode == '404'}">Page Not Found</c:when>
                <c:when test="${errorCode == '500'}">Server Error</c:when>
                <c:otherwise>Something Went Wrong</c:otherwise>
            </c:choose>
        </h1>

        <p style="color:#64748b;font-size:0.95rem;margin-bottom:1.75rem;">
            <c:choose>
                <c:when test="${not empty errorMsg}">${errorMsg}</c:when>
                <c:otherwise>An unexpected error occurred. Please try again.</c:otherwise>
            </c:choose>
        </p>

        <div style="display:flex;gap:0.75rem;justify-content:center;flex-wrap:wrap;">
            <a href="${pageContext.request.contextPath}/DashboardServlet"
               class="btn-primary-ems">&#127968; Go to Dashboard</a>
            <a href="javascript:history.back()"
               class="btn-secondary-ems">&#8592; Go Back</a>
        </div>

    </div>
</div>

<footer class="ems-footer">
    &copy; 2024 Employee Management System &mdash; Built with Java Servlets &amp; JSP
</footer>

</body>
</html>
