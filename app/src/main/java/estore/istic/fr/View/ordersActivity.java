package estore.istic.fr.View;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import estore.istic.fr.Controller.OrdersAdapter;
import estore.istic.fr.Facade.OnGetAllOrdersListener;
import estore.istic.fr.Facade.OnOrderActionListener;
import estore.istic.fr.Model.Domain.Order;
import estore.istic.fr.Model.Dto.OrderStatus;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Utils;
import estore.istic.fr.Services.OrdersService;

public class ordersActivity extends AppCompatActivity implements OnOrderActionListener {

    RecyclerView ordersRecycler;
    OrdersAdapter ordersAdapter;
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_orders);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Utils.statusAndActionBarIconsColor(this, R.id.main);

        initialisation();
        fetchOrders();

    }

    public void initialisation(){
        progressBar = findViewById(R.id.progress);
        ordersRecycler = findViewById(R.id.order_list);
    }


    public void fetchOrders() {
        OrdersService.getAllOrder(new OnGetAllOrdersListener() {
            @Override
            public void onLoading() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(List<Order> orders) {
                progressBar.setVisibility(View.GONE);
                settingRecycler(orders);
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                showToast(message);
            }
        });
    }

    public void settingRecycler(List<Order> orders){
        ordersAdapter = new OrdersAdapter(orders, this, this);
        ordersRecycler.setAdapter(ordersAdapter);
        ordersRecycler.setLayoutManager(new LinearLayoutManager(
                ordersActivity.this,
                LinearLayoutManager.VERTICAL,
                false
        ));
    }

    @Override
    public void onOrderClicked(Order order) {
        Intent intent = new Intent(this, orderDetailsActivity.class);
        // we want a specific order done by this user
        intent.putExtra("isLast", false);
        intent.putExtra("id", order.getOrderId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onOrderStatus(
            RelativeLayout parent,
            TextView status,
            Order order
    ) {

        int pendingCardColor = this.getResources().getColor(R.color.pendingLite);
        int pendingColor = this.getResources().getColor(R.color.pending);
        int confirmedCardColor = this.getResources().getColor(R.color.confirmedLite);
        int confirmedColor = this.getResources().getColor(R.color.confirmed);
        int onProcessCardColor = this.getResources().getColor(R.color.onProcessLite);
        int onProcessColor = this.getResources().getColor(R.color.onProcess);
        int shippedCardColor = this.getResources().getColor(R.color.shippedLite);
        int shippedColor = this.getResources().getColor(R.color.shipped);
        int deliveredCardColor = this.getResources().getColor(R.color.deliveredLite);
        int deliveredColor = this.getResources().getColor(R.color.delivered);
        int canceledCardColor = this.getResources().getColor(R.color.canceledLite);
        int canceledColor = this.getResources().getColor(R.color.canceled);

        int deliveryStatus = Utils.getOrderStatus(order.getStatus());

        switch (deliveryStatus) {
            case 0 : {
                status.setTextColor(pendingColor);
                status.setText(OrderStatus.PENDING.name());
                parent.setBackgroundColor(pendingCardColor);
                break;
            }
            case 1 : {
                parent.setBackgroundColor(confirmedCardColor);
                status.setTextColor(confirmedColor);
                status.setText(OrderStatus.CONFIRMED.name());
                break;
            }
            case 2 : {
                parent.setBackgroundColor(onProcessCardColor);
                status.setTextColor(onProcessColor);
                status.setText(OrderStatus.ON_PROCESS.name());
                break;
            }
            case 3 : {
                parent.setBackgroundColor(shippedCardColor);
                status.setTextColor(shippedColor);
                status.setText(OrderStatus.SHIPPED.name());
                break;
            }
            case 4 : {
                parent.setBackgroundColor(deliveredCardColor);
                status.setTextColor(deliveredColor);
                status.setText(OrderStatus.DELIVERED.name());
                break;
            }
            case -1 : {
                parent.setBackgroundColor(canceledCardColor);
                status.setTextColor(canceledColor);
                status.setText(OrderStatus.CANCELED.name());
                break;
            }
        }
    }

    public void showToast(String message) {
        Utils.showToast(this, message);
    }

}