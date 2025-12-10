package estore.istic.fr.Resources;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import estore.istic.fr.Model.Domain.Category;
import estore.istic.fr.Model.Domain.Order;
import estore.istic.fr.Model.Domain.Product;
import estore.istic.fr.Model.Dto.OrderStatus;
import estore.istic.fr.R;


public class Utils {

    public static ArrayList<Category> list;
    public static ArrayList<Product> list3, list5;
    public static int existss = 0;

    public static String EMAIL_PATTERN = "[a-zA-Z0-9!#$%&'*+/=?^:_`{|}~.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*";

    public static NumberFormat dollarFormatter = NumberFormat.getCurrencyInstance(Locale.US);

    public static DecimalFormat noDollarFormat = new DecimalFormat("#,###.00");

    // hide the keyboard when we clicks any where(better user experience )
    public static void SettingKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(),
                    0
            );
        }
    }

    public static void setup(AppCompatActivity activity, String title, boolean enableBack) {
        androidx.appcompat.widget.Toolbar toolbar = activity.findViewById(R.id.my_toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");

            TextView titleView = toolbar.findViewById(R.id.toolbar_title);
            if (titleView != null) {
                titleView.setText(title);
            }

            if (enableBack) {
                toolbar.setNavigationOnClickListener(v -> {
                    activity.getOnBackPressedDispatcher().onBackPressed();
                });
            } else {
                toolbar.setNavigationIcon(null);
            }
        }
    }

    public static void setUpKeyboard(View view, Activity activity) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utils.SettingKeyboard(activity);
                    return false;
                }
            });
        }
        // If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setUpKeyboard(innerView, activity);
            }
        }
    }

    public static void createBottomSheet(
            Context context,
            String title,
            String okText,
            int image,
            @Nullable Runnable onConfirm
    ) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet);
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        Optional<RelativeLayout> checkButton = Optional.ofNullable(bottomSheetDialog.findViewById(R.id.check_procces));
        Optional<TextView> titleText = Optional.ofNullable(bottomSheetDialog.findViewById(R.id.title));
        Optional<ImageView> icon = Optional.ofNullable(bottomSheetDialog.findViewById(R.id.icon));
        Optional<TextView> buttonText = Optional.ofNullable(bottomSheetDialog.findViewById(R.id.ok_text));

        titleText.get().setText(title);
        buttonText.get().setText(okText);
        icon.get().setImageResource(image);

        checkButton.ifPresent(button -> {
            button.setOnClickListener(view -> {
                onConfirm.run();
                bottomSheetDialog.dismiss();
            });
        });
        bottomSheetDialog.show();
    }

    public static AlertDialog createLoadingDialog(
            Activity activity,
            String title,
            String message
    ) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return null;
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.waiting_dialog, null);
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        TextView titleView = dialogView.findViewById(R.id.title);
        TextView messageView = dialogView.findViewById(R.id.message);

        if (titleView != null) titleView.setText(title);
        if (messageView != null) messageView.setText(message);

        setupDialogWindow(activity, alertDialog);

        try {
            alertDialog.show();
        } catch (WindowManager.BadTokenException e) {
            Log.e("DialogUtils", "Failed to show loading dialog: " + e.getMessage());
        }

        return alertDialog;
    }

    public static void createActionDialog(
            Activity activity,
            String title,
            String message,
            boolean isCancelable,
            Runnable okClick,
            Runnable cancelClick
    ) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_layout, null);
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setView(dialogView)
                .setCancelable(isCancelable)
                .create();

        TextView txtTitle = dialogView.findViewById(R.id.title);
        TextView txtMessage = dialogView.findViewById(R.id.message);
        AppCompatButton btnOk = dialogView.findViewById(R.id.settingsBtn);
        AppCompatButton btnCancel = dialogView.findViewById(R.id.cancelBtn);
        ImageView imgIcon = dialogView.findViewById(R.id.icon);

        if (txtTitle != null) txtTitle.setText(title);
        if (txtMessage != null) txtMessage.setText(message);

        if (btnOk != null) {
            btnOk.setText("Confirm");
            btnOk.setOnClickListener(v -> {
                okClick.run();
                alertDialog.dismiss();
            });
        }

        if (btnCancel != null) {
            btnCancel.setText("Cancel");
            btnCancel.setOnClickListener(v -> {
                cancelClick.run();
                alertDialog.dismiss();
            });
        }

        setupDialogWindow(activity, alertDialog);

        try {
            alertDialog.show();
        } catch (WindowManager.BadTokenException e) {
            Log.e("DialogUtils", "Failed to show action dialog: " + e.getMessage());
        }
    }

    private static void setupDialogWindow(Activity activity, AlertDialog alertDialog) {
        alertDialog.setOnShowListener(dialog -> {
            if (alertDialog.getWindow() != null) {

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
                int width = (int) (metrics.widthPixels * 0.90);

                alertDialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

                WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                params.gravity = Gravity.CENTER;
                alertDialog.getWindow().setAttributes(params);
            }
        });
    }

    public static String parseDate(Long date) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Instant instant = Instant.ofEpochMilli(date);
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy • h:mm");
            return zdt.format(formatter);
        }

        Date fixedDate = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy • h:mm", Locale.getDefault());
        return sdf.format(fixedDate);
    }

    public static void handlingOrderStatus(
            Context context,
            RelativeLayout parent,
            TextView status,
            Order order
    ) {
        int pendingCardColor = context.getResources().getColor(R.color.pendingLite);
        int pendingColor = context.getResources().getColor(R.color.pending);
        int confirmedCardColor = context.getResources().getColor(R.color.confirmedLite);
        int confirmedColor = context.getResources().getColor(R.color.confirmed);
        int onProcessCardColor = context.getResources().getColor(R.color.onProcessLite);
        int onProcessColor = context.getResources().getColor(R.color.onProcess);
        int shippedCardColor = context.getResources().getColor(R.color.shippedLite);
        int shippedColor = context.getResources().getColor(R.color.shipped);
        int deliveredCardColor = context.getResources().getColor(R.color.deliveredLite);
        int deliveredColor = context.getResources().getColor(R.color.delivered);
        int canceledCardColor = context.getResources().getColor(R.color.canceledLite);
        int canceledColor = context.getResources().getColor(R.color.canceled);

        int deliveryStatus = Utils.getOrderStatus(order.getStatus());

        switch (deliveryStatus) {
            case 0: {
                status.setTextColor(pendingColor);
                status.setText(OrderStatus.PENDING.name());
                parent.setBackgroundColor(pendingCardColor);
                break;
            }
            case 1: {
                parent.setBackgroundColor(confirmedCardColor);
                status.setTextColor(confirmedColor);
                status.setText(OrderStatus.CONFIRMED.name());
                break;
            }
            case 2: {
                parent.setBackgroundColor(onProcessCardColor);
                status.setTextColor(onProcessColor);
                status.setText(OrderStatus.ON_PROCESS.name());
                break;
            }
            case 3: {
                parent.setBackgroundColor(shippedCardColor);
                status.setTextColor(shippedColor);
                status.setText(OrderStatus.SHIPPED.name());
                break;
            }
            case 4: {
                parent.setBackgroundColor(deliveredCardColor);
                status.setTextColor(deliveredColor);
                status.setText(OrderStatus.DELIVERED.name());
                break;
            }
            case -1: {
                parent.setBackgroundColor(canceledCardColor);
                status.setTextColor(canceledColor);
                status.setText(OrderStatus.CANCELED.name());
                break;
            }
        }
    }

    public static void statusAndActionBarIconsColor(
            Activity activity,
            int parentID
    ) {

        // action bar
        if (activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            if (Optional.ofNullable(appCompatActivity.getSupportActionBar()).isPresent()) {
                appCompatActivity.getSupportActionBar().hide();
            }
        }

        // Setup keyboard
        setUpKeyboard(activity.findViewById(parentID), activity);

        // Set status bar color
        activity.getWindow().setStatusBarColor(Color.WHITE);

        // Set dark status bar icons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Objects.requireNonNull(activity.getWindow().getDecorView().getWindowInsetsController())
                    .setSystemBarsAppearance(
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    );
        }

        // Set navigation bar color
        activity.getWindow().setNavigationBarColor(Color.WHITE);

        // Set dark navigation bar icons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.getWindow()
                    .getDecorView()
                    .getWindowInsetsController()
                    .setSystemBarsAppearance(
                            WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                            WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                    );
        }
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    public static int getOrderStatus(OrderStatus status) {
        int currentStep;
        switch (status) {
            case PENDING: {
                currentStep = 0;
                break;
            }
            case CONFIRMED: {
                currentStep = 1;
                break;
            }
            case ON_PROCESS: {
                currentStep = 2;
                break;
            }
            case SHIPPED: {
                currentStep = 3;
                break;
            }
            case DELIVERED: {
                currentStep = 4;
                break;
            }
            default: {
                currentStep = -1;
                break;
            }
        }
        ;
        return currentStep;
    }

    public static List<SlideModel> getSlideList() {
        List<SlideModel> list = new ArrayList<>();
        list.add(new SlideModel("https://img.freepik.com/free-photo/flat-lay-salad-weights_23-2148262144.jpg?w=740&t=st=1685019447~exp=1685020047~hmac=166b1cf654e134b90653ae064c9b275b2750d35641392682d54e75aade66a596", ScaleTypes.CENTER_CROP));
        list.add(new SlideModel("https://images.unsplash.com/photo-1593095948071-474c5cc2989d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=870&q=80", ScaleTypes.CENTER_CROP));
        list.add(new SlideModel("https://plus.unsplash.com/premium_photo-1672352722063-678ed538f80e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=870&q=80", ScaleTypes.CENTER_CROP));
        list.add(new SlideModel("https://images.unsplash.com/photo-1609150990057-f13c984a12f6?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=870&q=80", ScaleTypes.CENTER_CROP));
        list.add(new SlideModel("https://images.unsplash.com/photo-1587854692152-cbe660dbde88?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=869&q=80", ScaleTypes.CENTER_CROP));
        list.add(new SlideModel("https://plus.unsplash.com/premium_photo-1672759453651-c6834f55c4f6?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1095&q=80", ScaleTypes.CENTER_CROP));
        list.add(new SlideModel("https://images.unsplash.com/photo-1559087316-6b27308e53f6?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=794&q=80", ScaleTypes.CENTER_CROP));
        return list;
    }

}


