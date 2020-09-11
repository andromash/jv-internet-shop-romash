<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Your orders</title>
</head>
<body>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Products</th>
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
                <a href="${pageContext.request.contextPath}/user/order?id=${orderId}"></a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
