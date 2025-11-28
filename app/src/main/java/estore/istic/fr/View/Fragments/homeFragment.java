package estore.istic.fr.View.Fragments;

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
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import estore.istic.fr.Resources.databaseHelper;
import estore.istic.fr.Services.CategoriesService;
import estore.istic.fr.Services.ProductsService;
import estore.istic.fr.Services.UsersService;

public class homeFragment extends Fragment implements OnProductActionListener, OnCategoryActionListener {

    ImageView searchImage, bigImage;
    public static ImageSlider imageSlider;
    TextView HeaderText, popularItemsText, secondHeaderText, CategoriesText, SuggestedItemsText, viewAllText1, viewAllText2, viewAllText3;
    MaterialCardView parent_slide_card;
    public static RecyclerView firstRecycler, secondRecycler, thirdRecycler;
    public static CategoriesAdapter categoriesAdapter;
    public static ProductsAdapter productsFirstAdapter, productsSecondAdapter;
    static View view;
    public static ProgressBar progressBar1, progressBar2;

    public homeFragment() {
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
        handlingOnClicks();
        SettingUpImageSlider();
        fetchProducts(view);
        fetchCategories(view);
        SettingAnimation();
        SettingHeaderTextUser();

        return view;
    }

    private void SettingAnimation() {
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
        /*
        imageSlider.setItemClickListener(i -> Utils.showToast(requireActivity().getApplication(), "Clicked on "));
        viewAllText1.setOnClickListener(view -> startActivity(new Intent(getActivity(), ViewAllCategoriesActivity.class)));
        viewAllText3.setOnClickListener(view -> startActivity(new Intent(getActivity(), ViewAllItemsActivity.class)));
        viewAllText2.setOnClickListener(view -> startActivity(new Intent(getActivity(), ViewAllItemsActivity.class)));
        searchImage.setOnClickListener(view -> startActivity(new Intent(getActivity(), SearchActivity.class)));*/
    }

    public void SettingUpImageSlider() {
        imageSlider.setImageList(
                Utils.getSlideList(),
                ScaleTypes.CENTER_CROP
        );
    }

    public void fetchProducts(View view) {
        ProductsService.getAllProducts(new OnGetProductsResultListener() {
            @Override
            public void onLoading() {
                progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(List<ProductDto> products) {
                settingProductsRecyclers(view, products);
            }

            @Override
            public void onError(String message) {
                progressBar2.setVisibility(View.GONE);
            }
        });
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
            public void onError(String message) {}
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
        firstRecycler.setAdapter(categoriesAdapter);
        firstRecycler.setLayoutManager(provideLayoutManager());
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
                partitionedProducts.get(true)
        );
        secondRecycler.setAdapter(productsSecondAdapter);
        secondRecycler.setLayoutManager((provideLayoutManager()));

        productsFirstAdapter = new ProductsAdapter(
                view.getContext(),
                this,
                partitionedProducts.get(false)
        );
        thirdRecycler.setAdapter(productsFirstAdapter);
        thirdRecycler.setLayoutManager(provideLayoutManager());
    }

    public RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(
                view.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
    }

    public void SettingHeaderTextUser() {
        UsersService.getUserName(userName -> userName.ifPresent(msg -> {
            HeaderText.setText("Welcome, ".concat(userName.get()).concat("\n").concat(HeaderText.getText().toString()));
        }));
    }

    @Override
    public void onCategoryClicked(Category category, int position) {}

    @Override
    public void onProductClicked(ProductDto product) {
        /*
        Intent intent = new Intent(getActivity(), ItemMoreInfomationsActivity.class);
        intent.putExtra("name", product.getProduct().getName());
        intent.putExtra("price", product.getProduct().getPrice());
        intent.putExtra("imageurl", product.getProduct().getImageUrl());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
    }

    @Override
    public void onProductLiked(Product product, int position) {
        ProductsService.addProductToFavorite(product, new OnFavoriteProductsModifiedListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                // notify the adapter
                if (product.getPrice() >= 600) {
                    productsSecondAdapter.onProductAddedToFavorite(position, true);
                    return;
                }
                productsFirstAdapter.onProductAddedToFavorite(position, true);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onProductUnliked(Product product, int position) {
        ProductsService.removeProductFromFavorite(product, new OnFavoriteProductsModifiedListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                // notify the adapter
                if (product.getPrice() >= 600) {
                    productsSecondAdapter.onProductAddedToFavorite(position, false);
                    return;
                }
                productsFirstAdapter.onProductAddedToFavorite(position, false);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}