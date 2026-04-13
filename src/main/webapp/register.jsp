<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account — Employee Management System</title>
    <meta name="description" content="Register for the Employee Management System admin panel.">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">

<div class="auth-card">
    <div class="auth-logo">
        <h1>&#128188; EMS</h1>
        <p>Employee Management System</p>
    </div>

    <h2>Create an account</h2>
    <p style="text-align:center;color:#64748b;margin-bottom:1.5rem;font-size:0.875rem;">
        Sign up to start managing your workforce
    </p>

    <c:if test="${not empty error}">
        <div class="alert-ems alert-error" role="alert">
            &#9888;&nbsp;${error}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/RegisterServlet" method="post"
          class="ems-form" id="registerForm">

        <div class="form-group">
            <label for="username">Email Address <span class="req">*</span></label>
            <input type="email" id="username" name="username"
                   placeholder="you@email.com"
                   value="<c:out value='${username}'/>"
                   required autofocus>
        </div>

        <div class="form-group">
            <label for="password">Password <span class="req">*</span></label>
            <input type="password" id="password" name="password"
                   placeholder="Create a password"
                   required minlength="6">
            <small style="color:#94a3b8;font-size:0.75rem;">Minimum 6 characters</small>
        </div>

        <div class="form-group">
            <label for="confirmPassword">Confirm Password <span class="req">*</span></label>
            <input type="password" id="confirmPassword" name="confirmPassword"
                   placeholder="Repeat your password"
                   required>
        </div>

        <button type="submit" class="btn-primary-ems" style="width:100%;justify-content:center;padding:0.7rem;margin-top:0.5rem;">
            Register &rarr;
        </button>
    </form>

    <p style="text-align:center;margin-top:1.25rem;font-size:0.875rem;color:#64748b;">
        Already have an account?
        <a href="${pageContext.request.contextPath}/login.jsp"
           style="color:#4f46e5;font-weight:600;text-decoration:none;">Log in instead</a>
    </p>
</div>

</body>
</html>