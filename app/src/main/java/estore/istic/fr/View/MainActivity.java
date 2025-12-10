package estore.istic.fr.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Optional;

import estore.istic.fr.R;
import estore.istic.fr.Resources.Utils;
import estore.istic.fr.View.Fragments.cartFragment;
import estore.istic.fr.View.Fragments.favoritesFragment;
import estore.istic.fr.View.Fragments.homeFragment;
import estore.istic.fr.View.Fragments.optionsFragment;
import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    public static AnimatedBottomBar bottomBar;
    homeFragment homeFragment;
    cartFragment cartFragment;
    optionsFragment optionsFragment;
    favoritesFragment favoritesFragment;
    final int HOME_ID = R.id.Home;
    final int FAV_ID = R.id.Fav;
    final int OPTIONS_ID = R.id.Options;
    final int CART_ID = R.id.Cart;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // putting the first fragment when we open the activity
        initialFragment();
        initialisation();
        onClicks();
        doublePressedToQuite();

    }

    public void initialisation(){
        bottomBar = findViewById(R.id.bottom);
        homeFragment = new homeFragment();
        cartFragment = new cartFragment();
        favoritesFragment = new favoritesFragment();
        optionsFragment = new optionsFragment();
    }

    public void doublePressedToQuite() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (pressedTime + 2000 > System.currentTimeMillis()) {
                    finishAffinity();
                    System.exit(0);
                    return;
                }

                Utils.showToast(MainActivity.this,  "Press back again to exit");
                pressedTime = System.currentTimeMillis();
            }
        });
    }

    public void onClicks() {
        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {    }

            @SuppressLint("NonConstantResourceId")
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                Optional<Fragment> fragment = Optional.empty();
                switch (tab1.getId()) {
                    case HOME_ID:
                        fragment = Optional.of(new homeFragment());
                        break;
                    case FAV_ID:
                        fragment = Optional.of(new favoritesFragment());
                        bottomBar.clearBadgeAtTabId(R.id.Fav);
                        break;
                    case CART_ID:
                        fragment = Optional.of(new cartFragment());
                        break;
                    case OPTIONS_ID:
                        fragment = Optional.of(new optionsFragment());
                        break;
                }
                fragment.ifPresent(value -> getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, value)
                        .commit()
                );
            }
        });

    }

    public void initialFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, new homeFragment())
                .commit();
        Utils.statusAndActionBarIconsColor(this, R.id.main);
    }

}