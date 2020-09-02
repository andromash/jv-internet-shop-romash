package com.internet.shop.dao.impl;

import com.internet.shop.db.Storage;
import com.internet.shop.model.Product;
import com.internet.shop.dao.ProductDao;
import com.internet.shop.lib.Dao;

import java.util.List;
import java.util.Optional;

@Dao
public class ProductDaoImpl implements ProductDao {
    @Override
    public Product create(Product product) {
        Storage.addProduct(product);
        return product;
    }

    @Override
    public Optional<Product> get(Long id) {
        return Storage.products.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Product> getAll() {
        return Storage.products;
    }

    @Override
    public Product update(Product product) {
        return Storage.products.stream()
                .filter(n -> n.getId().equals(product.getId()))
                .map(p -> p = product)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public boolean delete(Long id) {
        return Storage.products.remove(get(id).orElseThrow());
    }
}
