package estore.istic.fr.View;

import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import estore.istic.fr.Facade.OnUserActionListener;
import estore.istic.fr.Model.Domain.User;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Animations;
import estore.istic.fr.Resources.DatabaseHelper;
import estore.istic.fr.Resources.Utils;
import estore.istic.fr.Services.UsersService;

public class signupActivity extends AppCompatActivity {

    TextView loginText, secondText, mainText;
    RelativeLayout loginButton;
    TextInputLayout fullNameTextLayout, phoneNumberTextLayout, emailTextLayout, confirmPasswordTextLayout, passwordTextLayout;
    AlertDialog dialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.parent), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Utils.statusAndActionBarIconsColor(this, R.id.parent);

        initialisation();
        handlingOnClicks();
        handlingAnimation();

    }

    private void handlingAnimation() {
        Animations.FromeRightToLeft(loginButton);
        Animations.FromeRightToLeftEditetext4(confirmPasswordTextLayout);
        Animations.FromeRightToLeftEditetext1(emailTextLayout);
        Animations.FromeRightToLeftEditetext(fullNameTextLayout);
        Animations.FromeRightToLeftEditetext3(passwordTextLayout);
        Animations.FromeRightToLeftEditetext2(phoneNumberTextLayout);
        Animations.FromeLeftToRight(mainText);
        Animations.FromeLeftToRight1(secondText);
    }

    public void initialisation() {
        loginButton = findViewById(R.id.login_button);
        loginText = findViewById(R.id.go_to_login);
        secondText = findViewById(R.id.signup_second_text);
        mainText = findViewById(R.id.signup_main_text);
        fullNameTextLayout = findViewById(R.id.full_name_parent);
        phoneNumberTextLayout = findViewById(R.id.phone_parent);
        emailTextLayout = findViewById(R.id.email_parent);
        confirmPasswordTextLayout = findViewById(R.id.confirm_password_parent);
        passwordTextLayout = findViewById(R.id.password_parent);
    }

    public void handlingOnClicks() {
        loginButton.setOnClickListener(v -> {

            String userName = Objects.requireNonNull(fullNameTextLayout.getEditText()).getText().toString().trim();
            String userEmail = Objects.requireNonNull(emailTextLayout.getEditText()).getText().toString().trim();
            String userNumber = Objects.requireNonNull(phoneNumberTextLayout.getEditText()).getText().toString().trim();
            String userPassword = Objects.requireNonNull(passwordTextLayout.getEditText()).getText().toString().trim();
            String userConfirmPassword = Objects.requireNonNull(confirmPasswordTextLayout.getEditText()).getText().toString().trim();

            if (userName.isEmpty()) {
                fullNameTextLayout.getEditText().setError("Enter your name !");
                return;
            }
            if (userEmail.isEmpty()) {
                emailTextLayout.getEditText().setError("Enter your email !");
                return;
            }
            if (!userEmail.matches(Utils.EMAIL_PATTERN)) {
                emailTextLayout.getEditText().setError("Invalid email form !");
                return;
            }
            if (userNumber.isEmpty()) {
                phoneNumberTextLayout.getEditText().setError("Enter your number !");
                return;
            }
            if (userPassword.isEmpty()) {
                passwordTextLayout.getEditText().setError("Enter your password !");
                return;
            }
            if (userPassword.length() < 7) {
                passwordTextLayout.getEditText().setError("Short password !");
                return;
            }
            if (userConfirmPassword.isEmpty()){
                confirmPasswordTextLayout.getEditText().setError("Enter your password !");
                return;
            }
            if (!userPassword.equals(userConfirmPassword)) {
                confirmPasswordTextLayout.getEditText().setError("Passwords don't match !");
                return;
            }

            dialog = Utils.createLoadingDialog(
                    this,
                    "Wait a minute please !",
                    "We are registering you , you'll be ready in just a moment ..."
            );
            createUser(userName, userEmail, userPassword, userNumber);
        });

        loginText.setOnClickListener(v -> {
            startActivity(new Intent(signupActivity.this, loginActivity.class));
            finishAffinity();
        });
    }

    public void createUser(
            String name,
            String email,
            String password,
            String number
    ) {
        UsersService.createUser(
                name,
                email,
                password,
                number,
                new OnUserActionListener() {
                    @Override
                    public void onSuccess(String userName, String userEmail, String phoneNumber) {
                        showToast("Successfully Signed in !");
                        startActivity(new Intent(signupActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onError(String message) {
                        showToast(message);
                    }
                }
        );
    }

    private void showToast(String message) {
        dialog.dismiss();
        Utils.showToast(this, message);
    }
}