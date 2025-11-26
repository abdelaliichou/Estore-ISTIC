package estore.istic.fr.Model.Domain;

import com.google.firebase.firestore.PropertyName;

public class OrderItem {
    @PropertyName("product_id")
    private String productId;

    @PropertyName("product_name")
    private String productName;

    @PropertyName("product_price")
    private double priceAtPurchase;

    private int quantity;

    public OrderItem() {}

    // converting Cart -> Order
    public OrderItem(
            Product product,
            int quantity
    ) {
        this.quantity = quantity;
        this.productName = product.getName();
        this.productId = product.getProductId();
        this.priceAtPurchase = product.getPrice();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(double priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
