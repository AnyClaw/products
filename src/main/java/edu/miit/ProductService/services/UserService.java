package edu.miit.ProductService.services;

import java.math.BigDecimal;
import java.util.Optional;

import edu.miit.ProductService.entities.User;
import edu.miit.ProductService.managers.ConnectionManager;
import edu.miit.ProductService.managers.SessionManager;
import edu.miit.ProductService.repositories.UserRepository;

public class UserService {
    private final SessionManager sessionManager;

    private final UserRepository userRepository;

    public UserService(ConnectionManager connectionManager, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        userRepository = new UserRepository(connectionManager); 
    }

    public String register(String email, String password, String confirmPassword, String name, String phone) {
        if (!password.equals(confirmPassword)) {
            return "Пароли не совпадают";
        }

        User user = null;

        try {
            user = new User(email, password, name, phone, new BigDecimal(0));
        }
        catch(IllegalArgumentException e) {
            return e.getMessage();
        }

        if (userRepository.isUserExist(user)) {
            return "Пользователь с таким email уже существует";
        }

        if (userRepository.addUser(email, password, name, phone)) {
            return "Успешная регистрация";
        }

        return "Что то пошло не так";
    }

    public String login(String login, String password) {
        Optional<User> user = userRepository.findByLogin(login);

        if (!user.isEmpty()) {
            if (user.get().getPassword().equals(password)) {
                sessionManager.setCurrentUser(user.get());
                return "Произведён вход под именем " + sessionManager.getCurrentUser().getName();
            }
            else return "Неверный пароль";
        }

        return "Пользователь с таким логином не найден";
    }

    public boolean topUpBalance(double balance) {
        User user = sessionManager.getCurrentUser();
        Optional<User> updatedUser = userRepository.updateBalance(user.getId(), BigDecimal.valueOf(balance));

        if (updatedUser.isPresent()) {
            sessionManager.setCurrentUser(updatedUser.get());
            return true;
        }

        return false;
    }
}
