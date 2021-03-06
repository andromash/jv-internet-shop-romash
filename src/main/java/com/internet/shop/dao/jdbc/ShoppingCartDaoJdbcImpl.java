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
        ShoppingCart shoppingCart = new ShoppingCart();
        String query = "SELECT * FROM shopping_carts WHERE user_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                shoppingCart = extractShoppingCartFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get shopping cart from DB with user ID = "
                    + userId, e);
        }
        shoppingCart.setProducts(extractProductsFromShoppingCart(shoppingCart.getId()));
        return Optional.of(shoppingCart);
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
        ShoppingCart shoppingCart = new ShoppingCart();
        String query = "SELECT * FROM shopping_carts WHERE cart_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                shoppingCart = extractShoppingCartFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get shopping cart from DB with ID = "
                    + id, e);
        }
        shoppingCart.setProducts(extractProductsFromShoppingCart(shoppingCart.getId()));
        return Optional.of(shoppingCart);
    }

    @Override
    public List<ShoppingCart> getAll() {
        List<ShoppingCart> shoppingCarts = new ArrayList<>();
        String query = "SELECT * FROM shopping_carts WHERE deleted = false";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                shoppingCarts.add(extractShoppingCartFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get shopping carts from DB", e);
        }
        for (ShoppingCart shoppingCart : shoppingCarts) {
            shoppingCart.setProducts(extractProductsFromShoppingCart(shoppingCart.getId()));
        }
        return shoppingCarts;
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
        insertProducts(shoppingCart);
        return shoppingCart;

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
                + "VALUES(?, ?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(queryToUpdateProducts)) {
            statement.setLong(1, shoppingCart.getId());
            for (Product product : shoppingCart.getProducts()) {
                statement.setLong(2, product.getId());
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
            throw new DataProcessingException("Couldn't get shopping cart products with ID = "
                    + cartId, e);
        }
    }

    private ShoppingCart extractShoppingCartFromResultSet(ResultSet resultSet) throws SQLException {
        Long cartId = resultSet.getLong("cart_id");
        Long userId = resultSet.getLong("user_id");
        return new ShoppingCart(cartId, userId);
    }
}
