package com.internet.shop.controllers;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Role;
import com.internet.shop.model.User;
import com.internet.shop.service.UserService;
import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/add")
public class InjectAdminController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private UserService userService
            = (UserService) injector.getInstance(UserService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User userAdmin = new User("admin","testAdmin", "1111");
        userAdmin.setRoles(Set.of(Role.of("ADMIN")));
        userService.create(userAdmin);
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
