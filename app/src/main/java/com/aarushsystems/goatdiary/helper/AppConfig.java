package com.aarushsystems.goatdiary.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.File;
import java.util.ArrayList;

public class AppConfig {
    //public static final String EMAIL_VERIFICATION_URL = "https://goatdiary.000webhostapp.com/verify_email.php";
    /*public static final String REGISTER_URL = "https://goatdiary.000webhostapp.com/register_user.php";
    public static final String LOGIN_URL = "https://goatdiary.000webhostapp.com/login_user.php";
    public static final String FORGOT_PASSWORD_URL = "https://goatdiary.000webhostapp.com/forgot_password.php";
    public static final String PASSWORD_RESET_URL = "https://goatdiary.000webhostapp.com/password_reset.php";
    public static final String CHECK_STATUS_URL = "https://goatdiary.000webhostapp.com/check_active_status.php";
    public static final String USER_MANUAL = "https://goatdiary.000webhostapp.com/files/Goat%20Diary%20User%20Manual.pdf";
    public static final String TERMS_AND_CONDITIONS = "https://goatdiary.000webhostapp.com/files/GoatDiary%20Farm%20Management%20App%20Terms,%20Conditions%20and%20Privacy%20Policy.pdf";*/
    private Activity activity;

    public static final boolean PLAY_STORE_APK = false;
    public static final String REGISTER_URL = "http://goatapp.goatdiary.in/register_user.php";
    public static final String EMAIL_VERIFICATION_URL = "http://goatapp.goatdiary.in/verify_email.php";
    public static final String LOGIN_URL = "http://goatapp.goatdiary.in/login_user.php";
    public static final String FORGOT_PASSWORD_URL = "http://goatapp.goatdiary.in/forgot_password.php";
    public static final String PASSWORD_RESET_URL = "http://goatapp.goatdiary.in/password_reset.php";
    public static final String CHECK_STATUS_URL = "http://goatapp.goatdiary.in/check_active_status.php";
    public static final String USER_MANUAL = "http://goatapp.goatdiary.in/files/Goat%20Diary%20User%20Manual.pdf";
    public static final String TERMS_AND_CONDITIONS = "http://goatapp.goatdiary.in/files/GoatDiary%20Farm%20Management%20App%20Terms,%20Conditions%20and%20Privacy%20Policy.pdf";

    public AppConfig(Activity context) {
        this.activity = context;
    }

    public void permissionCheck() {
        String[] permissions = {Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        String rationale = "Please provide Internet and Storage Permission to save all Reports on your device and sync your data.\nThank You!";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning");

        Permissions.check(activity/*context*/, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
                String appDir = Environment.getExternalStorageDirectory().toString() + "/Goat Diary";
                File file = new File(appDir);
                if (!file.exists()) {
                    if (file.mkdir()) {
                        Log.i("CUSTOM", "folder created successfully");
                    } else {
                        Log.i("CUSTOM", "failed");
                    }
                } else {
                    Log.i("CUSTOM", "exists");
                }
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                DialogDateUtil dialogDateUtil = new DialogDateUtil(activity);
                dialogDateUtil.showMessage("Reports will not be saved on your device.");
            }
        });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
