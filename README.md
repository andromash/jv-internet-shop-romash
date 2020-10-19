# jv-internet-shop-romash

This is the project that implements the idea of an internet shop specializing in beverages. 
This project, which represents the client-server architecture, was build following the N-tier architecture and has a DAO layer, a Service layer, and Controllers. 
I built this project with SOLID principles and RBAC in mind. The client side of this application was created with JSP-pages and Bootstrap.
Client part of this application was created via jsp - pages and basic Bootstrap

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

## Idea of the shop
Only authenticated users can use the full functionality of the shop. Authentication is implemented via a filter.
After a user is registered, they are assigned the 'USER' role by default. Authorization is also implemented via a filter.
In the shop prototype presented in this repository, the shop visitors can have only two roles: USER and ADMIN.

##### USER 
Users can see all of the products that are presented in the shop (except for deleted ones - soft deletion is implemented in the project).
Every user has one shopping cart. Users can add products to their cart and then complete the order; they can see all of their previous orders and their details.

##### ADMIN

This role is supposed to be assigned manually by the system administrator. An admin can see all users, delete a user, see all orders, delete an order, add products, and delete products. An admin does not have a cart and cannot place an order.
The project also allows for a combination of the two roles -  if a user has two roles, they can perform all of the actions described for both roles.
If you want to log in as an Admin, open the .../admin/add link in your browser, and a new user with Admin's role will be created with the following login and password: 
'testAdmin', '1111'

Also, it is predicted that roles can be combined. So if a user has two roles - they can perform both sets of actions.

But first of all, there's file init_db.sql which holds sql script for creating appropriate DB.
To launch the project, download the project onto your machine and make sure you have MySQL Workbench on it. The file `init_db.sql` holds the SQL script for creating the DB for this project - you need to run the code from this file in MySQL Workbench first. Then you need to change the name and password
in `ConnectionUtil` so this code will run on your MySQL Server.
This project runs on Apache Tomcat, so you will need to configure it as well.
Maven is used as the packaging tool, and you need to enable the import all of the dependencies and plugins.

Project created by Andrii Romash https://github.com/andromash
