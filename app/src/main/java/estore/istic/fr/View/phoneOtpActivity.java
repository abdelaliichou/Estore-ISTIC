package estore.istic.fr.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chaos.view.PinView;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import estore.istic.fr.R;
import estore.istic.fr.Resources.Animations;
import estore.istic.fr.Resources.DatabaseHelper;
import estore.istic.fr.Resources.Utils;

public class phoneOtpActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ImageView mainImage;
    TextView secondText, mainText;
    RelativeLayout loginButton;
    PinView pinView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Utils.statusAndActionBarIconsColor(this, R.id.main);

        initialisation();
        handlingOnClicks();
        handlingAnimation();

    }

    private void handlingAnimation() {
        Animations.FromUpToDown(mainImage);
        Animations.FromeRightToLeft(loginButton);
        Animations.FromeRightToLeftPinview(pinView);
        Animations.FromeLeftToRight(mainText);
        Animations.FromeLeftToRight1(secondText);
    }

    public Optional<Map<String, String>> getUserInfos() {

        Map<String, String> data = new HashMap<>();
        Optional<String> serverCode = Optional.ofNullable(getIntent().getStringExtra("otpCode"));
        Optional<String> number = Optional.ofNullable(getIntent().getStringExtra("phoneNumber"));
        Optional<String> email = Optional.ofNullable(getIntent().getStringExtra("email"));

        if (serverCode.isPresent() && email.isPresent() && number.isPresent()) {
            data.put("phoneNumber", number.get());
            data.put("otpCode", serverCode.get());
            data.put("email", email.get());
            return Optional.of(data);
        }

        showToast("Something went wrong when receiving data from previous page!");
        return Optional.empty();
    }

    public void initialisation() {
        loginButton = findViewById(R.id.otp_submit);
        secondText = findViewById(R.id.otp_second_text);
        mainText = findViewById(R.id.otp_main_text);
        mainImage = findViewById(R.id.otp_image);
        pinView = findViewById(R.id.otp_number);
        progressBar = findViewById(R.id.progrres);

        // setting number text
        if (getUserInfos().isPresent()) {
            secondText.setText(secondText.getText().toString().concat(Objects.requireNonNull(getUserInfos().get().get("phoneNumber"))));
            return;
        }

        secondText.setText(secondText.getText().toString().concat("*********"));
    }

    public void handlingOnClicks() {
        loginButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            verifyCode();
        });
    }

    public void verifyCode() {

        if (getUserInfos().isEmpty()) {
            showToast("Something went wrong when receiving data from previous page!");
            return;
        }

        String userCode = Objects.requireNonNull(pinView.getText()).toString().trim();
        String serverCode = getUserInfos().get().get("otpCode");
        String email = getUserInfos().get().get("email");

        sendUserToNextActivity(serverCode, userCode, email);
    }

    public void sendUserToNextActivity(String serverCode, String userCode, String email) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                serverCode,
                userCode
        );
        DatabaseHelper.getAuth().signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                showToast("Invalid verification code !");
                return;
            }

            SendEmailVerification(email);
        });
    }

    public void SendEmailVerification(String email) {
        DatabaseHelper.getAuth().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        showToast(Objects.requireNonNull(task.getException()).getMessage());
                        return;
                    }

                    showToast("Your need to insert your new password in the link we've send in your email !");
                    Intent intent = new Intent(phoneOtpActivity.this, resetEmailActivity.class);
                    DatabaseHelper.getAuth().signOut();
                    startActivity(intent);
                    finish();

        }).addOnFailureListener(e -> showToast(e.getMessage()));
    }

    private void showToast(String message) {
        progressBar.setVisibility(View.GONE);
        Utils.showToast(this, message);
    }

}