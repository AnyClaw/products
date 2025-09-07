package edu.miit.ProductService.services;

import java.util.ArrayList;
import java.util.List;

import edu.miit.ProductService.entities.BasketItem;
import edu.miit.ProductService.entities.Product;
import edu.miit.ProductService.managers.ConnectionManager;
import edu.miit.ProductService.managers.SessionManager;
import edu.miit.ProductService.repositories.BasketRepository;
import edu.miit.ProductService.repositories.ProductRepository;

public class BasketService {
    private final SessionManager sessionManager;
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;

    public BasketService(ConnectionManager connectionManager, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        basketRepository = new BasketRepository(connectionManager);
        productRepository = new ProductRepository(connectionManager);
    }

    public void getBasket() {
        if (!sessionManager.isUserLogged()) return;

        List<BasketItem> items = basketRepository.findBasketItemsByUserId(sessionManager.getCurrentUser());
        List<Product> basket = new ArrayList<>();
        sessionManager.clearBasket();

        for (BasketItem item : items) {
            Product product = productRepository.findProductById(item.productId).get();
            product.setAmount(item.getAmount());
            basket.add(product);
            sessionManager.addProductToBasket(product);
        }

        sessionManager.setCurrentProductsList(basket);
    }

    public boolean addProduct(Long amount) {
        Product product = sessionManager.getSelectedProduct();
        getBasket();

        if (sessionManager.isProductInBasket(product)) {
            product = new Product(
                product.getId(), product.getName(),
                sessionManager.getProductFromBasket(product).getAmount() + amount, 
                product.getCost()
            );
        }
        else {
            product = new Product(
                product.getId(), product.getName(), amount, product.getCost()
            );
        }

        System.out.println(product);

        return product.getAmount() != 0 
            ? basketRepository.updateBasket(
                sessionManager.getCurrentUser().getId(), 
                product.getId(), 
                product.getAmount()
            )
            : basketRepository.deleteProduct(
                sessionManager.getCurrentUser().getId(), 
                product.getId()
            );
    }
}
