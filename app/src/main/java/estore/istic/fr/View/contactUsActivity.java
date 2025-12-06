package estore.istic.fr.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.function.Consumer;

import estore.istic.fr.Facade.OnUserActionListener;
import estore.istic.fr.Model.Domain.User;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Utils;
import estore.istic.fr.Services.UsersService;

public class contactUsActivity extends AppCompatActivity {

    RelativeLayout emailLayout, phoneLayout, facebook ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_us);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Utils.statusAndActionBarIconsColor(this, R.id.main);

        initialisation();
        onClicks();
    }

    public void initialisation() {
        emailLayout = findViewById(R.id.email);
        phoneLayout = findViewById(R.id.phone);
        facebook = findViewById(R.id.facebook);
    }

    public void loadUser(
            Consumer<String> onName,
            Consumer<String> onEmail,
            Consumer<String> onPhone,
            Consumer<String> onError
    ) {
        UsersService.getUserData(new OnUserActionListener() {
            @Override
            public void onSuccess(String userName, String userEmail, String phoneNumber) {
                onName.accept(userName);
                onEmail.accept(userEmail);
                onPhone.accept(phoneNumber);
            }

            @Override
            public void onError(String message) {
                onError.accept(message);
            }
        });
    }

    @SuppressLint({"IntentReset", "QueryPermissionsNeeded"})
    public void onClicks() {

        User user = new User();
        loadUser(
                user::setName,
                user::setEmail,
                user::setPhoneNumber,
                error -> {}
        );

        emailLayout.setOnClickListener(view -> {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.setData(Uri.parse("mailto:"));
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{user.getEmail()});
            email.putExtra(Intent.EXTRA_SUBJECT, "How was your experience in my application ?");
            email.putExtra(Intent.EXTRA_TEXT, "");
            email.setType("message/rfc822");

            if (email.resolveActivity(contactUsActivity.this.getPackageManager()) != null) {
                startActivity(email);
            }
        });

        phoneLayout.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:".concat(user.getPhoneNumber())));
            startActivity(intent);
        });

        facebook.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ali.ichou.31/"))));
    }

}