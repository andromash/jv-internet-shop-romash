<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add new product</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
          integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z"
          crossorigin="anonymous">
</head>
<body>
<h1>Enter information about new product</h1>
<h4 class="container-md">${message}</h4>
<div class="container">
    <form method="post" action="${pageContext.request.contextPath}/product/add">
        <table class="table table-striped table-hover">
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
                <td colspan="2">
                    <button type="submit" class="btn-success">Add new product to DB</button>
                </td>
            </tr>
        </table>
    </form>
    <a href="${pageContext.request.contextPath}/index">Main page</a><br>
    <a href="${pageContext.request.contextPath}/user/all">All user</a><br>
    <a href="${pageContext.request.contextPath}/product/all">All products</a><br>
</div>
</body>
</html>
