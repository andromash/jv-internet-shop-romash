package com.internet.shop.controllers;

import com.internet.shop.lib.Injector;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/shopping-cart/product/delete")
public class DeleteItemFromTheCartController extends HttpServlet {
    private static final long USER_ID = 1;
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private ShoppingCartService shoppingCartService
            = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
    private ProductService productService
            = (ProductService) injector.getInstance(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long productId = Long.valueOf(req.getParameter("id"));
        shoppingCartService.deleteProduct(
                shoppingCartService.getByUserId(USER_ID), productService.get(productId));
        resp.sendRedirect(req.getContextPath() + "/cart/products");
    }
}
