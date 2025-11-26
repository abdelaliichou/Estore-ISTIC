package estore.istic.fr.Model.Dto;

import estore.istic.fr.Model.Domain.Product;

public class CartItem {
    private Product product;
    private int quantity;

    public CartItem() {}

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
