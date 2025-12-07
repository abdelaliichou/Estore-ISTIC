package estore.istic.fr.View;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import estore.istic.fr.Controller.CartAdapter;
import estore.istic.fr.Facade.OnCartAdapterListener;
import estore.istic.fr.Facade.OnGetOrderListener;
import estore.istic.fr.Facade.OnUserActionListener;
import estore.istic.fr.Model.Domain.Order;
import estore.istic.fr.Model.Domain.CartItem;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Utils;
import estore.istic.fr.Services.OrdersService;
import estore.istic.fr.Services.UsersService;

public class orderDetailsActivity extends AppCompatActivity implements OnCartAdapterListener {

    TextView emailText, dateText, orderTotalPriceText, orderProductsQuantityText, orderIdText;
    RecyclerView orderRecycler;
    CartAdapter orderAdapter;
    ProgressBar progressBar;
    MaterialCardView checkProcessButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Utils.statusAndActionBarIconsColor(this, R.id.main);

        initialisation();
        updateUI();

    }

    public void initialisation() {
        checkProcessButton = findViewById(R.id.process);
        progressBar = findViewById(R.id.progress);
        emailText = findViewById(R.id.email);
        dateText = findViewById(R.id.date);
        orderTotalPriceText = findViewById(R.id.products_total_price);
        orderProductsQuantityText = findViewById(R.id.total_products);
        orderIdText = findViewById(R.id.id);
        orderRecycler = findViewById(R.id.order_list);
    }

    public void onClicks(String orderID) {
        checkProcessButton.setOnClickListener(view -> {
            startActivity(
                    new Intent(
                            orderDetailsActivity.this,
                            trackingOrdersActivity.class
                    ).putExtra("orderID", orderID)
            );
        });
    }

    public void updateUI() {
        fetchUserEmail();
        fetchLastOrder(
                orderID -> {
                    orderIdText.setText(orderID);
                    onClicks(orderID);
                },
                orderDate -> dateText.setText(parseDate(orderDate)),
                orderPrice -> orderTotalPriceText.setText(String.valueOf(orderPrice)),
                orderProductsQuantity -> orderProductsQuantityText.setText(String.valueOf(orderProductsQuantity)),
                this::settingRecycler
        );
    }

    public String parseDate(Long date) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Instant instant = Instant.ofEpochMilli(date);
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return zdt.format(formatter);
        }

        Date fixedDate = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(fixedDate);
    }

    public void fetchLastOrder(
            Consumer<String> orderID,
            Consumer<Long> orderDate,
            Consumer<Double> orderPrice,
            Consumer<Integer> orderProductsQuantity,
            Consumer<List<CartItem>> orderProducts
    ) {
        OrdersService.getLastOrder(new OnGetOrderListener() {
            @Override
            public void onLoading() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(Optional<Order> lastOrder) {
                progressBar.setVisibility(View.GONE);

                // exposing the fetched data to the outside
                lastOrder.ifPresent(order -> {
                    orderID.accept(order.getOrderId());
                    orderDate.accept(order.getOrderDate());
                    orderPrice.accept(order.getTotalPrice());
                    orderProducts.accept(order.getItems());
                    orderProductsQuantity.accept(
                            order.getItems()
                                    .stream()
                                    .mapToInt(CartItem::getQuantity)
                                    .sum()
                    );
                });
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                showToast(message);
            }
        });
    }

    public void fetchUserEmail() {
        UsersService.getUserData(new OnUserActionListener() {
            @Override
            public void onSuccess(String userName, String userEmail, String phoneNumber) {
                emailText.setText(userEmail);
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    public void settingRecycler(List<CartItem> orderProducts) {
        orderAdapter = new CartAdapter(
                orderProducts,
                this,
                false,
                this
        );
        orderRecycler.setAdapter(orderAdapter);
        orderRecycler.setLayoutManager(new LinearLayoutManager(
                orderDetailsActivity.this,
                LinearLayoutManager.VERTICAL,
                false
        ));
    }

    @Override
    public void onProductLongClicked(CartItem item) {
    }

    public void showToast(String message) {
        Utils.showToast(this, message);
    }

}