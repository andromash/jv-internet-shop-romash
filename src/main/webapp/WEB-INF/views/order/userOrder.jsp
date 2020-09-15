<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Order</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
          integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z"
          crossorigin="anonymous">
</head>
<body>
<%@include file="../header.jsp" %>
<div class="container">
    <table class="table table-striped table-hover">
        <tr>
            <td>
                <label>Order ID</label>
            </td>
            <td>
                <p>${order.id}</p>
            </td>
        </tr>
        <tr class="primary">
            <th>Product name</th>
            <th>Price</th>
        </tr>
        <c:forEach var="product" items="${order.products}">
            <tr>
                <td>
                    <c:out value="${product.name}"/>
                </td>
                <td>
                    <c:out value="${product.price}"/>
                </td>
            </tr>
        </c:forEach>
        <tr class="success">
            <td>
                <label>Total sum</label>
            </td>
            <td>
                <p><b>${total}</b></p>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
