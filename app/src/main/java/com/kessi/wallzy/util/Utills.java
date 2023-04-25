package com.kessi.wallzy.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kessi.wallzy.R;
import com.kessi.wallzy.CropWallActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Utills {
    public static String mFolderName = "wallpapers";

    public static ArrayList<String> getAllWallpaper(Context context) {
        ArrayList<String> allWallpaper = new ArrayList<>();
        String[] category = getCategories(context);
        if (category == null)
            return null;

        for (int i = 0; i < category.length; i++) {
            String[] files = getCatWallpaper(context, category[i]);
            for (int j = 0; j < files.length; j++) {
                files[j] = category[i] + "/" + files[j];
            }
            allWallpaper.addAll(Arrays.asList(files));
        }

        return allWallpaper;
    }


    public static String[] getCategories(Context context) {

        String[] list;
        try {
            list = context.getAssets().list(mFolderName);
            if (list.length > 0) {
                for (String folders : list) {
//                    Log.e("folders", folders);
                }
            }
            return list;
        } catch (IOException e) {
            return null;
        }
    }

    public static List<String> getRandomWallpaper(List<String> list) {
        ArrayList<String> tList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String item = list.get(new Random().nextInt(list.size()));
            if (!tList.contains(item)) {
                tList.add(item);
            }
        }

        return tList;
    }

    public static ArrayList<Integer> getCatSize(Context context) {
        ArrayList<Integer> allWallpaper = new ArrayList<>();
        String[] category = getCategories(context);
        if (category == null)
            return null;

        for (int i = 0; i < category.length; i++) {
            String[] files = getCatWallpaper(context, category[i]);
            allWallpaper.add(files.length);
        }

        return allWallpaper;
    }

    public static String[] getCatWallpaper(Context context, String folderName) {

        String[] list;
        try {
            list = context.getAssets().list(mFolderName + "/" + folderName);
            if (list.length > 0) {
                for (String file : list) {
//                    Log.e("files", file);
                }
            }
            return list;
        } catch (IOException e) {
            return null;
        }
    }

    public static String getCatThumb(Context context, String folderName) {

        String[] list;
        try {
            list = context.getAssets().list(mFolderName + "/" + folderName);
            if (list.length > 0) {
                return mFolderName + "/" + folderName + "/" + list[new Random().nextInt(list.length)];
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }


    public static List<String> getTrendWall(List<String> list) {
        ArrayList<String> tList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String item = list.get(new Random().nextInt(list.size()));
            if (!tList.contains(item)) {
                tList.add(item);
            }
        }

        return tList;
    }

    public static ArrayList<String> searchWallpaper(Context context, String search) {
        ArrayList<String> allWallpaper = new ArrayList<>();
        String[] category = getCategories(context);
        if (category == null)
            return null;

        for (int i = 0; i < category.length; i++) {
            if (allWallpaper.size() > 19){
                break;
            }
            String[] files = getCatWallpaper(context, category[i]);
            ArrayList<String> searchFiles = new ArrayList<>();
            for (int j = 0; j < files.length; j++) {
                if (searchFiles.size() > 19){
                    break;
                }
                if (files[j].contains(search)) {
                    searchFiles.add(category[i] + "/" + files[j]);
                }
            }
            allWallpaper.addAll(searchFiles);

        }

        return allWallpaper;
    }


    public static void dialogSetWallpaperOption(Context context, final Bitmap bitmap) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_set_wallpaper, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);

        final CardView alertLay = view.findViewById(R.id.alertLay);
        final LinearLayout setHomeWall = view.findViewById(R.id.setHomeWall);
        final LinearLayout setLockWall = view.findViewById(R.id.setLockWall);
        final LinearLayout setBoth = view.findViewById(R.id.setBoth);
        final LinearLayout lyt_option = view.findViewById(R.id.lyt_option);
        final LinearLayout lyt_progress = view.findViewById(R.id.lyt_progress);

        final AlertDialog alertDialog = alert.create();

        setHomeWall.setOnClickListener(v -> {

            alertDialog.setCancelable(false);
            lyt_option.setVisibility(View.GONE);
            lyt_progress.setVisibility(View.VISIBLE);

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context.getApplicationContext());

            new Handler().postDelayed(() -> {
                alertDialog.dismiss();
                lyt_progress.setVisibility(View.GONE);
                closeApp(context);
            }, 2000);

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                }
            } catch (IOException e) {
                e.printStackTrace();
                alertDialog.dismiss();
                lyt_progress.setVisibility(View.GONE);
                Toast.makeText(context, "Wallpaper not set", Toast.LENGTH_SHORT).show();
            }

        });

        setLockWall.setOnClickListener(v -> {
            alertDialog.setCancelable(false);
            lyt_option.setVisibility(View.GONE);
            lyt_progress.setVisibility(View.VISIBLE);

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context.getApplicationContext());

            new Handler().postDelayed(() -> {
                alertDialog.dismiss();
                lyt_progress.setVisibility(View.GONE);
                closeApp(context);
            }, 2000);

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                }
            } catch (IOException e) {
                e.printStackTrace();
                alertDialog.dismiss();
                lyt_progress.setVisibility(View.GONE);
                Toast.makeText(context, "Wallpaper not set", Toast.LENGTH_SHORT).show();
            }

        });

        setBoth.setOnClickListener(v -> {

            alertDialog.setCancelable(false);
            lyt_option.setVisibility(View.GONE);
            lyt_progress.setVisibility(View.VISIBLE);

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context.getApplicationContext());

            new Handler().postDelayed(() -> {
                alertDialog.dismiss();
                lyt_progress.setVisibility(View.GONE);
                closeApp(context);
            }, 2000);

            try {
                wallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                alertDialog.dismiss();
                lyt_progress.setVisibility(View.GONE);
                Toast.makeText(context, "Wallpaper not set", Toast.LENGTH_SHORT).show();
            }

        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        Render render = new Render(context);
        render.setAnimation(KSUtil.ZoomIn(alertLay));
        render.setDuration(400);
        render.start();
    }

    public static void dialogSetWallpaper(Context context, Bitmap bitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Set this image as wallpaper?")
                .setCancelable(true)
                .setPositiveButton("yes", (dialog, id) -> {
                    try {
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context.getApplicationContext());
                        wallpaperManager.setBitmap(bitmap);

                        final ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage("Setting wallpaper....");
                        progressDialog.show();

                        new Handler().postDelayed(() -> {
                            progressDialog.dismiss();
                            closeApp(context);
                        }, 2000);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Wallpaper not set", Toast.LENGTH_SHORT).show();
                        Log.v("ERROR", "Wallpaper not set");
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public static void closeApp(Context context) {
        Toast.makeText(context, "Wallpaper set successfully", Toast.LENGTH_SHORT).show();
        ((Activity) context).onBackPressed();
    }


    public static void dialogApplyOption(Context context, final String prefixPath, final String path) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_options_wall, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);

        final CardView alertLay = view.findViewById(R.id.alertLay);
        final LinearLayout option_apply_now = view.findViewById(R.id.applyNow);
        final LinearLayout option_crop_wallpaper = view.findViewById(R.id.cropWallpaper);


        final AlertDialog alertDialog = alert.create();

        option_apply_now.setOnClickListener(v -> {
            alertDialog.setCancelable(false);

            if (prefixPath.equals("")) {
                Glide.with(context)
                        .asBitmap()
                        .load(path)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                if (Build.VERSION.SDK_INT >= 24) {
                                    dialogSetWallpaperOption(context, resource);
                                } else {
                                    dialogSetWallpaper(context, resource);
                                }
                                alertDialog.dismiss();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
            } else {
                Glide.with(context)
                        .asBitmap()
                        .load(Uri.parse(prefixPath + path))
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                if (Build.VERSION.SDK_INT >= 24) {
                                    dialogSetWallpaperOption(context, resource);
                                } else {
                                    dialogSetWallpaper(context, resource);
                                }
                                alertDialog.dismiss();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
            }

        });

        option_crop_wallpaper.setOnClickListener(v -> {
            alertDialog.setCancelable(false);
            Intent intent = new Intent(context, CropWallActivity.class);
            intent.putExtra("prefixPath", prefixPath);
            intent.putExtra("image_url", path);
            context.startActivity(intent);
            alertDialog.dismiss();

        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        Render render = new Render(context);
        render.setAnimation(KSUtil.ZoomIn(alertLay));
        render.setDuration(400);
        render.start();
    }


    public static int perRequest = 1;

    public static String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    public static boolean hasPermissions(Context context, String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }


    public static int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    public static void changeStatusBarColor(Activity context, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getWindow().setStatusBarColor(ContextCompat.getColor(context.getApplicationContext(), color));
        }
    }

    public static void maxNativeSize(Context context, RelativeLayout relativeLayout){
        FrameLayout.LayoutParams tabp = new FrameLayout.LayoutParams(dpToPixel(context, 300),
                dpToPixel(context, 250));
        relativeLayout.setLayoutParams(tabp);
    }
    public static int dpToPixel(Context context, int dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        return  (int) (dp * scale + 0.5f);
    }
}
