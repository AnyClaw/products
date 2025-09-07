package edu.miit.ProductService.ui;

import java.util.Scanner;

import edu.miit.ProductService.managers.ConnectionManager;
import edu.miit.ProductService.managers.SessionManager;
import edu.miit.ProductService.services.OrderService;

public class OrderConsoleUI {
    private final Scanner in;
    private final OrderService orderService;

    private final String separatorStr = "---------------";

    public OrderConsoleUI(Scanner in, ConnectionManager connectionManager, SessionManager sessionManager) {
        this.in = in;
        orderService = new OrderService(connectionManager, sessionManager);
    }

    public void makeOrder() {
        System.out.println(separatorStr);
        System.out.println("Вы уверены?");
        System.out.println("1: Да");
        System.out.println("Любой символ: Нет");
        System.out.println(separatorStr);

        if (in.nextLine().equals("1")) {
            System.out.println(separatorStr);
            System.out.println(orderService.makeOrder());
        }

        System.out.println(separatorStr);
    }
}
