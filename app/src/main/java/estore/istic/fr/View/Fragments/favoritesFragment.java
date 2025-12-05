package estore.istic.fr.View.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
import estore.istic.fr.View.productDetailsActivity;


public class favoritesFragment extends Fragment implements OnProductActionListener {

    SwipeRefreshLayout refresh;
    ProductsAdapter favoriteProductsAdapter;
    RecyclerView favoriteProductsRecycler;
    ProgressBar progressBar;

    private Optional<Context> safeContext;

    public favoritesFragment() {}

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
        ProductsService.stopListening("favorites");
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        Utils.statusAndActionBarIconsColor(getActivity(), R.id.main);

        initialisation(view);
        settingProductsRecyclers(view, Collections.emptyList());
        fetchProducts(view);
        refresh(view);

        return view;
    }

    public void refresh(View view) {
        refresh.setOnRefreshListener(() -> {
            fetchProducts(view);
            refresh.setRefreshing(false);
        });
    }

    public void initialisation(View view) {
        refresh = view.findViewById(R.id.refresh);
        progressBar = view.findViewById(R.id.Favorite_progress);
        favoriteProductsRecycler = view.findViewById(R.id.Favorite_items);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void fetchProducts(View view) {
        ProductsService.getAllProducts(new OnGetProductsResultListener() {
            @Override
            public void onLoading() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(List<ProductDto> products) {

                // filter by favorites
                List<ProductDto> favorites = products.stream()
                        .filter(ProductDto::isFavorite)
                        .collect(Collectors.toList());

                favoriteProductsAdapter.updateList(favorites);
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                showToast(message);
            }
        });
    }

    public void settingProductsRecyclers(
            View view,
            List<ProductDto> products
    ) {
        favoriteProductsAdapter = new ProductsAdapter(
                view.getContext(),
                this,
                products,
                true
        );
        favoriteProductsRecycler.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        favoriteProductsRecycler.setAdapter(favoriteProductsAdapter);
    }

    @Override
    public void onProductClicked(ProductDto product) {
        Intent intent = new Intent(getActivity(), productDetailsActivity.class);
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
    public void onProductUnliked(Product product) {
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
        safeContext.ifPresent(context -> {
                    Utils.showToast(context, message);
        });
    }

}