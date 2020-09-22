package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.UserDao;
import com.internet.shop.exception.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Role;
import com.internet.shop.model.User;
import com.internet.shop.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Dao
public class UserDaoJdbcImpl implements UserDao {
    @Override
    public Optional<User> findByLogin(String login) {
        String query = "SELECT * FROM users u\n"
                + "INNER JOIN users_roles ur ON u.user_id = ur.user_id\n"
                + "INNER JOIN roles r ON ur.role_id = r.role_id\n"
                + "WHERE u.deleted = false AND u.login = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get user from DB with login = " + login, e);
        }
        return Optional.empty();
    }

    @Override
    public User create(User user) {
        String query = "INSERT INTO users(name, login, password) VALUES (?, ?, ?);";
        String queryToAddRole = "INSERT INTO users_roles(user_id, role_id) "
                + "VALUES (?, (SELECT role_id FROM roles WHERE role_name = ?));";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't create user - " + user, e);
        }
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(queryToAddRole)) {
            for (Role role : user.getRoles()) {
                statement.setLong(1, user.getId());
                statement.setString(2, role.getRoleName().name());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't create role of user - " + user, e);
        }
        return user;
    }

    @Override
    public Optional<User> get(Long id) {
        String query = "SELECT * FROM users u\n"
                + "INNER JOIN users_roles ur ON u.user_id = ur.user_id\n"
                + "WHERE u.deleted = false AND u.user_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get user from DB with ID = " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        List<User> allUsers = new ArrayList<>();
        String query = "SELECT * FROM users u\n"
                + "INNER JOIN users_roles ur ON u.user_id = ur.user_id\n"
                + "WHERE u.deleted = false;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                User user = extractUserFromResultSet(rs);
                allUsers.add(user);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get all users", e);
        }
        return allUsers;
    }

    @Override
    public User update(User user) {
        String query = "UPDATE users SET name = ?, login = ?, password = ? WHERE user_id = ?;";
        String queryToDeleteRoles = "DELETE FROM users_roles WHERE user_id = ?;";
        String queryToUpdateRoles = "INSERT INTO users_roles(user_id, role_id) "
                + "VALUES(?,(SELECT role_id FROM roles WHERE role_name = ?)); ";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.setLong(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't update user " + user, e);
        }
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(queryToDeleteRoles)) {
            statement.setLong(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't delete roles of user with ID = "
                    + user.getId(), e);
        }
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(queryToUpdateRoles)) {
            for (Role role : user.getRoles()) {
                statement.setLong(1, user.getId());
                statement.setString(2, role.getRoleName().name());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't update role of user - " + user, e);
        }
        return get(user.getId()).get();
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE users SET deleted = true WHERE user_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't delete user with ID = " + id, e);
        }
    }

    private Set<Role> getRolesOfUser(Long userId) {
        String query = "SELECT role_name FROM roles r INNER JOIN users_roles ur "
                + "ON ur.role_id = r.role_id WHERE ur.user_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet rs = statement.executeQuery();
            Set<Role> roles = new HashSet<>();
            while (rs.next()) {
                String roleName = rs.getString("role_name");
                Role role = Role.of(roleName);
                roles.add(role);
            }
            return roles;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get user with ID = " + userId, e);
        }
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        long userId = rs.getLong("user_id");
        String name = rs.getString("name");
        String login = rs.getString("login");
        String password = rs.getString("password");
        Set<Role> roles = getRolesOfUser(userId);
        return new User(userId, name, login, password, roles);
    }
}
