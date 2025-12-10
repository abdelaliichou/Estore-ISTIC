package estore.istic.fr.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.Optional;

import estore.istic.fr.Facade.OnUserActionListener;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Animations;
import estore.istic.fr.Resources.DatabaseHelper;
import estore.istic.fr.Resources.Utils;
import estore.istic.fr.Services.UsersService;

public class loginActivity extends AppCompatActivity {

    RelativeLayout LoginButton;
    TextView signupTestView, mainTextView, forgotPasswordTextView, secondTextView;
    ImageView mainImage;
    LinearLayout SupportLinearLayout;
    TextInputLayout emailLayout, passwordLayout;
    AlertDialog dialog;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        boolean fromSplash = getIntent().getBooleanExtra("FROM_SPLASH", false);

        if (!fromSplash) {

            // Keep the native splash visible to hide the transition
            splashScreen.setKeepOnScreenCondition(() -> true);

            Intent intent = new Intent(this, splashActivity.class);
            startActivity(intent);

            overridePendingTransition(0, 0);
            finish();

        } else {

            setContentView(R.layout.activity_login);
            EdgeToEdge.enable(this);
            Optional<View> parentView = Optional.ofNullable(findViewById(R.id.parent));
            parentView.ifPresent(parent -> {
                ViewCompat.setOnApplyWindowInsetsListener(parent, (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });

                Utils.statusAndActionBarIconsColor(this, R.id.parent);
            });


            initialisation();
            handlingAnimation();
            RememberMeUser();
            handlingOnClicks();

            // DatabaseSeeder.seed();
        }
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
        forgotPasswordTextView = findViewById(R.id.forgot_password_text);
    }

    private void handlingAnimation() {
        Animations.FromUpToDown(mainImage);
        Animations.FromeLeftToRight(mainTextView);
        Animations.FromeLeftToRight1(secondTextView);
        Animations.FromeRightToLeft(LoginButton);
        Animations.FromeDownToUp(SupportLinearLayout);
        Animations.FromeRightToLeftEditetext1(emailLayout);
        Animations.FromeRightToLeftEditetext2(passwordLayout);
    }

    public void handlingOnClicks() {
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

            dialog = Utils.createLoadingDialog(
                    this,
                    "Wait a minute please !",
                    "Searching for this user..."
            );
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

    public void RememberMeUser() {
        if (Optional.ofNullable(DatabaseHelper.getAuth().getCurrentUser()).isPresent()) {
            dialog = Utils.createLoadingDialog(
                    this,
                    "Wait a minute please !",
                    "Hello again, we're redirecting you to Home !"
            );
            navigateToHome();
        }
    }

    public void authenticateUser(String UserEmail, String UserPassword) {
        DatabaseHelper.getAuth().signOut();
        UsersService.authenticateUser(UserEmail, UserPassword, new OnUserActionListener() {
            @Override
            public void onSuccess(String userName, String userEmail, String phoneNumber) {
                navigateToHome();
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    public void navigateToHome() {
        startActivity(new Intent(loginActivity.this, MainActivity.class));
        finishAffinity();
    }

    private void showToast(String message) {
        dialog.dismiss();
        Utils.showToast(this, message);
    }
}