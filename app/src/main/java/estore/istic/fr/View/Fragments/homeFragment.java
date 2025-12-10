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
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.google.android.material.card.MaterialCardView;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import estore.istic.fr.Controller.CategoriesAdapter;
import estore.istic.fr.Controller.ProductsAdapter;
import estore.istic.fr.Facade.OnCategoriesResultListener;
import estore.istic.fr.Facade.OnCategoryActionListener;
import estore.istic.fr.Facade.OnFavoriteProductsModifiedListener;
import estore.istic.fr.Facade.OnProductActionListener;
import estore.istic.fr.Facade.OnGetProductsResultListener;
import estore.istic.fr.Facade.OnUserActionListener;
import estore.istic.fr.Model.Domain.Category;
import estore.istic.fr.Model.Domain.Product;
import estore.istic.fr.Model.Dto.ProductDto;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Animations;
import estore.istic.fr.Resources.Utils;
import estore.istic.fr.Services.CategoriesService;
import estore.istic.fr.Services.ProductsService;
import estore.istic.fr.Services.UsersService;
import estore.istic.fr.View.categoriesActivity;
import estore.istic.fr.View.productDetailsActivity;
import estore.istic.fr.View.productsActivity;

public class homeFragment extends Fragment implements OnProductActionListener, OnCategoryActionListener {

    ImageView bigImage;
    ImageSlider imageSlider;
    TextView HeaderText, popularItemsText, categoriesText, SuggestedItemsText, viewAllText1, viewAllText2, viewAllText3;
    MaterialCardView parent_slide_card;
    RecyclerView categoriesRecycler, populaireProductsRecycler, allProductsRecycler;
    CategoriesAdapter categoriesAdapter;
    ProductsAdapter allProductsAdapter, populaireProductsAdapter;
    View view;
    ProgressBar populaireProductsProgressBar, allProductsProgressBar, categoriesProgressBar;

    private Optional<Context> safeContext;

    public homeFragment() {}

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
        ProductsService.stopListening("products");
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        initialisation(view);

        settingAnimation();
        settingImageSlider();
        settingHeaderTextUser();
        settingProductsRecyclers(view, Collections.emptyList());

        fetchCategories(view);
        fetchProducts();

        handlingOnClicks();

