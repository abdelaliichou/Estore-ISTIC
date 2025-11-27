package estore.istic.fr.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import estore.istic.fr.R;
import estore.istic.fr.Resources.Utils;

public class resetEmailActivity extends AppCompatActivity {

    RelativeLayout checkEmailButton , doneButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email_recover);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Utils.statusAndActionBarIconsColor(this, R.id.main);

        initialisation();
        handlingOnClicks();

    }

    public void initialisation(){
        checkEmailButton = findViewById(R.id.send_email) ;
        doneButton = findViewById(R.id.done_buttton) ;
    }

    public void handlingOnClicks(){
        checkEmailButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        doneButton.setOnClickListener(v -> {
            startActivity(new Intent(resetEmailActivity.this, passwordRecoveredActivity.class));
            finish();
        });
    }

}