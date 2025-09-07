package edu.miit.ProductService.ui;

import java.util.Scanner;

import edu.miit.ProductService.entities.User;
import edu.miit.ProductService.managers.ConnectionManager;
import edu.miit.ProductService.managers.SessionManager;
import edu.miit.ProductService.services.UserService;

public class UserConsoleUI {
    private final Scanner in;
    private final SessionManager sessionManager;
    private final UserService userService;

    private final String separatorStr = "---------------";

    public UserConsoleUI(Scanner in, ConnectionManager connectionManager, SessionManager sessionManager) {
        this.in = in;
        this.sessionManager = sessionManager;
        userService = new UserService(connectionManager, sessionManager);
    }

    public void registrationForm() {
        String email, password, confirmPassword, name, phone;

        System.out.println(separatorStr);
        System.out.println("Регистрация нового пользователя");
        System.out.println(separatorStr);

        System.out.println("Введите email");
        email = in.nextLine();
        System.out.println(separatorStr);

        System.out.println("Введите пароль");
        password = in.nextLine();
        System.out.println(separatorStr);

        System.out.println("Подтвердите пароль");
        confirmPassword = in.nextLine();
        System.out.println(separatorStr);

        System.out.println("Введите ваше имя");
        name = in.nextLine();
        System.out.println(separatorStr);

        System.out.println("Введите ваш телефон");
        phone = in.nextLine();
        System.out.println(separatorStr);

        String message = userService.register(email, password, confirmPassword, name, phone);
        System.out.println(message);
        System.out.println(separatorStr);
    }

    public void loginForm() {
        String login, password;

        System.out.println(separatorStr);
        System.out.println("Вход в учётную запись");
        System.out.println(separatorStr);

        System.out.println("Введите логин");
        login = in.nextLine();
        System.out.println(separatorStr);

        System.out.println("Введите пароль");
        password = in.nextLine();
        System.out.println(separatorStr);

        String message = userService.login(login, password);
        System.out.println(message);
        System.out.println(separatorStr);
    }

    public boolean showUserInfo() {
        User user = sessionManager.getCurrentUser();

        System.out.println(separatorStr);

        if (user != null) {
            System.out.println(user);
            System.out.println(separatorStr);
            return true;
        }

        System.out.println("Вы не авторизованы");
        System.out.println(separatorStr);
        return false;
    }

    public void topUpBalanceForm() {
        System.out.println(separatorStr);
        System.out.println("Введите сумму");
        System.out.println(separatorStr);

        Double balance = null;

        do {
            if (balance != null) System.out.println("Введено некорректное значение");

            balance = Double.parseDouble(in.nextLine());
        } 
        while (balance < 0);

        if (userService.topUpBalance(balance)) {
            System.out.println("Баланс пополнен");
        }
        else {
            System.out.println("Что то пошло не так");
        }
    }
}
