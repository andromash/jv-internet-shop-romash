<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All orders</title>
</head>
<body>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Products</th>
        <th>User</th>
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
                    <button type="submit">Delete order</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
<a href="${pageContext.request.contextPath}/cart/products">Show cart</a><br>
<a href="${pageContext.request.contextPath}/registration">Sign up</a><br>
<a href="${pageContext.request.contextPath}/user/all">All user</a><br>
<a href="${pageContext.request.contextPath}/index">Main page</a><br>
</body>
</html>
