package com.kessi.wallzy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Insets;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;

import androidx.annotation.NonNull;

public class CustomUtils {

    public static final int TIME = 3000; // 5000ms = 5s

    public static final String jsonURL = "https://abdelalimalki.com/db.json";
    public static int dataIsLoaded = 0;

    public static boolean showAds = true;
    public static String bannerAd = "";
    public static String interAd = "";
    public static String nativeAd = "";
    public static String admobBanner = "";
    public static String admobInter = "";
    public static String admobNative = "";
    public static String fbBanner = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";
    public static String fbInter = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";
    public static String fbNative = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";
    public static String applovinBnr = "";
    public static String applovinInter = "";
    public static String applovinNative = "";
    public static String ironsource_api = "";
    public static boolean unityTest = true;
    public static String unityGameID = "";
    public static String unityBanner = "banner";
    public static String unityInter = "video";
    public static int intersitial_click_activites = 1;
    public static int intersitial_click_list = 3;



    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static int getDeviceSize(@NonNull Activity activity, int type) {
        // Type : 1 - width and 2 - height
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            if (type == 1)
                return windowMetrics.getBounds().width() - insets.left - insets.right;
            else
                return windowMetrics.getBounds().height() - insets.bottom - insets.top;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            if (type == 1)
                return displayMetrics.widthPixels;
            else
                return displayMetrics.heightPixels;
        }

    }

}
