package estore.istic.fr.Model.Domain;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

public class User implements Serializable {
    @PropertyName("user_id")
    String userId;
    String name , email ;
    @PropertyName("phone_number")

    String phoneNumber;

    public User() {}
    public User(
            String name,
            String email,
            String phoneNumber
    ) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
