package com.internet.shop.controllers.order;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Order;
import com.internet.shop.model.Product;
import com.internet.shop.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/user/order")
public class GetOrderByIdController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private OrderService orderService
            = (OrderService) injector.getInstance(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long orderId = Long.valueOf(req.getParameter("id"));
        Order order = orderService.get(orderId);
        req.setAttribute("order", order);
        BigDecimal totalSum = order.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        req.setAttribute("total", totalSum);
        req.getRequestDispatcher("/WEB-INF/views/order/userOrder.jsp").forward(req, resp);
    }
}
