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
import estore.istic.fr.View.productDetailsActivity;
import estore.istic.fr.View.productsActivity;

public class homeFragment extends Fragment implements OnProductActionListener, OnCategoryActionListener {

    ImageView searchImage, bigImage;
    ImageSlider imageSlider;
    TextView HeaderText, popularItemsText, secondHeaderText, CategoriesText, SuggestedItemsText, viewAllText1, viewAllText2, viewAllText3;
    MaterialCardView parent_slide_card;
    RecyclerView firstRecycler, secondRecycler, thirdRecycler;
    CategoriesAdapter categoriesAdapter;
    ProductsAdapter productsFirstAdapter, productsSecondAdapter;
    View view;
    ProgressBar progressBar1, progressBar2;

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

        Utils.statusAndActionBarIconsColor(getActivity(), R.id.main);

        initialisation(view);

        settingAnimation();
        settingImageSlider();
        settingHeaderTextUser();
        settingProductsRecyclers(
                view,
                Collections.emptyList()
        );

        fetchCategories(view);
        fetchProducts();

        handlingOnClicks();

        return view;
    }

    private void settingAnimation() {
        Animations.FromeRightToLeftCard(parent_slide_card);
        Animations.FromeLeftToRight(HeaderText);
        Animations.FromeLeftToRight(popularItemsText);
        Animations.FromeLeftToRight(CategoriesText);
        Animations.FromeLeftToRight(SuggestedItemsText);
        Animations.FromRightToLeft1(viewAllText1);
        Animations.FromRightToLeft1(viewAllText2);
        Animations.FromRightToLeft1(viewAllText3);
        Animations.FromeLeftToRight1(secondHeaderText);
    }

    public void initialisation(View view) {
        bigImage = view.findViewById(R.id.second_big_image);
        searchImage = view.findViewById(R.id.search);
        progressBar1 = view.findViewById(R.id.populaire_items_progress);
        progressBar2 = view.findViewById(R.id.all_items_progress);
        progressBar2.setVisibility(View.VISIBLE);
        HeaderText = view.findViewById(R.id.header_text);
        popularItemsText = view.findViewById(R.id.popular_items_text);
        secondHeaderText = view.findViewById(R.id.second_header);
        CategoriesText = view.findViewById(R.id.category_text);
        SuggestedItemsText = view.findViewById(R.id.suggested_items_text);
        viewAllText1 = view.findViewById(R.id.first_view_all_text);
        viewAllText2 = view.findViewById(R.id.second_view_all_text);
        viewAllText3 = view.findViewById(R.id.third_view_all_text);
        imageSlider = view.findViewById(R.id.slider);
        parent_slide_card = view.findViewById(R.id.parent_card);
        firstRecycler = view.findViewById(R.id.first_recycler);
        secondRecycler = view.findViewById(R.id.second_recyclerview);
        thirdRecycler = view.findViewById(R.id.third_recycler);
    }

    public void handlingOnClicks() {
        viewAllText3.setOnClickListener(view -> startActivity(new Intent(getActivity(), productsActivity.class)));
        viewAllText2.setOnClickListener(view -> startActivity(new Intent(getActivity(), productsActivity.class)));
        /*
        imageSlider.setItemClickListener(i -> Utils.showToast(requireActivity().getApplication(), "Clicked on "));
        viewAllText1.setOnClickListener(view -> startActivity(new Intent(getActivity(), ViewAllCategoriesActivity.class)));
        searchImage.setOnClickListener(view -> startActivity(new Intent(getActivity(), SearchActivity.class)));*/
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
            public void onLoading() {}

            @Override
            public void onSuccess(List<Category> categories) {
                settingCategoriesRecycler(view, categories);
            }

            @Override
            public void onError(String message) {
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
                categories
        );
        firstRecycler.setLayoutManager(provideLayoutManager());
        firstRecycler.setAdapter(categoriesAdapter);
    }

    public void fetchProducts() {
        ProductsService.getAllProducts(new OnGetProductsResultListener() {
            @Override
            public void onLoading() {
                progressBar2.setVisibility(View.VISIBLE);
                progressBar1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(List<ProductDto> products) {
                progressBar1.setVisibility(View.GONE);
                progressBar2.setVisibility(View.GONE);
                Map<Boolean, List<ProductDto>> partitionedProducts = products.stream().collect(
                        Collectors.partitioningBy(p -> p.getProduct().getPrice() >= 600)
                );

                // notify the wright adapter
                productsSecondAdapter.updateList(partitionedProducts.get(true));
                productsFirstAdapter.updateList(partitionedProducts.get(false));
            }

            @Override
            public void onError(String message) {
                showToast(message);
                progressBar1.setVisibility(View.GONE);
                progressBar2.setVisibility(View.GONE);
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

        productsSecondAdapter = new ProductsAdapter(
                view.getContext(),
                this,
                partitionedProducts.get(true),
                false
        );
        secondRecycler.setLayoutManager(provideLayoutManager());
        secondRecycler.setAdapter(productsSecondAdapter);

        productsFirstAdapter = new ProductsAdapter(
                view.getContext(),
                this,
                partitionedProducts.get(false),
                false
        );
        thirdRecycler.setLayoutManager(provideLayoutManager());
        thirdRecycler.setAdapter(productsFirstAdapter);
    }

    public RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(
                view.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
    }

    public void settingHeaderTextUser() {
        UsersService.getUserName(new OnUserActionListener() {
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
    public void onCategoryClicked(Category category, int position) {}

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