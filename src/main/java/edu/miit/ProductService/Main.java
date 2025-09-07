package edu.miit.ProductService;

import java.util.Scanner;

import edu.miit.ProductService.managers.ConnectionManager;
import edu.miit.ProductService.managers.SessionManager;
import edu.miit.ProductService.ui.UserConsoleUI;
import edu.miit.ProductService.ui.BasketConsoleUI;
import edu.miit.ProductService.ui.OrderConsoleUI;
import edu.miit.ProductService.ui.ProductConsoleUI;

public class Main {
    private static SessionManager sessionManager;

    private static UserConsoleUI userConsoleUI;
    private static ProductConsoleUI productConsoleUI;
    private static BasketConsoleUI basketConsoleUI;
    private static OrderConsoleUI orderConsoleUI;

    private static final String separatorStr = "---------------";

    public static void main(String[] args) {
        sessionManager = new SessionManager();

        try (
            ConnectionManager connectionManager = new ConnectionManager();
            Scanner in = new Scanner(System.in)
        ) {
            initUIClasses(in, connectionManager);

            if (connectionManager.isOpenConnection()) mainScreen(in);
            else System.out.println("Ошибка подключения к серверу");
        }
    }

    public static void initUIClasses(Scanner in, ConnectionManager connectionManager) {
        userConsoleUI = new UserConsoleUI(in, connectionManager, sessionManager);
        productConsoleUI = new ProductConsoleUI(in, connectionManager, sessionManager);
        basketConsoleUI = new BasketConsoleUI(in, connectionManager, sessionManager);
        orderConsoleUI = new OrderConsoleUI(in, connectionManager, sessionManager);
    }

    public static void mainScreen(Scanner in) {
        System.out.println(separatorStr);
        System.out.println("Добро пожаловать");
        System.out.println("Выберите действие");
        System.out.println(separatorStr);

        if (!sessionManager.isUserLogged()) {
            System.out.println("0: Зарегестрироваться");
            System.out.println("1: Войти в профиль");
        }
        else {
            System.out.println("1: Посмотреть профиль");
        }

        System.out.println("2: Посмотреть товары");
        System.out.println("3: Поиск товара");
        System.out.println("4: Посмотреть корзину");
        System.out.println("5: Оформить заказ");
        System.out.println("6: Выйти из приложения");
        System.out.println(separatorStr);

        boolean isEnd = false;
        while (!isEnd) {
            switch (in.nextLine()) {
                case "0" -> {
                    if (!sessionManager.isUserLogged()) {
                        userConsoleUI.registrationForm();
                        isEnd = true;
                        mainScreen(in);
                    }
                    else {
                        System.out.println("Введено недопустимое значение");
                    }
                }

                case "1" -> {
                    if (sessionManager.isUserLogged()) {
                        profileScreen(in);
                    }
                    else {
                        userConsoleUI.loginForm();
                        mainScreen(in);
                    }

                    isEnd = true;
                }

                case "2" -> {
                    productConsoleUI.showAllProducts();
                    if (productConsoleUI.select(sessionManager.getCurrentProductsList().size())) {
                        if (sessionManager.isUserLogged()) {
                            basketConsoleUI.addProductToBasket();
                        }
                        else {
                            System.out.println("Вы не авторизованы");
                            System.out.println(separatorStr);
                        }
                    }

                    mainScreen(in);
                    isEnd = true;
                }

                case "3" -> {
                    if (productConsoleUI.search() && productConsoleUI.select(sessionManager.getCurrentProductsList().size())) {
                        if (sessionManager.isUserLogged()) {
                            basketConsoleUI.addProductToBasket();
                        }
                        else {
                            System.out.println("Вы не авторизованы");
                            System.out.println(separatorStr);
                        }
                    }
                    isEnd = true;
                    mainScreen(in);
                }

                case "4" -> {
                    if (sessionManager.isUserLogged()) {
                        basketScreen(in);
                        isEnd = true;
                    }
                    else {
                        System.out.println("Вы не авторизованы");
                        System.out.println(separatorStr);
                    }
                }

                case "5" -> {
                    if (sessionManager.isUserLogged()) {
                        userConsoleUI.showUserInfo();
                        basketConsoleUI.showBasket();
                        orderConsoleUI.makeOrder();
                        mainScreen(in);
                        isEnd = true;
                    }
                    else {
                        System.out.println("Вы не авторизованы");
                        System.out.println(separatorStr);
                    }
                }

                case "6" -> {
                    isEnd = true;
                }

                default -> {
                    System.out.println("Введено недопустимое значение");
                    System.out.println(separatorStr);
                }
            }
        }
    }

    public static void profileScreen(Scanner in) {
        userConsoleUI.showUserInfo();

        System.out.println("Выберите действие");
        System.out.println(separatorStr);

        System.out.println("1: Перейти в корзину");
        System.out.println("2: Пополнить баланс");
        System.out.println("3: Перейти на главный экран");
        System.out.println(separatorStr);
        
        boolean isEnd = false;

        while (!isEnd) {
            switch (in.nextLine()) {
                case "1" -> {
                    basketScreen(in);
                    isEnd = true;
                }

                case "2" -> {
                    userConsoleUI.topUpBalanceForm();
                    isEnd = true;
                    mainScreen(in);
                }

                case "3" -> {
                    mainScreen(in);
                    isEnd = true;
                }

                default -> {
                    System.out.println("Введено некорректное значение");
                }
            }
        }
    } 

    public static void basketScreen(Scanner in) {
        basketConsoleUI.showBasket();

        if (productConsoleUI.select(sessionManager.getCurrentProductsList().size())) {
            System.out.println(separatorStr);
            System.out.println("1: Добавить");
            System.out.println("2: Убрать");
            System.out.println("3: Выйти из корзины");

            String choise = in.nextLine();
            boolean isEnd = false;

            while (!isEnd) {
                switch (choise) {
                    case "1" -> {
                        basketConsoleUI.addProductToBasket();
                        isEnd = true;
                    }

                    case "2" -> {
                        basketConsoleUI.removeProductFromBasket();
                        isEnd = true;
                    }

                    case "3" -> {
                        isEnd = true;
                    }

                    default -> {
                        System.out.println("Введено недопустимое значение");
                    }
                }
            }
        }
        
        mainScreen(in);
    }

    public static void orderScreen(Scanner in) {
        basketConsoleUI.showBasket();
        System.out.println(separatorStr);

        System.out.println("Оформить заказ?");
        System.out.println("0: Оформить");
        System.out.println("1: Перейти в корзину");
        System.out.println("2: Перейти на главный экран");
        System.out.println(separatorStr);

        boolean isEnd = false;

        while (!isEnd) {
            switch (in.nextLine()) {
                case "0" -> {
                    
                }

                case "1" -> {
                    basketScreen(in);
                    isEnd = true;
                }

                case "2" -> {
                    mainScreen(in);
                    isEnd = true;
                }

                default -> {
                    System.out.println("Введено некорркетное значение");
                    System.out.println(separatorStr);
                }
            }
        }
    }
}