package estore.istic.fr.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import estore.istic.fr.Model.Domain.User;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Animations;
import estore.istic.fr.Resources.DatabaseHelper;
import estore.istic.fr.Resources.Utils;

public class forgetPasswordActivity extends AppCompatActivity {

    RelativeLayout SubmitButton;
    TextView mainText, secondText;
    ImageView mainImage;
    TextInputLayout phoneLayout;
    String userNumber;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Utils.statusAndActionBarIconsColor(this, R.id.main);

        initialisation();
        handlingAnimation();
        handlingOnClicks();
    }

    private void handlingAnimation() {
        Animations.FromUpToDown(mainImage);
        Animations.FromeLeftToRight(mainText);
        Animations.FromeLeftToRight1(secondText);
        Animations.FromeRightToLeftEditetext2(phoneLayout);
        Animations.FromeRightToLeft(SubmitButton);
    }

    public void initialisation() {
        SubmitButton = findViewById(R.id.forget_password_submit_button);
        mainText = findViewById(R.id.signup_main_text);
        mainImage = findViewById(R.id.forget_password_image);
        phoneLayout = findViewById(R.id.forget_password_email_layout);
        secondText = findViewById(R.id.signup_second_text);
        progressBar = findViewById(R.id.progres);
    }

    public void handlingOnClicks() {
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userNumber = Objects.requireNonNull(phoneLayout.getEditText()).getText().toString().trim();

                if (userNumber.isEmpty()) {
                    phoneLayout.getEditText().setError("Enter your phone number !");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                verifyingPhoneNumber(userNumber);
            }
        });
    }

    private void verifyingPhoneNumber(String number) {
        DatabaseHelper.getDatabaseReference()
                .child("Users")
                .orderByChild("phoneNumber")
                .equalTo(number)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            showToast("No account with this phone number!");
                            return;
                        }

                        DataSnapshot firstUser = snapshot.getChildren().iterator().next();
                        Optional<User> dbUser = Optional.ofNullable(firstUser.getValue(User.class));

                        if (dbUser.isEmpty()) {
                            showToast("Failed to fetch the user associated to this phone number!");
                            return;
                        }

                        showToast("Sending message to ".concat(dbUser.get().getEmail()));
                        SendOTPCode(dbUser.get());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        showToast(error.getMessage());
                    }
                });

    }

    private void showToast(String message) {
        progressBar.setVisibility(View.GONE);
        Utils.showToast(this, message);
    }

    public void SendOTPCode(User user) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+33" + user.getPhoneNumber(),
                60,
                TimeUnit.SECONDS,
                forgetPasswordActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                        // passing the firebase otp code & the user data
                        Intent intent = new Intent(
                                forgetPasswordActivity.this,
                                phoneOtpActivity.class
                        )
                        .putExtra("otpCode", verificationId)
                        .putExtra("phoneNumber", user.getPhoneNumber())
                        .putExtra("email", user.getEmail());

                        progressBar.setVisibility(View.GONE);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        showToast("Verification completed!");
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        showToast(e.getMessage());
                    }
                }
        );
    }

}