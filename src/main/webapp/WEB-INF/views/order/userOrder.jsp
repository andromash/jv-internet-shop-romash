<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Order</title>
</head>
<body>
<table>
    <tr>
        <td>
            <label>Order ID</label>
        </td>
        <td>
            <p>${order.id}</p>
        </td>
    </tr>
    <tr>
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
    <tr>
        <td>
            <label>Total sum</label>
        </td>
        <td>
            <p><b>${total}</b></p>
        </td>
    </tr>
</table>
</body>
</html>
