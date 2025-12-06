package estore.istic.fr.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import estore.istic.fr.Controller.ProductsAdapter;
import estore.istic.fr.Facade.OnFavoriteProductsModifiedListener;
import estore.istic.fr.Facade.OnGetProductsResultListener;
import estore.istic.fr.Facade.OnProductActionListener;
import estore.istic.fr.Model.Domain.Product;
import estore.istic.fr.Model.Dto.ProductDto;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Utils;
import estore.istic.fr.Services.ProductsService;

public class productsActivity extends AppCompatActivity implements OnProductActionListener {
    ProductsAdapter productsAdapter;
    RecyclerView productsRecycler;
    AutoCompleteTextView searchField;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_products);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Utils.statusAndActionBarIconsColor(this, R.id.main);

        initialisation();
        settingProductsRecyclers(
                this,
                Collections.emptyList()
        );
        fetchProducts();

    }

    public void initialisation() {
        productsRecycler = findViewById(R.id.AllCategories_recycler);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        searchField = findViewById(R.id.complete_text);
    }

    public void settingProductsRecyclers(
            Context context,
            List<ProductDto> products
    ) {
        productsAdapter = new ProductsAdapter(
                context,
                this,
                products,
                true
        );
        productsRecycler.setAdapter(productsAdapter);
        productsRecycler.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public void fetchProducts() {
        ProductsService.getAllProducts(new OnGetProductsResultListener() {
            @Override
            public void onLoading() {
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                shimmerFrameLayout.startShimmer();
            }

            @Override
            public void onSuccess(List<ProductDto> products) {
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();
                productsAdapter.updateList(products);

                // autocomplete logic
                autoCompleteSearch(
                        products.stream()
                        .map(ProductDto::getProduct)
                        .map(Product::getName)
                        .collect(Collectors.toList())
                );
            }

            @Override
            public void onError(String message) {
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();
                showToast(message);
            }
        });
    }

    public void autoCompleteSearch(List<String> productNames) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                productsActivity.this,
                android.R.layout.simple_list_item_1,
                productNames
        );
        searchField.setAdapter(arrayAdapter);
        searchField.setOnItemClickListener((adapterView, view, i, l) -> {
            autoCompleteSearch(searchField.getText().toString().trim());
        });
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) fetchProducts();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void autoCompleteSearch(String productName) {
        ProductsService.filterProductsByName(productName, new OnGetProductsResultListener() {
            @Override
            public void onLoading() {
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                shimmerFrameLayout.startShimmer();
            }

            @Override
            public void onSuccess(List<ProductDto> products) {
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();

                if (products.isEmpty()) {
                    showToast("No products found !");
                    return;
                }
                productsAdapter.updateList(products);
            }

            @Override
            public void onError(String message) {
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();
            }
        });
    }

    @Override
    public void onProductClicked(ProductDto product) {
        Intent intent = new Intent(productsActivity.this, productDetailsActivity.class);
        intent.putExtra("name", product.getProduct().getName());
        intent.putExtra("id", product.getProduct().getProductId());
        intent.putExtra("imageUrl", product.getProduct().getImageUrl());
        intent.putExtra("isFavorite", String.valueOf(product.isFavorite()));
        intent.putExtra("price", String.valueOf(product.getProduct().getPrice()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onProductLiked(Product product) {
        ProductsService.addProductToFavorite(product, new OnFavoriteProductsModifiedListener() {
            @Override
            public void onSuccess(String message) {
                showToast(message);
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    @Override
    public void onProductDisliked(Product product) {
        ProductsService.removeProductFromFavorite(product, new OnFavoriteProductsModifiedListener() {
            @Override
            public void onSuccess(String message) {
                showToast(message);
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    public void showToast(String message) {
        Utils.showToast(this, message);
    }
}