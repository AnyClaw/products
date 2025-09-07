package edu.miit.ProductService.repositories;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import edu.miit.ProductService.entities.User;
import edu.miit.ProductService.managers.ConnectionManager;

public class UserRepository{
    private final ConnectionManager connectionManager;

    public UserRepository(ConnectionManager connectionManager) {
        if (connectionManager == null)
            throw new IllegalArgumentException("Connection Manager mustn't be null");

        this.connectionManager = connectionManager;
    }

    public boolean addUser(User user) {
        String sqlCommand = """
            INSERT 
            INTO users(login, password, name, phone, role) 
            VALUES (?, ?, ?, ?)
            RETURNING *;
        """;

        if (connectionManager.isOpenConnection()) {
            try (PreparedStatement statement = connectionManager.getPreparedStatement(sqlCommand)) {
                statement.setString(1, user.getLogin());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getName());
                statement.setString(4, user.getPhone());

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // User createdUser = User.getUserByResultSet(resultSet);
                    return true;
                }
            }
            catch(SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }

    public boolean addUser(String login, String password, String name, String phone) {
        return addUser(
            new User(null, login, password, name, phone, new BigDecimal(0))
        );
    }

    public boolean isUserExist(User user) {
        return !findByLogin(user.getLogin()).isEmpty();
    }

    public Optional<User> findByLogin(String login) {
        String sqlCommand = "SELECT * FROM users WHERE login = ? LIMIT 1;";

        if (connectionManager.isOpenConnection()) {
            try (PreparedStatement statement = connectionManager.getPreparedStatement(sqlCommand)) {
                statement.setString(1, login);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) 
                    return Optional.of(User.getUserByResultSet(resultSet));
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
    }

    public Optional<User> updateBalance(Long id, BigDecimal balance) {
        String sqlCommand = "UPDATE users SET balance = balance + ?::money WHERE id = ? RETURNING *;";

        if (connectionManager.isOpenConnection()) {
            try (PreparedStatement statement = connectionManager.getPreparedStatement(sqlCommand)) {
                statement.setBigDecimal(1, balance);
                statement.setLong(2, id);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next())
                    return Optional.of(User.getUserByResultSet(resultSet));
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
    }
}
