package edu.miit.ProductService.repositories;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import edu.miit.ProductService.entities.User;
import edu.miit.ProductService.managers.ConnectionManager;

public class OrderRepository {
    private final ConnectionManager connectionManager;

    public OrderRepository(ConnectionManager connectionManager) {
        if (connectionManager == null)
            throw new IllegalArgumentException("Connection mustn't be null");

        this.connectionManager = connectionManager;
    }

    public void deleteBasketItems(Long userId) {
        String sqlCommand = "DELETE FROM basket WHERE id_user = ?;";

        if (connectionManager.isOpenConnection()) {
            try (PreparedStatement statement = connectionManager.getPreparedStatement(sqlCommand)) {
                statement.setLong(1, userId);
                statement.executeUpdate();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void updateProduct(Long productId, Long amount) {
        String sqlCommand = "UPDATE products SET amount = amount - ? WHERE id = ?;";

        if (connectionManager.isOpenConnection()) {
            try (PreparedStatement statement = connectionManager.getPreparedStatement(sqlCommand)) {
                statement.setLong(1, amount);
                statement.setLong(2, productId);
                statement.executeUpdate();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Optional<User> updateBalance(Long userId, BigDecimal balance) {
        String sqlCommand = "UPDATE users SET balance = ?::money WHERE id = ? RETURNING *;";

        if (connectionManager.isOpenConnection()) {
            try (PreparedStatement statement = connectionManager.getPreparedStatement(sqlCommand)) {
                statement.setBigDecimal(1, balance);
                statement.setLong(2, userId);

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
