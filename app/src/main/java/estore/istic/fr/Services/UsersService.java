package estore.istic.fr.Services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Optional;

import estore.istic.fr.Facade.OnUserActionListener;
import estore.istic.fr.Resources.databaseHelper;

public class UsersService {

    public static void getUserName(OnUserActionListener listener) {
        databaseHelper.getDatabaseReference()
                .child("users")
                .child(Objects.requireNonNull(databaseHelper.getAuth().getCurrentUser()).getUid())
                .child("name")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            listener.onSuccess(Optional.ofNullable(snapshot.getValue(String.class)));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }
}
