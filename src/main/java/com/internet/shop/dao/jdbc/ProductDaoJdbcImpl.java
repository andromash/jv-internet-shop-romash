package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.ProductDao;
import com.internet.shop.exception.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Product;
import com.internet.shop.util.ConnectionUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class ProductDaoJdbcImpl implements ProductDao {
    @Override
    public Product create(Product product) {
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO products(name, price) VALUES (?, ?)",
                            Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                product.setId(resultSet.getLong(1));
            }
            return product;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't create product - " + product, e);
        }
    }

    @Override
    public Optional<Product> get(Long id) {
        Product product = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM products WHERE deleted = 0 AND product_id = ?")) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                product = extractProductFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get product from DB with ID = " + id, e);
        }
        return Optional.ofNullable(product);
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM products WHERE deleted = false")) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Product product = extractProductFromResultSet(rs);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get items from DB", e);
        }
        return products;
    }

    @Override
    public Product update(Product product) {
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                        "UPDATE products SET name = ?, price = ? WHERE product_id = ?")) {
            statement.setString(1, product.getName());
            statement.setString(2, String.valueOf(product.getPrice()));
            statement.setString(3, String.valueOf(product.getId()));
            statement.executeUpdate();
            return product;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't update product - " + product, e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE products SET deleted = 1 WHERE product_id = ?")) {
            statement.setString(1, String.valueOf(id));
            int i = statement.executeUpdate();
            return i == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't delete product with ID = " + id, e);
        }
    }

    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        long productId = rs.getLong("product_id");
        String name = rs.getString("name");
        BigDecimal price = rs.getBigDecimal("price");
        return new Product(productId, name, price);
    }
}
