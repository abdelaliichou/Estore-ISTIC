package estore.istic.fr.Resources;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsetsController;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
import java.util.function.Predicate;
import java.util.regex.Pattern;

import estore.istic.fr.Model.Domain.Category;
import estore.istic.fr.Model.Domain.Product;
import estore.istic.fr.Model.Dto.OrderStatus;
import estore.istic.fr.Model.Dto.ProductDto;
import estore.istic.fr.R;
import estore.istic.fr.View.forgetPasswordActivity;
import estore.istic.fr.View.loginActivity;


public class Utils {

    public static ArrayList<Category> list;
    public static ArrayList<Product> list3, list5;
    public static int existss = 0;

    public static String EMAIL_PATTERN = "[a-zA-Z0-9!#$%&'*+/=?^:_`{|}~.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*";
    public static String orderImageUrl = "https://images.unsplash.com/photo-1680281708071-453a5bc80372?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";

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

    public static AlertDialog createDialog(
            Context context,
            String title,
            String message,
            boolean isCancelable,
            int icon,
            int background,
            boolean isClickable,
            @Nullable Runnable onConfirm,
            @Nullable Runnable onCancel
    ) {
        MaterialAlertDialogBuilder progressDialog = new MaterialAlertDialogBuilder(context);

        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(isCancelable);
        if (isClickable) {
            progressDialog.setNegativeButton("Cancel", (dialog, i) -> {
                        onCancel.run();
                        dialog.dismiss();
                    }
            );
            progressDialog.setPositiveButton("Confirm", (dialog, i) -> {
                        onConfirm.run();
                        dialog.dismiss();
                    }
            );
        }
        progressDialog.setBackground(context.getResources().getDrawable(background));
        progressDialog.setIcon(icon);
        progressDialog.setCancelable(false);

        return progressDialog.show();
    }

    public static String parseDate(Long date) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Instant instant = Instant.ofEpochMilli(date);
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return zdt.format(formatter);
        }

        Date fixedDate = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(fixedDate);
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

    public static int getOrderStatus (OrderStatus status) {
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


