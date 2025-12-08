package estore.istic.fr.Services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import estore.istic.fr.Facade.OnUserActionListener;
import estore.istic.fr.Model.Domain.User;
import estore.istic.fr.Resources.DatabaseHelper;

public class UsersService {

    public static void getUserData(OnUserActionListener listener) {
        DatabaseHelper.getDatabaseReference()
                .child("users")
                .child(Objects.requireNonNull(DatabaseHelper.getAuth().getCurrentUser()).getUid())
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

    public static void updateUserData(String userName, String phoneNumber, OnUserActionListener listener) {

        Map<String, Object> updatedUser = new HashMap<>();
        updatedUser.put("name", userName);
        updatedUser.put("phoneNumber", phoneNumber);

        DatabaseHelper.getDatabaseReference()
                .child("users")
                .child(Objects.requireNonNull(DatabaseHelper.getAuth().getCurrentUser()).getUid())
                .updateChildren(updatedUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess("Profile updated successfully !","","");
                    } else {
                        listener.onError(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }
}
