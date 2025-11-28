package estore.istic.fr.Services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import estore.istic.fr.Facade.OnCategoriesResultListener;
import estore.istic.fr.Model.Domain.Category;
import estore.istic.fr.Resources.databaseHelper;


public class CategoriesService {

    public static void getAllCategories(OnCategoriesResultListener listener) {

        listener.onLoading();
        List<Category> list = new ArrayList<>();

        databaseHelper.getDatabaseReference()
                .child("categories")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot category : snapshot.getChildren()) {
                            list.add(category.getValue(Category.class));
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
