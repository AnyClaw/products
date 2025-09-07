package edu.miit.ProductService.services;

import java.math.BigDecimal;
import java.util.Optional;

import edu.miit.ProductService.entities.Product;
import edu.miit.ProductService.entities.User;
import edu.miit.ProductService.managers.ConnectionManager;
import edu.miit.ProductService.managers.SessionManager;
import edu.miit.ProductService.repositories.OrderRepository;

public class OrderService {
    private final SessionManager sessionManager;
    private final OrderRepository orderRepository;

    public OrderService(ConnectionManager connectionManager, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        orderRepository = new OrderRepository(connectionManager);
    }

    public String makeOrder() {
        BigDecimal totalCost = sessionManager.getTotalCost();
        BigDecimal balance = sessionManager.getCurrentUser().getBalance();

        if (sessionManager.getBasket().isEmpty()) {
            return "Корзина пуста";
        }

        if (balance.compareTo(totalCost) == -1) {
            return "Недостаточно средств";
        }
        
        User user = sessionManager.getCurrentUser();

        Optional<User> updatedUser = orderRepository.updateBalance(user.getId(), balance.subtract(totalCost));

        if (updatedUser.isPresent()) {
            sessionManager.setCurrentUser(updatedUser.get());
            user = sessionManager.getCurrentUser();
        }

        orderRepository.deleteBasketItems(user.getId());

        for (Product product : sessionManager.getBasket()) {
            orderRepository.updateProduct(product.getId(), product.getAmount());
        }

        return "Заказ совершён";
    }
}
