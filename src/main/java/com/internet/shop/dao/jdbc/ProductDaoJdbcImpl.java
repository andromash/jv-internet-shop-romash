package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.ProductDao;
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
    public Product create(Product item) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO products(name, price) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, item.getName());
            statement.setBigDecimal(2, item.getPrice());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                item.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return item;
    }

    @Override
    public Optional<Product> get(Long id) {
        Product product = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT product_id, name, price "
                            + "FROM products WHERE deleted = 0 AND product_id=?");
            statement.setString(1, String.valueOf(id));
            ResultSet rs = statement.executeQuery();
            long productId = rs.getLong("product_id");
            String name = rs.getString("name");
            BigDecimal price = rs.getBigDecimal("price");
            product = new Product(productId, name, price);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(product);
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM products WHERE deleted = 0");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Product product = extractProductFromResultSet(rs);
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public Product update(Product item) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE products SET name = ?, price = ? WHERE product_id = ?");
            statement.setString(1, item.getName());
            statement.setString(2, String.valueOf(item.getPrice()));
            statement.setString(3, String.valueOf(item.getId()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE products SET deleted = 1 WHERE product_id = ?");
            statement.setString(1, String.valueOf(id));
            int i = statement.executeUpdate();
            if (i == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        long productId = rs.getLong("product_id");
        String name = rs.getString("name");
        BigDecimal price = rs.getBigDecimal("price");
        return new Product(productId, name, price);
    }
}
