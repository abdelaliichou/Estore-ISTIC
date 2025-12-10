package estore.istic.fr.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import estore.istic.fr.R;
import estore.istic.fr.Resources.Animations;
import estore.istic.fr.Resources.Utils;

public class passwordRecoveredActivity extends AppCompatActivity {

    RelativeLayout submitButton;
    TextView mainText, secondText;
    ImageView mainImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password_recovered);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Utils.statusAndActionBarIconsColor(this, R.id.main);

        initialisation();
        handlingAnimation();
        handlingOnClicks();

        Snackbar.make(findViewById(R.id.main), "Password updated successfully !", Snackbar.LENGTH_SHORT)
                .setText("Your password has been updated successfully !")
                .setTextColor(getResources().getColor(R.color.white))
                .setBackgroundTint(getResources().getColor(R.color.fontmain))
                .setAction("Got it !", view -> {})
                .setActionTextColor(getResources().getColor(R.color.mainyellow))
                .show();
    }

    private void handlingAnimation() {
        Animations.FromUpToDown(mainImage);
        Animations.FromeLeftToRight(mainText);
        Animations.FromeLeftToRight1(secondText);
        Animations.FromeRightToLeft(submitButton);
    }

    public void initialisation() {
        submitButton = findViewById(R.id.all_good_submit_button);
        mainText = findViewById(R.id.all_good_main_text);
        secondText = findViewById(R.id.all_good_second_text);
        mainImage = findViewById(R.id.all_good_image);
    }

    private void handlingOnClicks() {
        submitButton.setOnClickListener(v -> {
            startActivity(new Intent(passwordRecoveredActivity.this, loginActivity.class));
            finishAffinity();
        });
    }

}

