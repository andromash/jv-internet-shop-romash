<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All orders</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
          integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z"
          crossorigin="anonymous">
</head>
<body>
<%@include file="../header.jsp" %>
<div class="container">
    <table class="table table-striped table-hover">
        <tr>
            <th>ID</th>
            <th>Products</th>
            <th>User</th>
            <th></th>
        </tr>
        <c:forEach var="order" items="${orders}">
            <tr>
                <td>
                    <c:out value="${order.id}"/>
                </td>
                <td>
                    <c:out value="${order.products}"/>
                </td>
                <td>
                    <c:out value="${order.userId}"/>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/user/order?id=${orderId}">Details</a>
                </td>
                <td>
                    <form action="${pageContext.request.contextPath}/order/delete" method="get">
                        <input type="hidden" name="orderId" value="${order.id}">
                        <button type="submit" class="btn-danger">Delete order</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
    <a href="${pageContext.request.contextPath}/cart/products">Show cart</a><br>
    <a href="${pageContext.request.contextPath}/registration">Sign up</a><br>
    <a href="${pageContext.request.contextPath}/user/all">All user</a><br>
    <a href="${pageContext.request.contextPath}/index">Main page</a><br>
</div>
</body>
</html>
