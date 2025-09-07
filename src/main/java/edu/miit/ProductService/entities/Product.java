package edu.miit.ProductService.entities;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Product {
    private final Long id;
    private final String name;
    private Long amount;
    private BigDecimal cost;

    public Product(Long id, String name, Long amount, BigDecimal cost) {
        this.id = id;
        this.name = name;
        setAmount(amount);
        setCost(cost);
    }

    public Product(String name, Long amount, BigDecimal cost) {
        this(null, name, amount, cost);
    }

    public static Product getProductByResultset(ResultSet resultSet) {
        try {
            return new Product(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getLong("amount"),
                new BigDecimal(
                            resultSet.getString("cost")
                            .replace(",", "")
                            .replace("$", ""))
            );
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addAmount(Long amount) {
        setAmount(this.amount + amount);
    }

    public void removeAmount(Long amount) {
        setAmount(this.amount - amount);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Amount must be positive");

        this.amount = amount;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        if (cost.doubleValue() < 0)
            throw new IllegalArgumentException("Cost must be positive");

        this.cost = cost;
    }

    @Override
    public String toString() {
        if (amount.doubleValue() == 0) return name + " {\n\tнет в наличии\n}\n";

        return String.format(
            "%s {\n\tкол-во: %d,\n\tстоимость за штуку: %f\n}", 
            name, amount, cost.doubleValue()
        );
    }
}
