package estore.istic.fr.View;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import estore.istic.fr.Facade.OnCartActionListener;
import estore.istic.fr.Facade.OnFavoriteProductsModifiedListener;
import estore.istic.fr.Model.Domain.Product;
import estore.istic.fr.Model.Dto.ProductDto;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Utils;
import estore.istic.fr.Services.OrdersService;
import estore.istic.fr.Services.ProductsService;

public class productDetailsActivity extends AppCompatActivity {

    RelativeLayout addToCard ;
    ImageView productImage, fullHeart, emptyHeart;
    MaterialCardView addImage, removeImage , color1, color2, color3, color4, color5;
    TextView productNameTextView, productPriceTextView, quantityTextView;
    int totalQuantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Utils.statusAndActionBarIconsColor(this, R.id.main);

        initialisation();
        chargeProductData();
        handlingOnClicks(this);
        handlingHeartClicks(this);

    }

    public void initialisation() {
        addToCard = findViewById(R.id.add_to_card);
        productImage = findViewById(R.id.image_product);
        productNameTextView = findViewById(R.id.info);
        quantityTextView = findViewById(R.id.somme);
        removeImage = findViewById(R.id.minus_button);
        addImage = findViewById(R.id.add_button);
        color1 = findViewById(R.id.color1);
        color2 = findViewById(R.id.color2);
        color3 = findViewById(R.id.color3);
        color4 = findViewById(R.id.color4);
        color5 = findViewById(R.id.color5);
        fullHeart = findViewById(R.id.heart_fill);
        emptyHeart = findViewById(R.id.heart_empty);
        productPriceTextView = findViewById(R.id.total_somme);
    }

    public ProductDto loadProduct() {
        ProductDto dto = new ProductDto(
                new Product(
                        getIntent().getStringExtra("name"),
                        Double.parseDouble(Objects.requireNonNull(getIntent().getStringExtra("price"))),
                        getIntent().getStringExtra("imageUrl")
                ),
                Boolean.parseBoolean(getIntent().getStringExtra("isFavorite"))
        );
        dto.getProduct().setProductId(getIntent().getStringExtra("id"));
        return dto;
    }

    public void chargeProductData() {
        Picasso.get().load(loadProduct().getProduct().getImageUrl()).into(productImage);
        productNameTextView.setText(loadProduct().getProduct().getName());
        productPriceTextView.setText("$".concat(String.valueOf(loadProduct().getProduct().getPrice())));
        if (loadProduct().isFavorite()) {
            addToFavoritesHeartsUI();
        } else {
            removeFromFavoritesHeartsUI();
        }
    }

    public void addToFavoritesHeartsUI() {
        emptyHeart.setVisibility(View.GONE);
        fullHeart.setVisibility(View.VISIBLE);
    }

    public void removeFromFavoritesHeartsUI() {
        emptyHeart.setVisibility(View.VISIBLE);
        fullHeart.setVisibility(View.GONE);
    }

    public void handlingHeartClicks(Context context) {
        emptyHeart.setOnClickListener(view -> {
            if (emptyHeart.getVisibility() == View.GONE) return;
            addToFavorite(context, loadProduct().getProduct());
        });
        fullHeart.setOnClickListener(view -> {
            if (fullHeart.getVisibility() == View.GONE) return;
            removeFromFavorite(context, loadProduct().getProduct());
        });
    }

    public void addToFavorite(Context context, Product product) {
        ProductsService.addProductToFavorite(product, new OnFavoriteProductsModifiedListener() {
            @Override
            public void onSuccess(String message) {
                Utils.showToast(context, message);
                addToFavoritesHeartsUI();
            }

            @Override
            public void onError(String message) {
                Utils.showToast(context, message);
                removeFromFavoritesHeartsUI();
            }
        });
    }

    public void removeFromFavorite(Context context, Product product) {
        ProductsService.removeProductFromFavorite(product, new OnFavoriteProductsModifiedListener() {
            @Override
            public void onSuccess(String message) {
                Utils.showToast(context, message);
                removeFromFavoritesHeartsUI();
            }

            @Override
            public void onError(String message) {
                Utils.showToast(context, message);
                addToFavoritesHeartsUI();
            }
        });
    }

    public void handlingOnClicks(Context context) {
        addToCard.setOnClickListener(view -> addProductToCart(context));
        addImage.setOnClickListener(view -> {
            totalQuantity++;
            quantityTextView.setText(String.valueOf(totalQuantity));
        });
        removeImage.setOnClickListener(view -> {
            if (totalQuantity > 1) {
                totalQuantity--;
                quantityTextView.setText(String.valueOf(totalQuantity));
            }
        });
        List<MaterialCardView> colorCards = Arrays.asList(color1, color2, color3, color4, color5);
        colorCards.forEach( colorCard -> {
            colorCard.setOnClickListener(v -> {
                colorCards.forEach( otherCard -> {
                    if (colorCard == otherCard) {
                        otherCard.setStrokeColor(getResources().getColor(R.color.colorlast));
                        otherCard.setStrokeWidth(5);
                    } else {
                        otherCard.setStrokeWidth(0);
                    }
                });
            });
        });
    }

    public void addProductToCart(Context context){
        OrdersService.addProductToCart(loadProduct().getProduct(), totalQuantity, new OnCartActionListener() {
            @Override
            public void onSuccess(String message) {
                Utils.showToast(context, loadProduct().getProduct().getName().concat(" added to card !"));
                finish();
            }

            @Override
            public void onError(String error) {
                Utils.showToast(context, error);
            }
        });
    }
}
