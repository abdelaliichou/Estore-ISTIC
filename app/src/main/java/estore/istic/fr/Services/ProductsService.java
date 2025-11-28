package estore.istic.fr.Services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import estore.istic.fr.Facade.OnFavoriteProductsModifiedListener;
import estore.istic.fr.Facade.OnGetProductsResultListener;
import estore.istic.fr.Model.Domain.Product;
import estore.istic.fr.Model.Dto.ProductDto;
import estore.istic.fr.Model.Mappers.ProductMapper;
import estore.istic.fr.Resources.databaseHelper;


public class ProductsService {

    public static void getAllProducts(OnGetProductsResultListener listener) {

        listener.onLoading();
        List<Product> allProducts = new ArrayList<>();
        Set<String> allFavoriteProductsIds = new HashSet<>();

        String uid = Objects.requireNonNull(databaseHelper.getAuth().getCurrentUser()).getUid();
        DatabaseReference productsRef = databaseHelper.getDatabaseReference().child("products");
        DatabaseReference favoritesRef = databaseHelper.getDatabaseReference().child("favorites").child(uid);

        // fetch all products
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot product : snapshot.getChildren()) {
                    allProducts.add(product.getValue(Product.class));
                }

                // fetch favorites
                favoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot favSnapshot) {
                        for (DataSnapshot favoriteProduct : favSnapshot.getChildren()) {
                            allFavoriteProductsIds.add(Objects.requireNonNull(favoriteProduct.getValue(Product.class)).getProductId());
                        }

                        // map to ProductDto including favorite info
                        List<ProductDto> completeList = ProductMapper.toDtoList(allProducts)
                                .stream()
                                .peek(dto -> dto.setFavorite(allFavoriteProductsIds.contains(dto.getProduct().getProductId())))
                                .collect(Collectors.toList());

                        listener.onSuccess(completeList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onError(error.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.getMessage());
            }
        });
    }

    public static void addProductToFavorite(
            Product product,
            OnFavoriteProductsModifiedListener listener
    ) {
        String uid = Objects.requireNonNull(databaseHelper.getAuth().getCurrentUser()).getUid();
        DatabaseReference ref = databaseHelper
                .getDatabaseReference()
                .child("favorites")
                .child(uid);

        ref.orderByChild("productId")
                .equalTo(product.getProductId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            listener.onSuccess(product.getName().concat(" already favorite!"));
                            return;
                        }

                        ref.push().setValue(product).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                listener.onSuccess(product.getName().concat(" added to favorite!"));
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onError(error.getMessage());
                    }
                });
    }

    public static void removeProductFromFavorite(
            Product product,
            OnFavoriteProductsModifiedListener listener
    ) {
        String uid = Objects.requireNonNull(databaseHelper.getAuth().getCurrentUser()).getUid();
        DatabaseReference ref = databaseHelper
                .getDatabaseReference()
                .child("favorites")
                .child(uid);

        ref.orderByChild("productId")
                .equalTo(product.getProductId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (!snapshot.exists()) {
                            listener.onSuccess(product.getName().concat(" is not in favorites!"));
                            return;
                        }

                        for (DataSnapshot favSnap : snapshot.getChildren()) {
                            favSnap.getRef().removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    listener.onSuccess(product.getName().concat(" removed from favorites!"));
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onError(error.getMessage());
                    }
                });

    }
}
