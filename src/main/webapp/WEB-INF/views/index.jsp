<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Beverage internet shop</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
          integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z"
          crossorigin="anonymous">
</head>
<body>
<%@include file="header.jsp" %>
<div class="container-md">
    <h1>HELLO WORLD!</h1>
    <a href="${pageContext.request.contextPath}/registration">Sign up</a><br>
    <a href="${pageContext.request.contextPath}/user/all">All user</a><br>
    <a href="${pageContext.request.contextPath}/product/all">All products</a><br>
    <a href="${pageContext.request.contextPath}/product/add">Add product</a><br>
    <a href="${pageContext.request.contextPath}/orders/all">All orders</a><br>
</div>
</body>
</html>
