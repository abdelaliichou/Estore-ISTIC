package estore.istic.fr.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;

import java.util.Objects;
import java.util.Optional;

import estore.istic.fr.R;
import estore.istic.fr.Resources.Animations;
import estore.istic.fr.Resources.DatabaseSeeder;
import estore.istic.fr.Resources.databaseHelper;
import estore.istic.fr.Resources.Utils;

public class loginActivity extends AppCompatActivity {

    RelativeLayout LoginButton;
    TextView signupTestView, mainTextView, forgotPasswordTextView, secondTextView;
    ImageView mainImage, googleImage, facebookImage;
    LinearLayout SupportLinearLayout, orLoginLayout;
    TextInputLayout emailLayout, passwordLayout;
    AlertDialog dialog;
    CheckBox saveStateBox;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.parent), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Utils.statusAndActionBarIconsColor(this, R.id.parent);

        initialisation();
        handlingAnimation();
        RememberMeUser(this);
        handlingOnClicks(this);

        // DatabaseSeeder.seed();

    }

    private void handlingAnimation() {
        Animations.FromUpToDown(mainImage);
        Animations.FromeLeftToRight(mainTextView);
        Animations.FromeLeftToRight1(secondTextView);
        Animations.FromeRightToLeft(LoginButton);
        Animations.FromeDownToUp(SupportLinearLayout);
        Animations.FromeRightToLeftEditetext1(emailLayout);
        Animations.FromeRightToLeftEditetext2(passwordLayout);
        Animations.FromeDownToUp(orLoginLayout);
    }

    public void initialisation() {
        progressBar = findViewById(R.id.progress);
        secondTextView = findViewById(R.id.login_second_text);
        LoginButton = findViewById(R.id.login_button);
        signupTestView = findViewById(R.id.go_to_sign);
        mainTextView = findViewById(R.id.main_text);
        mainImage = findViewById(R.id.main_img);
        SupportLinearLayout = findViewById(R.id.support_linear_layout);
        emailLayout = findViewById(R.id.email_parent_login);
        passwordLayout = findViewById(R.id.password_parent_login);
        orLoginLayout = findViewById(R.id.or_login_with_layout);
        forgotPasswordTextView = findViewById(R.id.forgot_password_text);
        saveStateBox = findViewById(R.id.remember_me);
        googleImage = findViewById(R.id.google_login);
        facebookImage = findViewById(R.id.facebook_login);
    }

    public void handlingOnClicks(Context context) {
        LoginButton.setOnClickListener(v -> {
            String userEmail = Objects.requireNonNull(emailLayout.getEditText()).getText().toString().trim();
            String userPassword = Objects.requireNonNull(passwordLayout.getEditText()).getText().toString().trim();

            if (userEmail.isEmpty()) {
                emailLayout.getEditText().setError("Enter your email please !");
                return;
            }
            if (userPassword.isEmpty()) {
                passwordLayout.getEditText().setError("Enter your password !");
                return;
            }
            if (!userEmail.matches(Utils.EMAIL_PATTERN)) {
                emailLayout.getEditText().setError("Invalid email form !");
                return;
            }
            if (userPassword.length() < 7) {
                passwordLayout.getEditText().setError("Short password !");
                return;
            }

            dialog = Utils.createDialog(
                    context,
                    "Wait a minute please !",
                    "Searching for this user...",
                    false,
                    R.drawable.ic__cloud_upload,
                    R.drawable.alert_dialog_back
            );

            dialog.show();
            authenticateUser(userEmail, userPassword);
        });

        signupTestView.setOnClickListener(v -> startActivity(new Intent(
                loginActivity.this,
                signupActivity.class
        )));

        forgotPasswordTextView.setOnClickListener(v -> startActivity(new Intent(
                loginActivity.this,
                forgetPasswordActivity.class
        )));
    }

    public void RememberMeUser(Context context) {
        if (Optional.ofNullable(databaseHelper.getAuth().getCurrentUser()).isPresent()) {
            dialog = Utils.createDialog(
                    context,
                    "Wait a minute please !",
                    "Hello again, we're redirecting you to Home !",
                    false,
                    R.drawable.ic__cloud_upload,
                    R.drawable.alert_dialog_back
            );
            dialog.show();
            navigateToHome();
        }
    }

    public void authenticateUser(String UserEmail, String UserPassword) {
        databaseHelper.getAuth().signOut();
        databaseHelper.getAuth().signInWithEmailAndPassword(
                UserEmail,
                UserPassword
        ).addOnCompleteListener(new OnCompleteListener<>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    navigateToHome();
                    return;
                }

                showToast(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    public void navigateToHome() {
        showToast("Welcome !");
        startActivity(new Intent(loginActivity.this, MainActivity.class));
        finishAffinity();
    }

    private void showToast(String message) {
        dialog.dismiss();
        Utils.showToast(this, message);
    }
}