package estore.istic.fr.View;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;

import estore.istic.fr.Facade.OnOrderSaveListener;
import estore.istic.fr.Model.Dto.OrderStatus;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Utils;
import estore.istic.fr.Services.OrdersService;

public class trackingOrdersActivity extends AppCompatActivity {

    MaterialCardView cardone, cardsecond, cardthird, cardfour, cardfive, doneButton;
    ImageView imgone, imgsecond, imgthird, imgfour, imgfive, doneimage, sendimage;
    LinearLayout linearone, linearsecond, linearthird, linearfour;
    TextView textone, textsecond, textthird, textfour, textfive, doneText;
    BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tracking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Utils.statusAndActionBarIconsColor(this, R.id.main);

        initialisation();
        fetchOrderState();
        OnClick();
    }

    public void initialisation() {
        doneimage = findViewById(R.id.done_image);
        sendimage = findViewById(R.id.send_houre);
        doneText = findViewById(R.id.text_button);
        textfive = findViewById(R.id.textfive);
        textfour = findViewById(R.id.textfour);
        textthird = findViewById(R.id.textthird);
        textsecond = findViewById(R.id.textsecond);
        textone = findViewById(R.id.textone);
        cardone = findViewById(R.id.one);
        cardsecond = findViewById(R.id.second);
        cardthird = findViewById(R.id.third);
        cardfour = findViewById(R.id.four);
        cardfive = findViewById(R.id.five);
        imgone = findViewById(R.id.imgone);
        imgsecond = findViewById(R.id.imgsecond);
        imgthird = findViewById(R.id.imgthird);
        imgfour = findViewById(R.id.imgfour);
        imgfive = findViewById(R.id.imgfive);
        linearone = findViewById(R.id.linearone);
        linearsecond = findViewById(R.id.linearsecond);
        linearthird = findViewById(R.id.linearthird);
        linearfour = findViewById(R.id.linearfour);
        doneButton = findViewById(R.id.done);
    }

    public void fetchOrderState() {
        OrdersService.tracOrderDeliveryState(getIntent().getStringExtra("orderID"), new OnOrderSaveListener() {
            @Override
            public void onLoading() {}

            @Override
            public void onSuccess(String orderStatus) {
                try {
                    updateDeliveryUI(OrderStatus.valueOf(orderStatus));
                } catch (IllegalArgumentException e) {
                    updateDeliveryUI(OrderStatus.PENDING);
                }
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    private void updateDeliveryUI(OrderStatus status) {
        MaterialCardView[] cards = { cardone, cardsecond, cardthird, cardfour, cardfive };
        ImageView[] icons = { imgone, imgsecond, imgthird, imgfour, imgfive };
        LinearLayout[] lines = { linearone, linearsecond, linearthird, linearfour };
        TextView[] texts = { textone, textsecond, textthird, textfour, textfive };

        int totalSteps = cards.length;
        int currentStep = getOrderStatus(status);

        int activeCardColor = getResources().getColor(R.color.colorlast);
        int inactiveCardColor = getResources().getColor(R.color.white);
        int activeTextColor = getResources().getColor(R.color.deliveryText);
        int inactiveTextColor = getResources().getColor(R.color.edite_text_hint_color);
        int activeIconColor = ContextCompat.getColor(this, R.color.white);
        int inactiveIconColor = ContextCompat.getColor(this, R.color.edite_text_hint_color);

        for (int i = 0; i < totalSteps; i++) {
            icons[i].setColorFilter(i <= currentStep ? activeIconColor : inactiveIconColor, android.graphics.PorterDuff.Mode.MULTIPLY);
            cards[i].setCardBackgroundColor(i <= currentStep ? activeCardColor : inactiveCardColor);
            texts[i].setTextColor(i <= currentStep ? activeTextColor : inactiveTextColor);
            if (i < 4) lines[i].setVisibility(i == currentStep ? View.VISIBLE : View.GONE);
        }

        if (status == OrderStatus.DELIVERED) {
            doneText.setText("Finish");
            doneButton.setClickable(true);
            doneimage.setVisibility(View.VISIBLE);
            sendimage.setVisibility(View.GONE);
            bottomSheetDialog();
            return;
        }

        doneText.setText("On process...");
        doneButton.setClickable(false);
        doneimage.setVisibility(View.GONE);
        sendimage.setVisibility(View.VISIBLE);
    }

    public int getOrderStatus (OrderStatus status) {
        int currentStep;
        switch (status) {
            case PENDING : {
                currentStep = 0;
                break;
            }
            case CONFIRMED : {
                currentStep = 1;
                break;
            }
            case ON_PROCESS :{
                currentStep = 2;
                break;
            }
            case SHIPPED : {
                currentStep = 3;
                break;
            }
            case DELIVERED : {
                currentStep = 4;
                break;
            }
            default: {
                currentStep = -1;
                break;
            }
        };
        return currentStep;
    }

    public void bottomSheetDialog() {
        Utils.createBottomSheet(
                this,
                "Thank you !\nYour trust is our priority",
                "Done",
                () -> {}
        );
    }

    public void OnClick() {
        doneButton.setOnClickListener(view -> {
            finish();
        });
    }

    public void showToast(String message) {
        Utils.showToast(this, message);
    }

}