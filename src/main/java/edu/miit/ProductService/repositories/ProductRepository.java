package edu.miit.ProductService.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.miit.ProductService.entities.Product;
import edu.miit.ProductService.managers.ConnectionManager;

public class ProductRepository {
    private final ConnectionManager connectionManager;

    public ProductRepository(ConnectionManager connectionManager) {
        if (connectionManager == null)
            throw new IllegalArgumentException("Connection mustn't be null");

        this.connectionManager = connectionManager;
    }

    public List<Product> findAllProducts() {
        String sqlCommand = "SELECT * FROM products;";
        List<Product> products = new ArrayList<>();

        if (connectionManager.isOpenConnection()) {
            try (Statement statement = connectionManager.getStatement()) {
                ResultSet resultSet = statement.executeQuery(sqlCommand);

                while (resultSet.next()) {
                    products.add(Product.getProductByResultset(resultSet));
                }
            }
            catch(SQLException e) {
                throw new RuntimeException();
            }
        }

        return products;
    }

    public List<Product> findProductsByName(String name) {
        String sqlCommand = """
                SELECT * FROM products 
                WHERE LOWER(name) LIKE CONCAT('%', LOWER(?), '%');
                """;
        List<Product> products = new ArrayList<>();

        if (connectionManager.isOpenConnection()) {
            try (PreparedStatement statement = connectionManager.getPreparedStatement(sqlCommand)) {
                statement.setString(1, name);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    products.add(Product.getProductByResultset(resultSet));
                }
            }
            catch (SQLException e) {
                throw new RuntimeException();
            }
        }

        return products;
    }

    public Optional<Product> findProductById(long id) {
        String sqlCommand = "SELECT * FROM products WHERE id=? LIMIT 1;";

        if (connectionManager.isOpenConnection()) {
            try (PreparedStatement statement = connectionManager.getPreparedStatement(sqlCommand)) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return Optional.of(Product.getProductByResultset(resultSet));
                }
            }
            catch (SQLException e) {
                throw new RuntimeException();
            }
        }

        return Optional.empty();
    }
}
