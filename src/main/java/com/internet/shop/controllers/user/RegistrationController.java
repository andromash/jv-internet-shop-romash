package com.internet.shop.controllers.user;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Role;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.model.User;
import com.internet.shop.service.ShoppingCartService;
import com.internet.shop.service.UserService;
import com.internet.shop.util.HashUtil;
import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/registration")
public class RegistrationController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private UserService userService = (UserService) injector.getInstance(UserService.class);
    private ShoppingCartService shoppingCartService
            = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String login = req.getParameter("login");
        String password = req.getParameter("pwd");
        String passwordRepeated = req.getParameter("pwd-repeated");

        if (userService.findByLogin(login).isPresent()) {
            req.setAttribute("message", "This login is already taken. Choose another one");
            req.getRequestDispatcher("/WEB-INF/views/registration.jsp").forward(req, resp);
            return;
        }
        if (password.equals(passwordRepeated)) {
            byte[] salt = HashUtil.getSalt();
            password = HashUtil.hashPassword(password, salt);
            User user = new User(name, login, password, salt);
            user.setRoles(Set.of(Role.of("USER")));
            User userWithId = userService.create(user);
            ShoppingCart shoppingCart = new ShoppingCart(userWithId.getId());
            shoppingCartService.create(shoppingCart);
            resp.sendRedirect(req.getContextPath() + "/index");
        } else {
            req.setAttribute("message", "You password and repeated one don't match");
            req.getRequestDispatcher("/WEB-INF/views/registration.jsp").forward(req, resp);
        }
    }
}
