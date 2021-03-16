<%--
  Created by IntelliJ IDEA.
  User: anario
  Date: 2021-02-14
  Time: 14:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*, java.util.*" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<div class="center">
    <h1>Auth Page</h1>
    <p>Please login to proceed...</p>

    <c:if test="${requestScope.error_message != null}">
    <div class="error_message">
        <c:forEach items="${requestScope.error_message}" var="err">
            <b style="color: red;"><c:out value="${err}"/></b>
        </c:forEach>
    </div>
    </c:if>
    <c:if test="${requestScope.success_message != null}">
        <div class="success_message">
            <b style="color: green;"><c:out value="${requestScope.success_message}"/></b>
        </div>
    </c:if>

    <form action="auth" method="POST">
        <div>
            <label for="fusername">Username:</label>
            <input id="fusername" type="text" name="email" placeholder="Email..."/>
        </div>
        <div>
            <label for="fpassword">Password:</label>
            <input id="fpassword" type="password" name="password" placeholder="Password..."/>
        </div>
        <div style="clear: both"></div>
        <input type="submit" name="dosubmit" value="Submit"/>
    </form>
    <a href="register.jsp">Don't have an account?</a>
</div>

