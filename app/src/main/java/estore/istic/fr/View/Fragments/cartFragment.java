package estore.istic.fr.View.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import estore.istic.fr.Controller.CartAdapter;
import estore.istic.fr.Facade.OnCartAdapterListener;
import estore.istic.fr.Facade.OnCartActionListener;
import estore.istic.fr.Facade.OnCartRealTimeListener;
import estore.istic.fr.Facade.OnOrderSaveListener;
import estore.istic.fr.Model.Domain.Order;
import estore.istic.fr.Model.Domain.CartItem;
import estore.istic.fr.Resources.DatabaseHelper;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Utils;
import estore.istic.fr.Services.OrdersService;
import estore.istic.fr.View.orderDetailsActivity;

public class cartFragment extends Fragment implements OnCartAdapterListener {

    RelativeLayout payButton;
    RecyclerView card_recycler;
    ImageView emptyImage;
    CartAdapter adapter;
    TextView totalPrice, totalItems;
    AlertDialog dialog;
    ProgressBar progressBar;

    private Optional<Context> safeContext;

    public cartFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        safeContext = Optional.of(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        safeContext = Optional.empty();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        OrdersService.stopListening();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        initialisation(view);
        settingRecycler(view.getContext(), Collections.emptyList());
        listeningToCartItems();
        onClicks();

        return view;
    }

    public void initialisation(View view) {
        emptyImage = view.findViewById(R.id.empty);
        payButton = view.findViewById(R.id.pay);
        progressBar = view.findViewById(R.id.card_progress);
        card_recycler = view.findViewById(R.id.Card_items);
        totalItems = view.findViewById(R.id.product_number);
        totalPrice = view.findViewById(R.id.products_total_price);
    }

    public void listeningToCartItems() {
        OrdersService.getCartItems(new OnCartRealTimeListener() {
            @Override
            public void onLoading() {
                emptyImage.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onData(List<CartItem> cartItems) {
                updateUI(cartItems);
                progressBar.setVisibility(View.GONE);
                emptyImage.setVisibility(cartItems.isEmpty() ? View.VISIBLE : View.GONE);

                adapter.updateList(cartItems);
            }

            @Override
            public void onError(String message) {
                showToast(message);
                emptyImage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void updateUI(List<CartItem> cartItems) {
        totalPrice.setText(Utils.noDollarFormat.format(getTotalPrice(cartItems)));
        totalItems.setText(String.valueOf(getTotalQuantity(cartItems)));
    }

    public double getTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToDouble(CartItem::calculateTotalPrice)
                .sum();
    }

    public int getTotalQuantity(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public void settingRecycler(Context context, List<CartItem> cartItems) {
        adapter = new CartAdapter(
                cartItems,
                context,
                true,
                this
        );
        card_recycler.setAdapter(adapter);
        card_recycler.setLayoutManager(new LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
        ));
    }

    public void onClicks() {
        payButton.setOnClickListener(view -> {
            if (adapter.getCartItems().isEmpty()) {
                showToast("Cart is empty !");
                return;
            }

            confirmationDialog();
        });
    }

    public void confirmationDialog() {
        Utils.createActionDialog(
                getActivity(),
                "Order confirmation !",
                "You want to send this order to the store ?",
                true,
                this::sendOrder,
                () -> {}
        );
    }

    public void sendOrder() {
        dialog = Utils.createLoadingDialog(
                getActivity(),
                "Wait a minute please !",
                "Your order is being send to the store !"
        );
        registerOrder(adapter.getCartItems());
    }

    public void registerOrder(List<CartItem> cartItems) {
        String uid = Objects.requireNonNull(DatabaseHelper.getAuth().getCurrentUser()).getUid();
        Order order = new Order(
                uid,
                getTotalPrice(cartItems),
                cartItems
        );

        OrdersService.saveOrder(order, new OnOrderSaveListener() {
            @Override
            public void onLoading() {
                showToast("Processing the order... ");
            }

            @Override
            public void onSuccess(String orderId) {
                dialog.dismiss();
                bottomSheetDialog();
                OrdersService.clearCart(uid);
            }

            @Override
            public void onError(String message) {
                dialog.dismiss();
                showToast(message);
            }
        });
    }

    public void bottomSheetDialog() {
        Utils.createBottomSheet(
                requireActivity(),
                "Thank you !\nYour order is on the way",
                "Check process ",
                R.drawable.congratulations,
                () -> {
                    startActivity(new Intent(
                                    getActivity(),
                                    orderDetailsActivity.class
                            ).putExtra("isLast", true)
                    );
                }
        );
    }

    @Override
    public void onProductLongClicked(CartItem item) {
        Utils.createActionDialog(
                getActivity(),
                "Delete item !",
                "Do you want to delete this product from your card ?",
                true,
                () -> removeItemFromCart(item),
                () -> {}
        );
    }

    public void removeItemFromCart(CartItem item) {
        OrdersService.deleteCartItem(item, new OnCartActionListener() {
            @Override
            public void onSuccess(String message) {
                showToast(message);
            }

            @Override
            public void onError(String error) {
                showToast(error);
            }
        });
    }

    public void showToast(String message) {
        safeContext.ifPresent(context -> {
            Utils.showToast(context, message);
        });
    }
}