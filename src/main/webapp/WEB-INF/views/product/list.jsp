<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All existing products</title>
</head>
<body>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Price</th>
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
                <form action="${pageContext.request.contextPath}/product/delete" method="post">
                    <input type="hidden" name="productId" value="${product.id}">
                    <button type="submit">Delete ${product.name} product</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
<a href="${pageContext.request.contextPath}/product/add">Add new product</a><br>
</body>
</html>
