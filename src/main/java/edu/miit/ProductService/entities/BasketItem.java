package edu.miit.ProductService.entities;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BasketItem {
    public final Long userId;
    public final Long productId;
    private Long amount;

    public BasketItem(Long userId, Long productId, Long amount) {
        this.userId = userId;
        this.productId = productId;
        this.amount = amount;
    }

    public static BasketItem getBasketItemByResultSet(ResultSet resultSet) {
        try {
            return new BasketItem(
                resultSet.getLong("id_user"),
                resultSet.getLong("id_product"),
                resultSet.getLong("amount")
            );
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "BasketItem{" + userId + " " + productId + " " + amount + "}";
    }
}
