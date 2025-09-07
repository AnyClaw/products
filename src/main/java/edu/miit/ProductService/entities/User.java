package edu.miit.ProductService.entities;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User implements Cloneable {
    private final Long id;
    private String login;
    private String password;
    private String name;
    private String phone;
    private BigDecimal balance;

    public User(Long id, String login, String password, String name, String phone, BigDecimal balance) {
        this.id = id;
        setLogin(login);
        setPassword(password);
        setName(name);
        setPhone(phone);
        this.balance = balance;
    }

    public User(String login, String password, String name, String phone, BigDecimal balance) {
        this(null, login, password, name, phone, balance);
    }

    public static User getUserByResultSet(ResultSet resultSet) {
        try {
            return new User(
                resultSet.getLong("id"),
                resultSet.getString("login"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("phone"),
                new BigDecimal(
                    resultSet.getString("balance")
                    .replace(",", "")
                    .replace("$", "")
                )
            );
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if (login == null || login.isBlank())
            throw new IllegalArgumentException("Логин введено некорректно");

        this.login = login;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank())   
            throw new IllegalArgumentException("Пароль введён некорректно");

        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Имя введено некорректно");

        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.isBlank())
            throw new IllegalArgumentException("Телефон введён некорректно");

        this.phone = phone;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public User copy() {
        return new User(id, login, password, name, phone, balance);
    }

    @Override
    public User clone() {
        try {
            return (User) super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return String.format(
            "Имя: %s\nБаланс: %f",
            name, balance.doubleValue()
        );
    }
}
