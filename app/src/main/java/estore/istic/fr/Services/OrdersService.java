package estore.istic.fr.Services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import estore.istic.fr.Facade.OnGetProductsResultListener;
import estore.istic.fr.Model.Domain.Product;
import estore.istic.fr.Model.Dto.ProductDto;
import estore.istic.fr.Resources.databaseHelper;


public class OrdersService {

    public static void getAllProducts(OnGetProductsResultListener listener) {

        listener.onLoading();
        ArrayList<ProductDto> list = new ArrayList<>();

        databaseHelper.getDatabaseReference()
                .child("products")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot product : snapshot.getChildren()) {
                            list.add(new ProductDto(
                                        product.getValue(Product.class),
                                        false
                                    )
                            );
                        }

                        listener.onSuccess(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onError(error.getMessage());
                    }
                });
    }
}
