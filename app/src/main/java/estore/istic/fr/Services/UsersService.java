package estore.istic.fr.Services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Optional;

import estore.istic.fr.Facade.OnUserActionListener;
import estore.istic.fr.Model.Domain.User;
import estore.istic.fr.Resources.databaseHelper;

public class UsersService {

    public static void getUserData(OnUserActionListener listener) {
        databaseHelper.getDatabaseReference()
                .child("users")
                .child(Objects.requireNonNull(databaseHelper.getAuth().getCurrentUser()).getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Optional<User> user = Optional.ofNullable(snapshot.getValue(User.class));
                        if (user.isPresent()) {
                            listener.onSuccess(
                                    user.get().getName(),
                                    user.get().getEmail(),
                                    user.get().getPhoneNumber()
                            );
                        } else {
                            listener.onError("Error while fetching user info");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onError("Error while fetching user info");
                    }
                });
    }
}
