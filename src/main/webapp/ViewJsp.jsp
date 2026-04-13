<%-- ViewJsp.jsp — DEPRECATED. Redirects to EmployeeServlet?action=list --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<% response.sendRedirect(request.getContextPath() + "/EmployeeServlet?action=list"); %>