package estore.istic.fr.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import estore.istic.fr.Controller.OrdersAdapter;
import estore.istic.fr.Facade.OnGetAllOrdersListener;
import estore.istic.fr.Facade.OnOrderActionListener;
import estore.istic.fr.Model.Domain.Order;
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

    public void showToast(String message) {
        Utils.showToast(this, message);
    }

}