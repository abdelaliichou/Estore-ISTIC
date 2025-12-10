package estore.istic.fr.Services;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
import estore.istic.fr.View.MainActivity;
import estore.istic.fr.View.signupActivity;

public class UsersService {

    public static void authenticateUser(
            String email,
            String password,
            OnUserActionListener listener
    ) {
        DatabaseHelper.getAuth().signInWithEmailAndPassword(
                email,
                password
        ).addOnCompleteListener(new OnCompleteListener<>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    listener.onSuccess("","","");
                    return;
                }

                listener.onError(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    public static void createUser(
            String name,
            String email,
            String password,
            String number,
            OnUserActionListener listener
    ) {
        DatabaseHelper.getAuth().createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                listener.onError(Objects.requireNonNull(task.getException()).getMessage());
                return;
            }

            // save the created user into the realtime database
            User user = new User(name, email, number);
            DatabaseHelper.getDatabaseReference()
                    .child("users")
                    .child(Objects.requireNonNull(DatabaseHelper.getAuth().getCurrentUser()).getUid())
                    .setValue(user)
                    .addOnCompleteListener(task1 -> {
                        if (!task1.isSuccessful()){
                            listener.onError(Objects.requireNonNull(task1.getException()).getMessage());
                            return;
                        }

                        listener.onSuccess("","","");
                    });
        });
    }

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
