package edu.miit.ProductService.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager implements AutoCloseable {
    private final String DRIVER = "org.postgresql.Driver";
    private final String URL = "jdbc:postgresql://localhost/postgres";
    private final String USER = "postgres";
    private final String PASSWORD = "postgres";

    private Connection connection;

    public ConnectionManager() {
        if (!checkDriver())
            throw new RuntimeException("Driver isn't found");

        openConnection();
    }

    public final boolean checkDriver() {
        try {
            Class.forName(DRIVER);
            return true;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }

    public final boolean openConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            return true;
        }
        catch (SQLException e) {
            return false;
        }
    }

    public boolean isOpenConnection() {
        try {
            return connection != null && !connection.isClosed();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Statement getStatement() throws SQLException {
        if (isOpenConnection()) 
            return connection.createStatement();

        throw new RuntimeException("Connection is closed");
    }

    public PreparedStatement getPreparedStatement(String sqlCommand) throws SQLException {
        if (isOpenConnection()) 
            return connection.prepareStatement(sqlCommand);

        throw new RuntimeException("Connection is closed");
    }

    public final boolean closeConnection() {
        try {
            if (connection != null) connection.close();
            return true;
        }
        catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void close() {
        closeConnection();
    }
}