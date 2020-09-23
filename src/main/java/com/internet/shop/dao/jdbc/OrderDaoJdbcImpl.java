package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.OrderDao;
import com.internet.shop.exception.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Order;
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
public class OrderDaoJdbcImpl implements OrderDao {
    @Override
    public List<Order> getOrdersOfUser(Long userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE user_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get order from DB with user ID = "
                    + userId, e);
        }
        for(Order order : orders) {
            order.setProducts(extractProductsFromOrder(order.getId()));
        }
        return orders;
    }

    @Override
    public Order create(Order order) {
        String query = "INSERT INTO orders(user_id) VALUES (?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, order.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                order.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't create order - " + order, e);
        }
        return insertProducts(order);
    }

    @Override
    public Optional<Order> get(Long id) {
        Order order = new Order();
        String query = "SELECT * FROM orders WHERE order_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                order = extractOrderFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get order from DB with ID = " + id, e);
        }
        order.setProducts(extractProductsFromOrder(order.getId()));
        return Optional.of(order);
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        ResultSet rs;
        String query = "SELECT * FROM orders;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            rs = statement.executeQuery();
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get orders from DB", e);
        }
        try {
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get orders from DB", e);
        }
        return orders;
    }

    @Override
    public Order update(Order order) {
        String queryToDeleteProducts = "DELETE FROM orders_products WHERE order_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(queryToDeleteProducts)) {
            statement.setLong(1, order.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't delete product from shopping cart - "
                    + order, e);
        }
        return insertProducts(order);
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE orders SET deleted = true WHERE order_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't delete order with ID = " + id, e);
        }
    }

    private Order insertProducts(Order order) {
        String queryToUpdateProducts = "INSERT INTO orders_products(order_id, product_id) "
                + "VALUES(?,?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(queryToUpdateProducts)) {
            statement.setLong(1, order.getId());
            for (int i = 0; i < order.getProducts().size(); i++) {
                statement.setLong(2, order.getProducts().get(i).getId());
                statement.executeUpdate();
            }
            return order;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't insert data to order with ID = "
                    + order.getId(), e);
        }
    }

    private List<Product> extractProductsFromOrder(Long orderId) {
        String query = "SELECT * FROM products p INNER JOIN orders_products op "
                + "ON p.product_id = op.product_id WHERE op.order_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                Long productId = resultSet.getLong("product_id");
                String name = resultSet.getString("name");
                BigDecimal price = resultSet.getBigDecimal("price");
                products.add(new Product(productId, name, price));
            }
            return products;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get order with ID = " + orderId, e);
        }
    }

    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Long orderId = rs.getLong("order_id");
        Long userId = rs.getLong("user_id");
        return new Order(orderId, userId);
    }
}
