<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<h1>Hello! Please provide your information</h1>
<h4 style="color: red">${message}</h4>
<form method="post" action="${pageContext.request.contextPath}/registration">
<table>
    <tr>
        <td>
            <label>Enter your name:</label>
        </td>
        <td>
            <input type="text" name="name">
        </td>
    </tr>
    <tr>
        <td>
            <label>Create login:</label>
        </td>
        <td>
            <input type="text" name="login">
        </td>
    </tr>
    <tr>
        <td>
            <label>Create password:</label>
        </td>
        <td>
            <input type="password" name="pwd">
        </td>
    </tr>
    <tr>
        <td>
            <label>Repeat password:</label>
        </td>
        <td>
            <input type="password" name="pwd-repeated">
        </td>
    </tr>
    <tr>
        <td rowspan="2">
            <button type="submit">Register</button>
        </td>
    </tr>
</table>
</form>
<a href="${pageContext.request.contextPath}/index">Main page</a><br>
<a href="${pageContext.request.contextPath}/user/all">All user</a><br>
<a href="${pageContext.request.contextPath}/product/all">All products</a><br>
</body>
</html>
