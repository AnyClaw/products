package edu.miit.ProductService.ui;

import java.util.List;
import java.util.Scanner;

import edu.miit.ProductService.entities.Product;
import edu.miit.ProductService.managers.ConnectionManager;
import edu.miit.ProductService.managers.SessionManager;
import edu.miit.ProductService.services.BasketService;

public class BasketConsoleUI {
    private final Scanner in;
    private final SessionManager sessionManager;
    private final BasketService basketService;

    private final String separatorStr = "---------------";

    public BasketConsoleUI(Scanner in, ConnectionManager connectionManager, SessionManager sessionManager) {
        this.in = in;
        this.sessionManager = sessionManager;
        basketService = new BasketService(connectionManager, sessionManager);
    }

    public boolean showBasket() {
        basketService.getBasket();
        
        System.out.println(separatorStr);

        if (sessionManager.isUserLogged()) {
            System.out.println(sessionManager.getCurrentUser().getName());
            System.out.println("Ваша корзина");
            System.out.println(separatorStr);

            List<Product> products = sessionManager.getBasket();
            System.out.println("[");
            for (int i = 0; i < products.size(); i++) {
                System.out.println((i + 1) + ": " + products.get(i));
            }
            System.out.println("]");
            System.out.println(separatorStr);

            System.out.println("Общая стоимость: " + sessionManager.getTotalCost());
            System.out.println(separatorStr);
            
            return true;
        }
        else {
            System.out.println("Вы не авторизованы");
            System.out.println(separatorStr);
            return false;
        }
    }

    public void addProductToBasket() {
        if (!sessionManager.isUserLogged()) {
            System.out.println("Вы не авторизованы");
            return;
        }

        basketService.getBasket();

        System.out.println(separatorStr);
        System.out.println("Введите количество");
        
        Long amount = null;
        Product product1 = sessionManager.getSelectedProduct();
        Product product2 = sessionManager.getProductFromBasket(product1);
        Long amountInBasket = product2 != null
            ? product1.getAmount() - product2.getAmount()
            : product1.getAmount();

        System.out.println(amountInBasket);

        do {
            if (amount != null) System.out.println("Введено недопустимое значение");

            try {
                amount = Long.parseLong(in.nextLine());
            }
            catch (NumberFormatException e) {
                amount = -1l;
            }
            System.out.println(separatorStr);
        } 
        while (amount < 0 || amount > amountInBasket);

        if (basketService.addProduct(amount)) {
            System.out.println("Товар добавлен");
        }
        else {
            System.out.println("Ошибка");
        }
    }

    public void removeProductFromBasket() {
        if (!sessionManager.isUserLogged()) {
            System.out.println("Вы не авторизованы");
            return;
        }

        System.out.println(separatorStr);
        System.out.println("Введите количество");
        
        Long amount = null;

        do {
            if (amount != null) System.out.println("Введено недопустимое значение");

            amount = Long.parseLong(in.nextLine());
            System.out.println(separatorStr);
        } 
        while (sessionManager.getSelectedProduct().getAmount() < amount || amount < 0);

        if (basketService.addProduct(-amount)) {
            System.out.println("Товар убран");
        }
        else {
            System.out.println("Ошибка");
        }
    }
}
