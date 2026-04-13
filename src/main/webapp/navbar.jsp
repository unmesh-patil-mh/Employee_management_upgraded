<%-- navbar.jsp : Reusable navigation bar — include via <jsp:include page="/navbar.jsp"/> --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<nav class="ems-navbar">
    <a href="${pageContext.request.contextPath}/DashboardServlet" class="brand">
        &#128188;&nbsp;EMS
        <span class="badge-dot"></span>
    </a>

    <ul class="nav-links">
        <li><a href="${pageContext.request.contextPath}/DashboardServlet">Dashboard</a></li>
        <li><a href="${pageContext.request.contextPath}/EmployeeServlet?action=list">Employees</a></li>
        <li><a href="${pageContext.request.contextPath}/EmployeeServlet?action=add">+ Add Employee</a></li>
    </ul>

    <div class="user-badge">
        <div class="avatar">
            <%-- fn:toUpperCase + fn:substring is safe EL --%>
            <c:set var="uname" value="${sessionScope.username}"/>
            <c:choose>
                <c:when test="${not empty uname}">${fn:toUpperCase(fn:substring(uname, 0, 1))}</c:when>
                <c:otherwise>A</c:otherwise>
            </c:choose>
        </div>
        <span>${sessionScope.username}</span>
        <a href="${pageContext.request.contextPath}/LogoutServlet"
           style="color:rgba(255,255,255,0.6);font-size:0.8rem;text-decoration:none;margin-left:0.4rem;"
           title="Logout">&#x23FB; Logout</a>
    </div>
</nav>
