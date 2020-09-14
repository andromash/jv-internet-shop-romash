<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
          integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z"
          crossorigin="anonymous">
</head>
<body>
<div class="container">
    <h1>Hello! Please provide your information</h1>
    <h4 class="bg-danger">${message}</h4>
    <hr>
    <form method="post" action="${pageContext.request.contextPath}/registration">
        <div class="form-group">
            <label>Enter your name:</label>
            <input type="text" name="name" class="form-control">
        </div>
        <div class="form-group">
            <label>Create login:</label>
            <input type="text" name="login" class="form-control">
        </div>
        <div class="form-group">
            <label>Create password:</label>
            <input type="password" name="pwd" class="form-control">
        </div>
        <div class="form-group">
            <label>Repeat password:</label>
            <input type="password" name="pwd-repeated" class="form-control">
        </div>
        <button class="btn btn-primary" type="submit">Register</button>
    </form>
    <hr>
    <a href="${pageContext.request.contextPath}/index" class="page-link">Main page</a><br>
    <a href="${pageContext.request.contextPath}/user/all" class="page-link">All user</a><br>
    <a href="${pageContext.request.contextPath}/product/all" class="page-link">All products</a><br>
    <hr>
</div>
</body>
</html>
