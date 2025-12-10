package estore.istic.fr.View;

import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import estore.istic.fr.Facade.OnUserActionListener;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Utils;
import estore.istic.fr.Services.UsersService;

public class userProfileActivity extends AppCompatActivity {

    TextInputLayout nameLayout, phoneLayout;
    RelativeLayout updateButton;
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_user_iactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Utils.statusAndActionBarIconsColor(this, R.id.main);
        Utils.setup(userProfileActivity.this, "Profile", true);

        initialisation();
        loadUser();
        onClicks();

    }

    public void initialisation() {
        nameLayout = findViewById(R.id.name);
        phoneLayout = findViewById(R.id.phone);
        updateButton = findViewById(R.id.update);
    }

    public void loadUser() {
        UsersService.getUserData(new OnUserActionListener() {
            @Override
            public void onSuccess(String userName, String userEmail, String phoneNumber) {
                Objects.requireNonNull(phoneLayout.getEditText()).setText(phoneNumber);
                Objects.requireNonNull(nameLayout.getEditText()).setText(userName);
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    public void onClicks() {
        updateButton.setOnClickListener(view -> validateInformation());
    }

    public void validateInformation() {
        if (Objects.requireNonNull(phoneLayout.getEditText()).getText().toString().isEmpty()) {
            phoneLayout.getEditText().setError("Enter phone number !");
            return;
        }

        if (Objects.requireNonNull(nameLayout.getEditText()).getText().toString().isEmpty()) {
            nameLayout.getEditText().setError("Enter your name !");
            return;
        }

        dialog = Utils.createLoadingDialog(
                this,
                "Updating profile !",
                "We are updating your profile, please wait a minute !"
        );
        UpdateUser(
                nameLayout.getEditText().getText().toString().trim(),
                phoneLayout.getEditText().getText().toString().trim()
        );
    }

    public void UpdateUser(String name, String number) {
        UsersService.updateUserData(name, number, new OnUserActionListener() {
            @Override
            public void onSuccess(String userName, String userEmail, String phoneNumber) {
                showToast(userName);
                dialog.dismiss();
                finish();
            }

            @Override
            public void onError(String message) {
                showToast(message);
                dialog.dismiss();
            }
        });
    }

    public void showToast(String message) {
        Utils.showToast(this, message);
    }

}