<%-- EditJsp.jsp — DEPRECATED. Redirects to employee list --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<% response.sendRedirect(request.getContextPath() + "/EmployeeServlet?action=list"); %>