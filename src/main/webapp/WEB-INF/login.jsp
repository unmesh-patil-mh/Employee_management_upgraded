<%-- 
    WEB-INF/login.jsp — DEPRECATED original login page.
    This file is inside WEB-INF and is NOT directly accessible by browsers.
    The real login page is at: /login.jsp (webapp root)
    This stub exists only to prevent a 404 during any internal forward that
    might still reference it.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<% response.sendRedirect(request.getContextPath() + "/login.jsp"); %>