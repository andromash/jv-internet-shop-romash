# jv-internet-shop-romash

This is the project that implements idea of secured internet shop. 
The main subject of the shop is different beverages. 
This projects represents client - server architecture.
Also this projects was build on N-tier architecture. So we have DAO layer, Service layer, Controller.

Technologies, used in this project:
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

Client part of this application was created via jsp - pages and basic Bootstrap

## Idea of the shop
So, this shop works only with Authenticated users (Authentication is implemented via filter).
After user is registered it has 'USER' role by default. Authorization is also implemented via filter.
In sample case our project has two roles: USER and ADMIN.

##### USER: 
can see all of the products,
that exist in the shop (but not deleted, soft deleting is implemented), every USER has one shopping cart,
 can add product to cart and then complete order, can see all of its orders and details of them.
##### ADMIN:

role, set by system administrator, can delete user, see all users, see all orders, delete order,
add product, delete product. Admin does not have a cart and can not create order.

Also, it is predicted that roles can be combined. So if user has two roles - it can do both.

If you want to enter as Admin, there's address /admin/add  which will add new user with Admin's role

login: testAdmin

password: 1111

But first of all, there's file init_db.sql which holds sql script for creating appropriate DB.
So run this code in MySQL on your machine first. You need to change name and password
in ConnectionUtil so this code will run on your MySQL Server.
Also this projects runs in Apache Tomcat. So you need to configure it.
Maven is used as packaging tool, so you need to enable import all of the dependencies and plugins.

Project was made by Andrii Romash https://github.com/andromash