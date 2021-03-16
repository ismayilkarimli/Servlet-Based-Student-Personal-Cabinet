<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*, java.util.*" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Dashboard</title>
</head>
<body>
<div>
    <h3>Welcome, ${sessionScope.current_user}</h3>
    <h4>Your personal details</h4>
    <h5>Name:<c:out value="${user.firstName}"/></h5>
    <h5>Last name: ${user.lastName}</h5>
    <h5>Age: ${user.age}</h5>
    <h5>City: ${user.city}</h5>
    <h5>Country: ${user.country}</h5>
    <h5>Edit your details</h5>
    <form action="dashboard" method="post">
        <label>First Name: <input type="text" name="first_name"/></label>
        <label>Last Name: <input type="text" name="last_name"/></label>
        <label>Age: <input type="text" name="age" pattern="^[0-9]*$"/></label>
        <label>City: <input type="text" name="city"/></label>
        <label>Country: <input type="text" name="country"/></label>
        <input type="submit" name="dosubmit" value="Submit"/>
    </form>
    <h4>Courses you are in:</h4>
    <table>
        <c:forEach var="registered_course" items="${user.courses}">
            <tr>
                <td>${registered_course} <a href="dashboard?action=drop/${registered_course}">drop</a></td>
            </tr>
        </c:forEach>
    </table>
    <h4>Available courses:</h4>
    <table>
        <c:forEach var="course" items="${availableCourses}">
            <tr>
                <td>${course} <a href="dashboard?action=register/${course}">register</a></td>
            </tr>
        </c:forEach>
    </table>
    <a href="auth?action=logout">Log out</a>
</div>
</body>
</html>

