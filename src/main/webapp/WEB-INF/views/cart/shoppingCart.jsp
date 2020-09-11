<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All users</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
          integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z"
          crossorigin="anonymous">
</head>
<body>
<div class="container">
    <table class="table table-striped table-hover">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Price</th>
            <th></th>
        </tr>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <c:out value="${product.id}"/>
                </td>
                <td>
                    <c:out value="${product.name}"/>
                </td>
                <td>
                    <c:out value="${product.price}"/>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/shopping-cart/product/delete?id=${product.id}">
                        Delete from the cart</a>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="2">
                <form action="${pageContext.request.contextPath}/order/complete" method="post">
                    <input type="hidden" name="cartId" value="${cartId}">
                    <button type="submit" class="btn-success">Create order</button>
                </form>
            </td>
        </tr>
    </table>
    <a href="${pageContext.request.contextPath}/registration">Sign up</a><br>
    <a href="${pageContext.request.contextPath}/user/all">All user</a><br>
    <a href="${pageContext.request.contextPath}/product/all">All products</a><br>
    <a href="${pageContext.request.contextPath}/index">Main page</a><br>
</div>
</body>
</html>
