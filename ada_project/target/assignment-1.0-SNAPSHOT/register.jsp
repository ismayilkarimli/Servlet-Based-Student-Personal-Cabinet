<%--
  Created by IntelliJ IDEA.
  User: ismayil
  Date: 3/11/21
  Time: 20:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*, java.util.*" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Register</title>
</head>
<body>
<h1>Registration page</h1>
<c:if test="${requestScope.error_message != null}">
    <div class="error_message">
        <c:forEach items="${requestScope.error_message}" var="err">
            <b style="color: red;"><c:out value="${err}"/></b>
        </c:forEach>
    </div>
</c:if>
<form action="register" method="post">
    <div>
        <label for="femail">Email:</label>
        <input id="femail" type="text" name="email" placeholder="Email..."/>
    </div>
    <div>
        <label for="fpassword">Password:</label>
        <input id="fpassword" type="password" name="password" placeholder="Password..."/>
    </div>
    <div style="clear: both"></div>
    <input type="submit" name="dosubmit" value="Submit"/>
</form>
<a href="auth.jsp">Already have an account?</a>
</body>
</html>
