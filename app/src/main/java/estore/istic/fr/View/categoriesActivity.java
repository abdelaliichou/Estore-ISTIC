package estore.istic.fr.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import estore.istic.fr.Controller.CategoriesAdapter;
import estore.istic.fr.Facade.OnCategoriesResultListener;
import estore.istic.fr.Facade.OnCategoryActionListener;
import estore.istic.fr.Model.Domain.Category;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Utils;
import estore.istic.fr.Services.CategoriesService;

public class categoriesActivity extends AppCompatActivity implements OnCategoryActionListener {

    CategoriesAdapter categoriesAdapter;
    RecyclerView categoriesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_categories);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Utils.statusAndActionBarIconsColor(this, R.id.main);

        initialisation();
        fetchCategories(this);

    }

    public void initialisation() {
        categoriesRecycler = findViewById(R.id.AllCategories_recycler);
    }

    public void fetchCategories(Context context) {
        CategoriesService.getAllCategories(new OnCategoriesResultListener() {
            @Override
            public void onLoading() {
            }

            @Override
            public void onSuccess(List<Category> categories) {
                settingCategoriesRecycler(context, categories);
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    public void settingCategoriesRecycler(
            Context context,
            List<Category> categories
    ) {
        categoriesAdapter = new CategoriesAdapter(
                context,
                this,
                categories,
                true
        );
        categoriesRecycler.setAdapter(categoriesAdapter);
        categoriesRecycler.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public void showToast(String message) {
        Utils.showToast(this, message);
    }

    @Override
    public void onCategoryClicked(Category category) {
        Intent intent = new Intent(categoriesActivity.this, productsByCategoryActivity.class);
        intent.putExtra("id", category.getCategoryId());
        intent.putExtra("name", category.getName());
        intent.putExtra("imageUrl", category.getImageUrl());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}