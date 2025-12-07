package estore.istic.fr.Services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import estore.istic.fr.Facade.OnCartActionListener;
import estore.istic.fr.Facade.OnCartRealTimeListener;
import estore.istic.fr.Facade.OnGetOrderListener;
import estore.istic.fr.Facade.OnOrderSaveListener;
import estore.istic.fr.Model.Domain.Order;
import estore.istic.fr.Model.Domain.Product;
import estore.istic.fr.Model.Domain.CartItem;
import estore.istic.fr.Resources.databaseHelper;


public class OrdersService {

    private static final String uid = Objects.requireNonNull(databaseHelper.getAuth().getCurrentUser()).getUid();
    private static ValueEventListener listener; // to stop listening when quiting the app

    public static void stopListening() {
        DatabaseReference ref = databaseHelper.getDatabaseReference()
                .child("cart")
                .child(uid);
        if (listener != null) ref.removeEventListener(listener);
    }

    public static void getCartItems(OnCartRealTimeListener realtimeListener) {
        realtimeListener.onLoading();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CartItem> items = new ArrayList<>();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Optional<CartItem> item = Optional.ofNullable(itemSnapshot.getValue(CartItem.class));
                    item.ifPresent(items::add);
                }

                realtimeListener.onData(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                realtimeListener.onError(error.getMessage());
            }
        };

        databaseHelper.getDatabaseReference()
                .child("cart")
                .child(uid)
                .addValueEventListener(listener);
    }

    public static void addProductToCart(Product product, int quantity, OnCartActionListener listener) {
        DatabaseReference ref = databaseHelper.getDatabaseReference()
                .child("cart")
                .child(uid);

        CartItem cartItem = new CartItem(product, quantity);
        ref.orderByChild("product/productId")
                .equalTo(product.getProductId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // Product already in cart → update quantity
                        if (snapshot.exists()) {
                            for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                                Optional<CartItem> existingItem = Optional.ofNullable(itemSnapshot.getValue(CartItem.class));
                                existingItem.ifPresent(item -> {
                                    int newQuantity = existingItem.get().getQuantity() + quantity;
                                    ref.child(Objects.requireNonNull(itemSnapshot.getKey()))
                                            .child("quantity")
                                            .setValue(newQuantity)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    listener.onSuccess(product.getName() + " quantity updated in cart!");
                                                } else {
                                                    listener.onError(Objects.requireNonNull(task.getException()).getMessage());
                                                }
                                            });
                                });
                            }
                        } else {
                            // Product not in cart → add new
                            ref.push().setValue(cartItem).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    listener.onSuccess(product.getName() + " added to cart!");
                                } else {
                                    listener.onError(Objects.requireNonNull(task.getException()).getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onError(error.getMessage());
                    }
                });
    }

    public static void saveOrder(Order order, OnOrderSaveListener listener) {
        DatabaseReference ref = databaseHelper.getDatabaseReference()
                .child("orders")
                .child(order.getUserId());

        Optional<String> orderId = Optional.ofNullable(ref.push().getKey());
        if (orderId.isEmpty()) {
            listener.onError("Failed to save order!");
            return;
        }

        listener.onLoading();
        order.setOrderId(orderId.get());
        ref.child(orderId.get())
                .setValue(order)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess(orderId.get());
                    } else {
                        listener.onError(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public static void getLastOrder(OnGetOrderListener listener) {
        String uid = Objects.requireNonNull(databaseHelper.getAuth().getCurrentUser()).getUid();
        DatabaseReference ref = databaseHelper.getDatabaseReference()
                .child("orders")
                .child(uid);

        listener.onLoading();
        ref.orderByChild("orderDate")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (!snapshot.exists()) {
                            listener.onSuccess(Optional.empty());
                            return;
                        }

                        for (DataSnapshot orderSnap : snapshot.getChildren()) {
                            try {
                                Optional<Order> order = Optional.ofNullable(orderSnap.getValue(Order.class));
                                listener.onSuccess(order);
                            } catch (DatabaseException e) {
                                listener.onError("Could not fetch order delivery status");
                            }
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onError(error.getMessage());
                    }
                });
    }

    public static void tracOrderDeliveryState(String orderID, OnOrderSaveListener listener) {
        databaseHelper.getDatabaseReference()
                .child("orders")
                .child(Objects.requireNonNull(databaseHelper.getAuth().getCurrentUser()).getUid())
                .orderByChild("orderId")
                .equalTo(orderID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (!snapshot.exists()) {
                            listener.onError("No order found!");
                            return;
                        }

                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                            Optional<Order> order = Optional.ofNullable(orderSnapshot.getValue(Order.class));
                            if (order.isPresent()) {
                                listener.onSuccess(order.get().getStatus().toString());
                                return;
                            } else {
                                listener.onError("No order found!");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onError(error.getMessage());
                    }
                });
    }

    public static void clearCart(String uid) {
        databaseHelper.getDatabaseReference()
                .child("cart")
                .child(uid)
                .removeValue();
    }

    public static void deleteCartItem(CartItem cartItem, OnCartActionListener listener) {

        DatabaseReference ref = databaseHelper.getDatabaseReference()
                .child("cart")
                .child(uid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                boolean removed = false;
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {

                    Optional<CartItem> item = Optional.ofNullable(itemSnapshot.getValue(CartItem.class));
                    if (item.isPresent() && item.get().getProduct().getProductId().equals(cartItem.getProduct().getProductId())) {
                        ref.child(Objects.requireNonNull(itemSnapshot.getKey())).removeValue((error, databaseReference) -> {
                            if (error == null) {
                                listener.onSuccess(cartItem.getProduct().getName().concat(" removed from cart!"));
                            } else {
                                listener.onError(error.getMessage());
                            }
                        });
                        removed = true;
                        break;
                    }
                }

                if (!removed) {
                    listener.onError(cartItem.getProduct().getName().concat(" not found in cart!"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.getMessage());
            }
        });
    }
}
