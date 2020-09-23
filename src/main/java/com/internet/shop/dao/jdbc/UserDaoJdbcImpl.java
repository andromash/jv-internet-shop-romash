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
        User user = new User();
        String query = "SELECT * FROM users u\n"
                + "INNER JOIN users_roles ur ON u.user_id = ur.user_id\n"
                + "INNER JOIN roles r ON ur.role_id = r.role_id\n"
                + "WHERE u.deleted = false AND u.login = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user = extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get user from DB with login = " + login, e);
        }
        user.setRoles(getRolesOfUser(user.getId()));
        return Optional.of(user);
    }

    @Override
    public User create(User user) {
        String query = "INSERT INTO users(name, login, password) VALUES (?, ?, ?);";
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
        return insertRoles(user);
    }

    @Override
    public Optional<User> get(Long id) {
        User user = new User();
        String query = "SELECT * FROM users u\n"
                + "INNER JOIN users_roles ur ON u.user_id = ur.user_id\n"
                + "WHERE u.deleted = false AND u.user_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user = extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get user from DB with ID = " + id, e);
        }
        user.setRoles(getRolesOfUser(user.getId()));
        return Optional.of(user);
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
        for (int i = 0; i < allUsers.size(); i++) {
            User user = allUsers.get(i);
            user.setRoles(getRolesOfUser(user.getId()));
            allUsers.set(i, user);
        }
        return allUsers;
    }

    @Override
    public User update(User user) {
        String query = "UPDATE users SET name = ?, login = ?, password = ? WHERE user_id = ?;";
        String queryToDeleteRoles = "DELETE FROM users_roles WHERE user_id = ?;";
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
        insertRoles(user);
        user.setRoles(getRolesOfUser(user.getId()));
        return user;
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
        String query = "SELECT r.role_id, role_name FROM roles r INNER JOIN users_roles ur "
                + "ON ur.role_id = r.role_id WHERE ur.user_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet rs = statement.executeQuery();
            Set<Role> roles = new HashSet<>();
            while (rs.next()) {
                Long roleId = rs.getLong("role_id");
                String roleName = rs.getString("role_name");
                Role role = Role.of(roleName);
                role.setId(roleId);
                roles.add(role);
            }
            return roles;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get user with ID = " + userId, e);
        }
    }

    private User insertRoles(User user) {
        String queryToUpdateRoles = "INSERT INTO users_roles(user_id, role_id) "
                + "VALUES(?,(SELECT role_id FROM roles WHERE role_name = ?)); ";
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
        user.setRoles(getRolesOfUser(user.getId()));
        return user;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        long userId = rs.getLong("user_id");
        String name = rs.getString("name");
        String login = rs.getString("login");
        String password = rs.getString("password");
        return new User(userId, name, login, password);
    }
}
