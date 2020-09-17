package com.internet.shop.web.filter;

import com.internet.shop.controllers.LoginController;
import com.internet.shop.lib.Injector;
import com.internet.shop.model.Role;
import com.internet.shop.model.User;
import com.internet.shop.service.UserService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthorizationFilter implements Filter {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private UserService userService
            = (UserService) injector.getInstance(UserService.class);
    private final Map<String, List<Role.RoleName>> protectedUrls = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        protectedUrls.put("/user/all", List.of(Role.RoleName.ADMIN));
        protectedUrls.put("/user/delete", List.of(Role.RoleName.ADMIN));
        protectedUrls.put("/orders/all", List.of(Role.RoleName.ADMIN));
        protectedUrls.put("/order/delete", List.of(Role.RoleName.ADMIN));
        protectedUrls.put("/admin/product/all", List.of(Role.RoleName.ADMIN));
        protectedUrls.put("/product/add", List.of(Role.RoleName.ADMIN));
        protectedUrls.put("/product/delete", List.of(Role.RoleName.ADMIN));
        protectedUrls.put("/order/complete", List.of(Role.RoleName.USER));
        protectedUrls.put("/shopping-cart/product/delete", List.of(Role.RoleName.USER));
        protectedUrls.put("/cart/products", List.of(Role.RoleName.USER));
        protectedUrls.put("/user/orders", List.of(Role.RoleName.USER));
        protectedUrls.put("/product/buy", List.of(Role.RoleName.USER));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String url = req.getServletPath();
        Long userId = (Long) req.getSession().getAttribute(LoginController.USER_ID);
        if (!protectedUrls.containsKey(url)
                || isAuthorized(userService.get(userId), protectedUrls.get(url))) {
            filterChain.doFilter(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/accessDenied.jsp").forward(req, resp);
        }
    }

    @Override
    public void destroy() {
    }

    private boolean isAuthorized(User user, List<Role.RoleName> authorizedRoles) {
        for (Role.RoleName authorizedRole : authorizedRoles) {
            for (Role userRole : user.getRoles()) {
                if (authorizedRole.equals(userRole.getRoleName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
