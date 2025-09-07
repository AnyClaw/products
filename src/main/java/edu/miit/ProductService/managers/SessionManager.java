package edu.miit.ProductService.managers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import edu.miit.ProductService.entities.Product;
import edu.miit.ProductService.entities.User;

public class SessionManager {
    private User currentUser;

    private List<Product> currentProductsList = new ArrayList<>();
    private Product selectedProduct;

    private List<Product> basket = new ArrayList<>();

    public BigDecimal getTotalCost() {
        BigDecimal totalCost = new BigDecimal(0);

        for (Product product : basket) {
            BigDecimal productCost = product.getCost();
            BigDecimal productAmmount = new BigDecimal(product.getAmount());

            totalCost = totalCost.add(productCost.multiply(productAmmount));
        }

        return totalCost;
    }

    public Product getProductFromBasket(Product product) {
        for (Product basketProduct : basket) {
            if (basketProduct.getName().equals(product.getName())) return basketProduct;
        }

        return null;
    }

    public boolean isUserLogged() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<Product> getCurrentProductsList() {
        return currentProductsList;
    }

    public void setCurrentProductsList(List<Product> currentProductsList) {
        this.currentProductsList = currentProductsList;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(int arrayId) {
        selectedProduct = currentProductsList.get(arrayId);
    }

    public List<Product> getBasket() {
        return basket;
    }

    public boolean isProductInBasket(Product product) {
        for (Product basketProduct : basket) {
            if (basketProduct.getName().equals(product.getName())) return true;
        }

        return false;
    }

    // добавляется при получении данных из бд
    public void addProductToBasket(Product product) {
        basket.add(product);
    }

    public void clearBasket() {
        basket = new ArrayList<>();
    }
}
