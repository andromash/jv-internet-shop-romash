package com.shop.db;

import com.shop.model.Product;

import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static long productId = 0;
    public static final List<Product> products = new ArrayList<>();

    public static void addProduct(Product product) {
        productId++;
        product.setId(productId);
        products.add(product);
    }
}