        return view;
    }

    private void settingAnimation() {
        Animations.FromeRightToLeftCard(parent_slide_card);
        Animations.FromeLeftToRight(HeaderText);
        Animations.FromeLeftToRight(popularItemsText);
        Animations.FromeLeftToRight(categoriesText);
        Animations.FromeLeftToRight(SuggestedItemsText);
        Animations.FromRightToLeft1(viewAllText1);
        Animations.FromRightToLeft1(viewAllText2);
        Animations.FromRightToLeft1(viewAllText3);
    }

    public void initialisation(View view) {
        bigImage = view.findViewById(R.id.second_big_image);
        populaireProductsProgressBar = view.findViewById(R.id.populaire_items_progress);
        allProductsProgressBar = view.findViewById(R.id.all_items_progress);
        categoriesProgressBar = view.findViewById(R.id.categories_progress);
        HeaderText = view.findViewById(R.id.header_text);
        popularItemsText = view.findViewById(R.id.popular_items_text);
        categoriesText = view.findViewById(R.id.category_text);
        SuggestedItemsText = view.findViewById(R.id.suggested_items_text);
        viewAllText1 = view.findViewById(R.id.first_view_all_text);
        viewAllText2 = view.findViewById(R.id.second_view_all_text);
        viewAllText3 = view.findViewById(R.id.third_view_all_text);
        imageSlider = view.findViewById(R.id.slider);
        parent_slide_card = view.findViewById(R.id.parent_card);
        categoriesRecycler = view.findViewById(R.id.first_recycler);
        populaireProductsRecycler = view.findViewById(R.id.second_recyclerview);
        allProductsRecycler = view.findViewById(R.id.third_recycler);
    }

    public void handlingOnClicks() {
        viewAllText3.setOnClickListener(view -> startActivity(new Intent(getActivity(), productsActivity.class)));
        viewAllText2.setOnClickListener(view -> startActivity(new Intent(getActivity(), productsActivity.class)));
        viewAllText1.setOnClickListener(view -> startActivity(new Intent(getActivity(), categoriesActivity.class)));
    }

    public void settingImageSlider() {
        imageSlider.setImageList(
                Utils.getSlideList(),
                ScaleTypes.CENTER_CROP
        );
    }

    public void fetchCategories(View view) {
        CategoriesService.getAllCategories(new OnCategoriesResultListener() {
            @Override
            public void onLoading() {
                categoriesProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(List<Category> categories) {
                categoriesProgressBar.setVisibility(View.GONE);
                settingCategoriesRecycler(view, categories);
            }

            @Override
            public void onError(String message) {
                categoriesProgressBar.setVisibility(View.GONE);
                showToast(message);
            }
        });
    }

    public void settingCategoriesRecycler(
            View view,
            List<Category> categories
    ) {
        categoriesAdapter = new CategoriesAdapter(
                view.getContext(),
                this,
                categories,
                false
        );
        categoriesRecycler.setLayoutManager(provideLayoutManager());
        categoriesRecycler.setAdapter(categoriesAdapter);
    }

    public void fetchProducts() {
        ProductsService.getAllProducts(new OnGetProductsResultListener() {
            @Override
            public void onLoading() {
                populaireProductsProgressBar.setVisibility(View.VISIBLE);
                allProductsProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(List<ProductDto> products) {
                populaireProductsProgressBar.setVisibility(View.GONE);
                allProductsProgressBar.setVisibility(View.GONE);
                Map<Boolean, List<ProductDto>> partitionedProducts = products.stream().collect(
                        Collectors.partitioningBy(p -> p.getProduct().getPrice() >= 600)
                );

                // notify the wright adapter
                populaireProductsAdapter.updateList(partitionedProducts.get(true));
                allProductsAdapter.updateList(partitionedProducts.get(false));
            }

            @Override
            public void onError(String message) {
                showToast(message);
                populaireProductsProgressBar.setVisibility(View.GONE);
                allProductsProgressBar.setVisibility(View.GONE);
            }
        });
    }

    public void settingProductsRecyclers(
            View view,
            List<ProductDto> products
    ) {

        Map<Boolean, List<ProductDto>> partitionedProducts = products.stream().collect(
                Collectors.partitioningBy(p -> p.getProduct().getPrice() >= 600)
        );

        populaireProductsAdapter = new ProductsAdapter(
                view.getContext(),
                this,
                partitionedProducts.get(true),
                false
        );
        populaireProductsRecycler.setLayoutManager(provideLayoutManager());
        populaireProductsRecycler.setAdapter(populaireProductsAdapter);

        allProductsAdapter = new ProductsAdapter(
                view.getContext(),
                this,
                partitionedProducts.get(false),
                false
        );
        allProductsRecycler.setLayoutManager(provideLayoutManager());
        allProductsRecycler.setAdapter(allProductsAdapter);
    }

    public RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(
                view.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
    }

    public void settingHeaderTextUser() {
        UsersService.getUserData(new OnUserActionListener() {
            @Override
            public void onSuccess(String userName, String userEmail, String phoneNumber) {
                HeaderText.setText("Welcome, ".concat(userName).concat("\n").concat(HeaderText.getText().toString()));
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    @Override
    public void onCategoryClicked(Category category) {
        ProductsService.filterProductsByCategory(category.getCategoryId(), new OnGetProductsResultListener() {
            @Override
            public void onLoading() {
                populaireProductsProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(List<ProductDto> products) {
                populaireProductsProgressBar.setVisibility(View.GONE);
                if (products.isEmpty()) {
                    showToast("No products found !");
                    return;
                }
                populaireProductsAdapter.updateList(products);
            }

            @Override
            public void onError(String message) {
                populaireProductsProgressBar.setVisibility(View.GONE);
            }
        });
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
        safeContext.ifPresent(context -> {
            Utils.showToast(context, message);
        });
    }

}