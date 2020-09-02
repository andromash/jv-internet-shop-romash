package com.internet.shop;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.service.ProductService;

public class Application {
    private static final Long COFFEE_ID = 1L;
    private static final Long GREEN_TEA_ID = 2L;
    private static Injector injector = Injector.getInstance("com.internet.shop");

    public static void main(String[] args) {
        ProductService productService = (ProductService) injector.getInstance(ProductService.class);
        productService.create(new Product("Coffee", 125.50));
        productService.create(new Product("Green Tea", 38.0));
        productService.create(new Product("Black Tea", 34.50));
        System.out.println("All products:");
        for (Product product : productService.getAll()) {
            System.out.println(product);
        }
        System.out.println("Changing price of green tea:");
        Product greenTeaProduct = productService.get(GREEN_TEA_ID);
        greenTeaProduct.setPrice(greenTeaProduct.getPrice() * 0.85);
        productService.update(greenTeaProduct);
        System.out.println(productService.get(GREEN_TEA_ID));
        System.out.println("Deleting coffee:");
        productService.delete(COFFEE_ID);
        for (Product product : productService.getAll()) {
            System.out.println(product);
        }
    }
}
