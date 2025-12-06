package estore.istic.fr.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import estore.istic.fr.Model.Domain.Category;
import estore.istic.fr.Model.Domain.Product;
import estore.istic.fr.Model.Dto.ProductDto;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Utils;
import estore.istic.fr.Services.ProductsService;

public class productsByCategoryActivity extends AppCompatActivity implements OnProductActionListener{

    TextView headerText;
    RecyclerView productsRecyclerView;
    ProductsAdapter productsAdapter;
    ShimmerFrameLayout shimmerFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_products_by_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Utils.statusAndActionBarIconsColor(this, R.id.main);

        initialisation();
        settingProductsRecyclers(this, Collections.emptyList());
        fetchProducts();

    }

    public void initialisation() {
        productsRecyclerView = findViewById(R.id.category_items_recycler);
        headerText = findViewById(R.id.category_name);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        headerText.setText(loadCategory().getName());
    }

    public Category loadCategory() {
        Category category = new Category(
                getIntent().getStringExtra("name"),
                getIntent().getStringExtra("imageUrl")
        );
        category.setCategoryId(getIntent().getStringExtra("id"));
        return category;
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
        productsRecyclerView.setAdapter(productsAdapter);
        productsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public void fetchProducts() {
        ProductsService.filterProductsByCategory(loadCategory().getCategoryId(), new OnGetProductsResultListener() {
            @Override
            public void onLoading() {
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                productsRecyclerView.setVisibility(View.GONE);
                shimmerFrameLayout.startShimmer();
            }

            @Override
            public void onSuccess(List<ProductDto> products) {
                productsRecyclerView.setVisibility(View.VISIBLE);
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();

                productsAdapter.updateList(products);
            }

            @Override
            public void onError(String message) {
                productsRecyclerView.setVisibility(View.VISIBLE);
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();
                showToast(message);
            }
        });
    }

    @Override
    public void onProductClicked(ProductDto product) {
        Intent intent = new Intent(productsByCategoryActivity.this, productDetailsActivity.class);
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