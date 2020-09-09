<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add new product</title>
</head>
<body>
<h1>Enter information about new product</h1>
<h4 style="color: red">${message}</h4>
<form method="post" action="${pageContext.request.contextPath}/product/add">
<table>
    <tr>
        <td>
            <label>Enter name of product</label>
        </td>
        <td>
            <input type="text" name="name">
        </td>
    </tr>
    <tr>
        <td>
            <label>Enter price of product</label>
        </td>
        <td>
            <input type="text" name="price">
        </td>
    </tr>
    <tr>
        <td rowspan="2">
            <button type="submit">Add new product to DB</button>
        </td>
    </tr>
</table>
</form>
<a href="${pageContext.request.contextPath}/index">Main page</a><br>
<a href="${pageContext.request.contextPath}/user/all">All user</a><br>
<a href="${pageContext.request.contextPath}/product/all">All products</a><br>
</body>
</html>
