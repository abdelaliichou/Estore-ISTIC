package estore.istic.fr.View;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;

import estore.istic.fr.R;
import estore.istic.fr.Resources.Animations;
import estore.istic.fr.Resources.Utils;

public class splashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_motions);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Utils.statusAndActionBarIconsColor(this, R.id.main);

        handlingAnimations();

        FirebaseApp.initializeApp(this);


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, loginActivity.class).putExtra("FROM_SPLASH", true);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 2000);

    }

    void handlingAnimations() {
        Animations.FromeLeftToRight3(findViewById(R.id.main_text));
    }
}