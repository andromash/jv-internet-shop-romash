<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login page</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
          integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z"
          crossorigin="anonymous">
</head>
<body>
<div class="container">
    <h1>Login page</h1>
    <h4 class="bg-danger">${errorMsg}</h4>
    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="form-group">
            <label>Enter login:</label>
            <input type="text" name="login" class="form-control">
        </div>
        <div class="form-group">
            <label>Enter password:</label>
            <input type="password" name="pwd" class="form-control">
        </div>
        <button class="btn btn-primary" type="submit">Login</button>
    </form>
    <hr>
    <a href="${pageContext.request.contextPath}/registration">Do not have account yet? Sign up now!</a>
</div>
</body>
</html>
