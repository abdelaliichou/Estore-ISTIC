package estore.istic.fr.Controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import estore.istic.fr.Resources.Utils;

public class NotificationsPermissionHandler {
    private final Activity activity;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    public NotificationsPermissionHandler(AppCompatActivity activity) {
        this.activity = activity;
        requestPermissionLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                this::handlePermissionResult
        );
    }

    public void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED) {
                return;
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.POST_NOTIFICATIONS)) {
                // Show explanation dialog
                showWhyDialog();
                return;
            }

            // Request permission directly
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
    }

    private void handlePermissionResult(boolean isGranted) {
        if (isGranted) {
            Utils.showToast(activity, "Notifications Enabled");
            return;
        }

        // Check if we need to show settings dialog (if user checked "Don't ask again")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.POST_NOTIFICATIONS)) {
                showSettingsDialog();
                return;
            }

            Utils.showToast(activity,  "Notifications Denied");
        }
    }

    private void showWhyDialog() {
        Utils.createActionDialog(
                activity,
                "Permission Required",
                "We need notifications to update you on your order status.",
                true,
                () -> requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS),
                () -> Utils.showToast(activity, "Notifications Disabled")
        );
    }

    private void showSettingsDialog() {
        Utils.createActionDialog(
                activity,
                "Permission Required",
                "Notifications are permanently disabled. Please enable them in settings.",
                true,
                () -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                    activity.startActivity(intent);
                },
                () -> Utils.showToast(activity, "Notifications Disabled")
        );
    }
}
