#jv-internet-shop-romash

This is project that implements idea of secured internet shop. 
The main subject of shop is different beverages. 
This projects represents client - server architecture.

Technologies used in this project:
- Java 8
- Servlets
- Filters
- JSP
- Bootstrap
- Maven
- Tomcat
- DAO pattern
- MySQL
- JDBC

Also, I tried to keep in mind SOLID principles and RBAC

Client part of this application were created via jsp - pages and basic Bootstrap

## Idea of the shop
So, this shop works only with Authenticated users (Authentication is implemented via filter).
After user is registered it has 'USER' role by default. Authorization is also implemented via filter.
In sample case our project has two roles: USER and ADMIN.
#####USER: 
can see all of the products,
that exist in the shop (but not deleted, soft deleting is implemented), every USER has one shopping cart,
 can add product to cart and then complete order, can see all of its orders and details of them.
#####ADMIN:
role, set by system administrator, can delete user, see all users, see all orders, delete order,
add product, delete product. Admin does not have a cart and can not create order.

Also, it is predicted that roles can be combined. So if user has two roles - it can do both.
