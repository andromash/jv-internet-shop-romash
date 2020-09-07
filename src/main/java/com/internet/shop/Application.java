package com.internet.shop;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.model.User;
import com.internet.shop.service.OrderService;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import com.internet.shop.service.UserService;
import java.math.BigDecimal;

public class Application {
    private static final Long COFFEE_ID = 1L;
    private static final Long GREEN_TEA_ID = 2L;
    private static final Long BLACK_TEA_ID = 3L;
    private static Injector injector = Injector.getInstance("com.internet.shop");

    public static void main(String[] args) {
        ProductService productService = (ProductService) injector.getInstance(ProductService.class);
        productService.create(
                new Product("Coffee", BigDecimal.valueOf(125.50)));
        productService.create(
                new Product("Green Tea", BigDecimal.valueOf(38.0)));
        productService.create(
                new Product("Black Tea", BigDecimal.valueOf(34.50)));
        System.out.println("All products:");
        for (Product product : productService.getAll()) {
            System.out.println(product);
        }
        System.out.println("Changing price of green tea:");
        Product greenTeaProduct = new Product(productService.get(GREEN_TEA_ID));
        greenTeaProduct.setPrice(greenTeaProduct.getPrice().multiply(BigDecimal.valueOf(0.85)));
        productService.update(greenTeaProduct);
        System.out.println(productService.get(GREEN_TEA_ID));
        System.out.println("Deleting coffee:");
        productService.delete(COFFEE_ID);
        for (Product product : productService.getAll()) {
            System.out.println(product);
        }

        System.out.println("Creating users..");
        UserService userService = (UserService) injector.getInstance(UserService.class);
        User andrii = userService.create(
                new User("Andrii", "beer-drinker", "12345678"));
        User anna = userService.create(
                new User("Anna", "green-tee-pohudeniye", "love333love"));
        System.out.println(userService.getAll());
        System.out.println("Creating carts...");

        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        ShoppingCart shoppingCartOfAndrii = shoppingCartService.create(
                new ShoppingCart(andrii.getId()));
        ShoppingCart shoppingCartOfAnna = shoppingCartService.create(
                new ShoppingCart(anna.getId()));
        shoppingCartService.addProduct(shoppingCartOfAndrii, productService.get(GREEN_TEA_ID));
        shoppingCartService.addProduct(shoppingCartOfAndrii, productService.get(BLACK_TEA_ID));
        shoppingCartService.addProduct(shoppingCartOfAnna, productService.get(GREEN_TEA_ID));
        shoppingCartService.addProduct(shoppingCartOfAnna, productService.get(GREEN_TEA_ID));
        System.out.println("Andrii's cart: " + shoppingCartService.getByUserId(andrii.getId()));
        System.out.println("Anna's cart: " + shoppingCartService.getByUserId(anna.getId()));
        System.out.println("Andrii don't wanna black tea..");
        shoppingCartService.deleteProduct(shoppingCartOfAndrii, productService.get(BLACK_TEA_ID));
        System.out.println(shoppingCartService.getByUserId(andrii.getId()));

        System.out.println("Creating orders..");
        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);
        orderService.completeOrder(shoppingCartOfAndrii);
        orderService.completeOrder(shoppingCartOfAnna);
        System.out.println(orderService.getAll());
    }
}
