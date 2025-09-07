package edu.miit.ProductService.services;

import java.util.List;

import edu.miit.ProductService.entities.Product;
import edu.miit.ProductService.managers.ConnectionManager;
import edu.miit.ProductService.managers.SessionManager;
import edu.miit.ProductService.repositories.ProductRepository;

public class ProductService {
    private final SessionManager sessionManager;
    private final ProductRepository productRepository;

    public ProductService(ConnectionManager connectionManager, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        productRepository = new ProductRepository(connectionManager); 
    }
    public List<Product> getCurrenProductsList() {
        return sessionManager.getCurrentProductsList();
    }

    public List<Product> getAllProducts() {
        sessionManager.setCurrentProductsList(productRepository.findAllProducts());
        return sessionManager.getCurrentProductsList();
    }

    public List<Product> search(String name) {
        sessionManager.setCurrentProductsList(productRepository.findProductsByName(name));
        return sessionManager.getCurrentProductsList();
    }

    public String selectProduct(int arrayId) {
        if (arrayId >= sessionManager.getCurrentProductsList().size() || arrayId < 0)
            return "Введено недопустимое значение";

        sessionManager.setSelectedProduct(arrayId);

        return sessionManager.getSelectedProduct().toString();
    }
}
