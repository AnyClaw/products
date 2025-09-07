package edu.miit.ProductService.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.miit.ProductService.entities.BasketItem;
import edu.miit.ProductService.entities.User;
import edu.miit.ProductService.managers.ConnectionManager;

public class BasketRepository {
    private final ConnectionManager connectionManager;

    public BasketRepository(ConnectionManager connectionManager) {
        if (connectionManager == null)
            throw new IllegalArgumentException("Connection mustn't be null");

        this.connectionManager = connectionManager;
    }

    public List<BasketItem> findBasketItemsByUserId(User user) {
        String sqlCommand = "SELECT * FROM basket WHERE id_user=?;";
        List<BasketItem> items = new ArrayList<>();

        if (connectionManager.isOpenConnection()) {
            try (PreparedStatement statement = connectionManager.getPreparedStatement(sqlCommand)) {
                statement.setLong(1, user.getId());

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    items.add(BasketItem.getBasketItemByResultSet(resultSet));
                }
            }
            catch(SQLException e) {
                throw new IllegalArgumentException(e);
            }
        }

        return items;
    }

    public boolean updateBasket(Long userId, Long productId, Long newAmount) {
        String sqlCommand = """
                INSERT INTO basket
                VALUES (?, ?, ?)
                ON CONFLICT (id_user, id_product)
                DO UPDATE SET amount = ?;
            """;
        int cnt = 0;

        if (connectionManager.isOpenConnection()) {
            try (PreparedStatement statement = connectionManager.getPreparedStatement(sqlCommand)) {
                statement.setLong(1, userId);
                statement.setLong(2, productId);
                statement.setLong(3, newAmount);
                statement.setLong(4, newAmount);

                cnt = statement.executeUpdate();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return cnt != 0;
    }

    public boolean deleteProduct(Long userId, Long productId) {
        String sqlCommand = """
                DELETE FROM basket WHERE id_user = ? AND id_product = ?;
            """;
        int cnt = 0;

        if (connectionManager.isOpenConnection()) {
            try (PreparedStatement statement = connectionManager.getPreparedStatement(sqlCommand)) {
                statement.setLong(1, userId);
                statement.setLong(2, productId);

                cnt = statement.executeUpdate();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return cnt != 0;
    }
}