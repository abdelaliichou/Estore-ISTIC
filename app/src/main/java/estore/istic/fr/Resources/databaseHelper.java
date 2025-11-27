package estore.istic.fr.Resources;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Optional;

public class databaseHelper {
    private static DatabaseReference databaseReference;
    private static FirebaseAuth auth;

    private databaseHelper() { }

    public static DatabaseReference getDatabaseReference() {
        if (Optional.ofNullable(databaseReference).isEmpty()) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("ISTIC");
        }
        return databaseReference;
    }

    public static FirebaseAuth getAuth() {
        if (Optional.ofNullable(auth).isEmpty()) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

}
