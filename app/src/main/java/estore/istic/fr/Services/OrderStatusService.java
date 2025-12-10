package estore.istic.fr.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.Optional;

import estore.istic.fr.Model.Domain.Order;
import estore.istic.fr.Resources.DatabaseHelper;
import estore.istic.fr.Resources.NotificationUtils;

public class OrderStatusService extends Service {
    private DatabaseReference ordersRef;
    private ChildEventListener orderListener;

    @Override
    public void onCreate() {
        super.onCreate();

        // Ensure user is logged in
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            stopSelf();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Point to: orders -> userID
        // We listen to the whole folder of this user
        ordersRef = DatabaseHelper.getDatabaseReference()
                .child("orders")
                .child(userId);

        startListening();
    }

    private void startListening() {
        orderListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Only use this if you want to notify "New Order Placed".
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // It fires only when data inside an order changes.
                Optional<Order> order = Optional.ofNullable(snapshot.getValue(Order.class));
                order.ifPresent(o -> {
                    String orderId = o.getOrderId();
                    String newStatus = o.getStatus().toString();
                    NotificationUtils.createNotification(
                            getApplicationContext(),
                            "Order Update",
                            "Order #" + orderId + " is now: " + newStatus
                    );
                });
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Order deleted (optional to handle)
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Order priority changed (ignore)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("OrderService", "Database error: " + error.getMessage());
            }
        };

        ordersRef.addChildEventListener(orderListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Keeps the service running as long as possible
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove listener to save battery/data when service dies
        if (ordersRef != null && orderListener != null) {
            ordersRef.removeEventListener(orderListener);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
