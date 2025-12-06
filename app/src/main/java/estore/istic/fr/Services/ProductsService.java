package estore.istic.fr.Services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
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

    private static final String uid = Objects.requireNonNull(databaseHelper.getAuth().getCurrentUser()).getUid();

    private static ValueEventListener productsListener; // to stop listening when quiting the app

    public static void stopListening(String child) {
        DatabaseReference ref = databaseHelper.getDatabaseReference()
                .child(child)
                .child(uid);

        if (productsListener != null) ref.removeEventListener(productsListener);
    }

    public static void getAllProducts(OnGetProductsResultListener realtimeListener) {

        realtimeListener.onLoading();
        List<Product> allProducts = new ArrayList<>();
        Set<String> allFavoriteProductsIds = new HashSet<>();

        DatabaseReference productsRef = databaseHelper.getDatabaseReference().child("products");
        DatabaseReference favoritesRef = databaseHelper.getDatabaseReference().child("favorites").child(uid);

        // fetch all products
        productsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allProducts.clear();
                for (DataSnapshot product : snapshot.getChildren()) {
                    allProducts.add(product.getValue(Product.class));
                }

                // fetch favorites
                favoritesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot favSnapshot) {
                        allFavoriteProductsIds.clear();
                        for (DataSnapshot favoriteProduct : favSnapshot.getChildren()) {
                            allFavoriteProductsIds.add(Objects.requireNonNull(favoriteProduct.getValue(Product.class)).getProductId());
                        }

                        // map to ProductDto including favorite info
                        List<ProductDto> completeList = ProductMapper.toDtoList(allProducts)
                                .stream()
                                .peek(dto -> dto.setFavorite(allFavoriteProductsIds.contains(dto.getProduct().getProductId())))
                                .collect(Collectors.toList());

                        realtimeListener.onSuccess(completeList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        realtimeListener.onError(error.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                realtimeListener.onError(error.getMessage());
            }
        };

        productsRef.addValueEventListener(productsListener);
    }

    public static void addProductToFavorite(
            Product product,
            OnFavoriteProductsModifiedListener listener
    ) {
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
                        } else {
                            ref.push().setValue(product).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    listener.onSuccess(product.getName().concat(" added to favorite!"));
                                } else {
                                    listener.onError(Objects.requireNonNull(task.getException()).getMessage());
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

    public static void removeProductFromFavorite(
            Product product,
            OnFavoriteProductsModifiedListener listener
    ) {
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
                        } else {
                            for (DataSnapshot favSnap : snapshot.getChildren()) {
                                favSnap.getRef().removeValue().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        listener.onSuccess(product.getName().concat(" removed from favorites!"));
                                    } else {
                                        listener.onError(Objects.requireNonNull(task.getException()).getMessage());
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onError(error.getMessage());
                    }
                });

    }

    public static void filterProductsByName(
            String productName,
            OnGetProductsResultListener realtimeListener
    ) {

        realtimeListener.onLoading();

        List<Product> allProducts = new ArrayList<>();
        Set<String> allFavoriteProductsIds = new HashSet<>();

        DatabaseReference favoritesRef = databaseHelper.getDatabaseReference().child("favorites").child(uid);

        // fetch all products
        databaseHelper.getDatabaseReference()
                .child("products")
                .orderByChild("name")
                .equalTo(productName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            realtimeListener.onSuccess(Collections.emptyList());
                        } else {
                            allProducts.clear();
                            for (DataSnapshot product : snapshot.getChildren()) {
                                allProducts.add(product.getValue(Product.class));
                            }

                            // fetch favorites
                            favoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot favSnapshot) {
                                    allFavoriteProductsIds.clear();
                                    for (DataSnapshot favoriteProduct : favSnapshot.getChildren()) {
                                        allFavoriteProductsIds.add(Objects.requireNonNull(favoriteProduct.getValue(Product.class)).getProductId());
                                    }

                                    // map to ProductDto including favorite info
                                    List<ProductDto> completeList = ProductMapper.toDtoList(allProducts)
                                            .stream()
                                            .peek(dto -> dto.setFavorite(allFavoriteProductsIds.contains(dto.getProduct().getProductId())))
                                            .collect(Collectors.toList());

                                    realtimeListener.onSuccess(completeList);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    realtimeListener.onError(error.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        realtimeListener.onError(error.getMessage());
                    }
                });
    }

    public static void filterProductsByCategory(
            String categoryId,
            OnGetProductsResultListener realtimeListener
    ) {

        realtimeListener.onLoading();

        List<Product> allProducts = new ArrayList<>();
        Set<String> allFavoriteProductsIds = new HashSet<>();

        DatabaseReference favoritesRef = databaseHelper.getDatabaseReference().child("favorites").child(uid);

        // fetch all products
        databaseHelper.getDatabaseReference()
                .child("products")
                .orderByChild("categoryId")
                .equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            realtimeListener.onSuccess(Collections.emptyList());
                        } else {
                            allProducts.clear();
                            for (DataSnapshot product : snapshot.getChildren()) {
                                allProducts.add(product.getValue(Product.class));
                            }

                            // fetch favorites
                            favoritesRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot favSnapshot) {
                                    allFavoriteProductsIds.clear();
                                    for (DataSnapshot favoriteProduct : favSnapshot.getChildren()) {
                                        allFavoriteProductsIds.add(Objects.requireNonNull(favoriteProduct.getValue(Product.class)).getProductId());
                                    }

                                    // map to ProductDto including favorite info
                                    List<ProductDto> completeList = ProductMapper.toDtoList(allProducts)
                                            .stream()
                                            .peek(dto -> dto.setFavorite(allFavoriteProductsIds.contains(dto.getProduct().getProductId())))
                                            .collect(Collectors.toList());

                                    realtimeListener.onSuccess(completeList);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    realtimeListener.onError(error.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        realtimeListener.onError(error.getMessage());
                    }
                });
    }
}
