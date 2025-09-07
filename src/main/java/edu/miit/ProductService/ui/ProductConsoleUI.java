package edu.miit.ProductService.ui;

import java.util.List;
import java.util.Scanner;

import edu.miit.ProductService.entities.Product;
import edu.miit.ProductService.managers.ConnectionManager;
import edu.miit.ProductService.managers.SessionManager;
import edu.miit.ProductService.services.ProductService;

public class ProductConsoleUI {
    private final Scanner in;
    private final ProductService productService;

    private final String separatorStr = "---------------";

    public ProductConsoleUI(Scanner in, ConnectionManager connectionManager, SessionManager sessionManager) {
        this.in = in;
        productService = new ProductService(connectionManager, sessionManager);
    }

    public void showAllProducts() {
        System.out.println(separatorStr);

        List<Product> products = productService.getAllProducts();

        if (products.size() == 0) {
            System.out.println("Товаров в данный момент нет");
            System.out.println(separatorStr);
            return;
        }

        System.out.println("Вот наши товары");
        System.out.println(separatorStr);

        for (int i = 0; i < products.size(); i++) {
            System.out.println((i + 1) + ") " + products.get(i));
        }
        System.out.println(separatorStr);
    }

    public boolean search() {
        String name;
        List<Product> products;

        System.out.println(separatorStr);
        System.out.println("Поиск по названию");
        name = in.nextLine();
        System.out.println(separatorStr);

        products = productService.search(name);

        if (products.size() == 0) {
            System.out.println("Товаров с таким именем не найдено");
            return false;
        }

        System.out.println("Найдено " + products.size() + " товаров");
        System.out.println(separatorStr);

        for (int i = 0; i < products.size(); i++) {
            System.out.println((i + 1) + ") " + products.get(i));
        }
        System.out.println(separatorStr);

        return true;
    }

    public boolean select(long productListSize) {
        System.out.println(separatorStr);
        if (productListSize != 0) System.out.println("1-" + productListSize + ": Выберите товар");
        System.out.println("0: Выйти на главный экран");
        System.out.println(separatorStr);

        String message = null;

        do {
            if (message != null) System.out.println(message);

            int arrayId;

            try {
                arrayId = Integer.parseInt(in.nextLine());
            }
            catch (NumberFormatException e) {
                message = "Введено недопустимое значение";
                continue;
            }

            if (arrayId == 0) return false;
            message = productService.selectProduct(arrayId - 1);
        }
        while (message.equals("Введено недопустимое значение"));

        System.out.println(separatorStr);
        System.out.println(message);
        System.out.println(separatorStr);

        return !message.equals("Товар не найден") && 
            !message.equals("Введено недопустимое значение");
    }
}
