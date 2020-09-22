package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.ShoppingCartDao;
import com.internet.shop.exception.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;
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
public class ShoppingCartDaoJdbcImpl implements ShoppingCartDao {
    @Override
    public Optional<ShoppingCart> getByUser(Long userId) {
        String query = "SELECT * FROM shopping_carts WHERE user_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                return Optional.of(extractShoppingCartFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get shopping cart from DB with user ID = "
                    + userId, e);
        }
        return Optional.empty();
    }

    @Override
    public ShoppingCart create(ShoppingCart shoppingCart) {
        String query = "INSERT INTO shopping_carts(user_id) VALUES (?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, shoppingCart.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                shoppingCart.setId(resultSet.getLong(1));
            }
            return shoppingCart;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't create shopping cart - " + shoppingCart, e);
        }
    }

    @Override
    public Optional<ShoppingCart> get(Long id) {
        String query = "SELECT * FROM shopping_carts sc\n"
                + "INNER JOIN shopping_carts_products scp ON sc.cart_id = scp.cart_id\n"
                + "INNER JOIN products p ON scp.product_id = p.product_id\n"
                + "WHERE sc.cart_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(extractShoppingCartFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get shopping cart from DB with ID = "
                    + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<ShoppingCart> getAll() {
        String query = "SELECT * FROM shopping_carts sc\n"
                + "INNER JOIN shopping_carts_products scp ON sc.cart_id = scp.cart_id\n"
                + "INNER JOIN products p ON scp.product_id = p.product_id\n";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            List<ShoppingCart> shoppingCarts = new ArrayList<>();
            while (rs.next()) {
                shoppingCarts.add(extractShoppingCartFromResultSet(rs));
            }
            return shoppingCarts;
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get shopping carts from DB", e);
        }
    }

    @Override
    public ShoppingCart update(ShoppingCart shoppingCart) {
        String queryToDeleteProducts = "DELETE FROM shopping_carts_products WHERE cart_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(queryToDeleteProducts)) {
            statement.setLong(1, shoppingCart.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't delete products from shopping cart - "
                    + shoppingCart, e);
        }
        return insertProducts(shoppingCart);
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE shopping_carts SET deleted = true WHERE cart_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't delete shopping cart with ID = " + id, e);
        }
    }

    private ShoppingCart insertProducts(ShoppingCart shoppingCart) {
        String queryToUpdateProducts = "INSERT INTO shopping_carts_products(cart_id, product_id) "
                + "VALUES(?,?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(queryToUpdateProducts)) {
            for (int i = 0; i < shoppingCart.getProducts().size(); i++) {
                statement.setLong(1, shoppingCart.getId());
                statement.setLong(2, shoppingCart.getProducts().get(i).getId());
                statement.executeUpdate();
            }
            return shoppingCart;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't update shopping cart - " + shoppingCart, e);
        }
    }

    private List<Product> extractProductsFromShoppingCart(Long cartId) {
        String query = "SELECT * FROM products p INNER JOIN shopping_carts_products scp "
                + "ON p.product_id = scp.product_id WHERE scp.cart_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, cartId);
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
            throw new DataProcessingException("Couldn't get shopping cart with ID = " + cartId, e);
        }
    }

    private ShoppingCart extractShoppingCartFromResultSet(ResultSet resultSet) throws SQLException {
        Long cartId = resultSet.getLong("cart_id");
        Long userId = resultSet.getLong("user_id");
        List<Product> products = extractProductsFromShoppingCart(cartId);
        return new ShoppingCart(cartId, products, userId);
    }
}
