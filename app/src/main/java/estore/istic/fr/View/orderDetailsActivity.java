package estore.istic.fr.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import java.util.Optional;
import java.util.function.Consumer;

import estore.istic.fr.Controller.CartAdapter;
import estore.istic.fr.Facade.OnCartAdapterListener;
import estore.istic.fr.Facade.OnGetOrderListener;
import estore.istic.fr.Model.Domain.Order;
import estore.istic.fr.Model.Domain.CartItem;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Utils;
import estore.istic.fr.Services.OrdersService;

public class orderDetailsActivity extends AppCompatActivity implements OnCartAdapterListener {

    TextView dateText, orderTotalPriceText, orderProductsQuantityText, orderIdText, statusText;
    RecyclerView orderRecycler;
    CartAdapter orderAdapter;
    ProgressBar progressBar;
    MaterialCardView checkProcessButton;
    RelativeLayout statusParent;

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
        Utils.setup(orderDetailsActivity.this, "Order Details", true);

        initialisation();
        updateUI();

    }

    public void initialisation() {
        checkProcessButton = findViewById(R.id.process);
        progressBar = findViewById(R.id.progress);
        dateText = findViewById(R.id.date);
        orderTotalPriceText = findViewById(R.id.products_total_price);
        orderProductsQuantityText = findViewById(R.id.total_products);
        orderIdText = findViewById(R.id.id);
        orderRecycler = findViewById(R.id.order_list);
        statusText = findViewById(R.id.orderStatus);
        statusParent = findViewById(R.id.orderStatusParent);
    }

    public void updateUI() {
        checkOrderType();
    }

    public void checkOrderType() {
        boolean isLastOrder = getIntent().getBooleanExtra("isLast", true);
        if (isLastOrder) {
            // pass the order id received from the exposed value
            fetchLastOrder(this::onClicks);
            return;
        }

        // pass the order id from received from the intent
        String orderId = getIntent().getStringExtra("id");
        fetchOrder(orderId);
        onClicks(orderId);
    }

    public void fetchLastOrder(Consumer<String> orderId) {
        OrdersService.getLastOrder(new OnGetOrderListener() {
            @Override
            public void onLoading() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(Optional<Order> lastOrder) {
                progressBar.setVisibility(View.GONE);
                lastOrder.ifPresent(order -> {

                    settingRecycler(order.getItems());

                    orderIdText.setText(order.getOrderId());
                    dateText.setText(Utils.parseDate(order.getOrderDate()));
                    orderTotalPriceText.setText(Utils.noDollarFormat.format(order.getTotalPrice()));
                    orderProductsQuantityText.setText(String.valueOf(
                            order.getItems()
                                    .stream()
                                    .mapToInt(CartItem::getQuantity)
                                    .sum()
                    ));

                    updateStatus(order);

                    // expose orderId because in this case we haven't receive order is by intent
                    orderId.accept(order.getOrderId());
                });
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                showToast(message);
            }
        });
    }

    public void fetchOrder(String orderId) {
        OrdersService.getOrderById(orderId, new OnGetOrderListener() {
            @Override
            public void onLoading() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(Optional<Order> lastOrder) {
                progressBar.setVisibility(View.GONE);
                lastOrder.ifPresent(order -> {

                    settingRecycler(order.getItems());

                    orderIdText.setText(order.getOrderId());
                    dateText.setText(Utils.parseDate(order.getOrderDate()));
                    orderTotalPriceText.setText(Utils.noDollarFormat.format(order.getTotalPrice()));
                    orderProductsQuantityText.setText(String.valueOf(
                            order.getItems()
                                    .stream()
                                    .mapToInt(CartItem::getQuantity)
                                    .sum()
                    ));

                    updateStatus(order);
                });
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                showToast(message);
            }
        });
    }

    public void updateStatus(Order order) {
        Utils.handlingOrderStatus(
                this,
                statusParent,
                statusText,
                order
        );
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
    public void onProductLongClicked(CartItem item) {}

    public void showToast(String message) {
        Utils.showToast(this, message);
    }

}