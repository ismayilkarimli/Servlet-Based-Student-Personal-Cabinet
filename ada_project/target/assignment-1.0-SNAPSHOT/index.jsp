<%--
  Created by IntelliJ IDEA.
  User: ismayil
  Date: 2/17/21
  Time: 01:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*, java.util.*" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Dashboard</title>
</head>
<body>
<c:choose>
    <c:when test="${sessionScope.is_authorized != null}">
        <c:redirect url="/dashboard.jsp"/>
    </c:when>
    <c:when test="${sessionScope.is_authorized == null}">
        <c:import url="auth.jsp"/>
    </c:when>
</c:choose>
</body>
</html>
