# jv-internet-shop-romash

This is the project that implements the idea of an internet shop specializing in beverages. 
This project, which represents the client-server architecture, was build following the N-tier architecture and has a DAO layer, a Service layer, and Controllers. I built this project with SOLID principles and RBAC in mind. The client side of this application was created with JSP-pages and Bootstrap.

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
To launch the project, download the project onto your machine and make sure you have MySQL Workbench on it. The file `init_db.sql` holds the SQL script for creating the DB for this project - you need to run the code from this file in MySQL Workbench first. Then you need to change the name and password
in `ConnectionUtil` so this code will run on your MySQL Server.
This project runs on Apache Tomcat, so you will need to configure it as well.
Maven is used as the packaging tool, and you need to enable the import all of the dependencies and plugins.

Project was made by Andrii Romash https://github.com/andromash
