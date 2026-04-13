<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login — Employee Management System</title>
    <meta name="description" content="Sign in to the Employee Management System admin panel.">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">

<div class="auth-card">
    <!-- Logo / Branding -->
    <div class="auth-logo">
        <h1>&#128188; EMS</h1>
        <p>Employee Management System</p>
    </div>

    <h2>Welcome back</h2>

    <!-- Error / Success Alerts -->
    <c:if test="${not empty error}">
        <div class="alert-ems alert-error" role="alert">
            &#9888;&nbsp;${error}
        </div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="alert-ems alert-success" role="alert">
            &#10003;&nbsp;${success}
        </div>
    </c:if>

    <!-- Login Form -->
    <form action="${pageContext.request.contextPath}/LoginServlet" method="post"
          class="ems-form" id="loginForm" novalidate>

        <div class="form-group">
            <label for="username">Email Address <span class="req">*</span></label>
            <input type="email" id="username" name="username"
                   placeholder="admin@example.com"
                   value="<c:out value='${param.username}'/>"
                   required autofocus>
        </div>

        <div class="form-group">
            <label for="password">Password <span class="req">*</span></label>
            <input type="password" id="password" name="password"
                   placeholder="Enter your password"
                   required>
        </div>

        <button type="submit" class="btn-primary-ems" style="width:100%;justify-content:center;padding:0.7rem;">
            Sign In &rarr;
        </button>
    </form>

    <p style="text-align:center;margin-top:1.25rem;font-size:0.875rem;color:#64748b;">
        New user?
        <a href="${pageContext.request.contextPath}/register.jsp"
           style="color:#4f46e5;font-weight:600;text-decoration:none;">Create an account</a>
    </p>
</div>

</body>
</html>