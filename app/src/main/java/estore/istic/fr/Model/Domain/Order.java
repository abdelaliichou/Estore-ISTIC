package estore.istic.fr.Model.Domain;

import com.google.firebase.database.PropertyName;

import java.util.List;

import estore.istic.fr.Model.Dto.OrderStatus;

public class Order {
    String orderID;
    @PropertyName("user_id")
    private String userId;
    private OrderStatus status;
    @PropertyName("order_date")
    private long orderDate;
    @PropertyName("total_price")
    private double totalPrice;
    private List<OrderItem> items;

    public Order(){}

    public Order(
            String userId,
            double totalPrice,
            List<OrderItem> items
    ) {
        this.items = items;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = OrderStatus.PENDING;
        this.orderDate = System.currentTimeMillis();
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public long getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(long timestamp) {
        this.orderDate = timestamp;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
