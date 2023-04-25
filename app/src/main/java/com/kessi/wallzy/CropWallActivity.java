package com.kessi.wallzy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kessi.wallzy.util.AdManager;
import com.kessi.wallzy.util.Utills;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CropWallActivity extends AppCompatActivity {

    private CropImageView mCropImageView;
    String image_url, prefixPath;
    ImageView setAsIV, backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_wall_activity);

        if (Utills.getStatusBarHeight(CropWallActivity.this) > Utills.convertDpToPixel(24)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        Intent i = getIntent();
        image_url = i.getStringExtra("image_url");
        prefixPath = i.getStringExtra("prefixPath");

        mCropImageView = findViewById(R.id.CropImageView);
        mCropImageView.setImageUriAsync(Uri.parse(image_url));

        if (prefixPath.equals("")) {
            Glide.with(this)
                    .asBitmap()
                    .load(image_url)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            mCropImageView.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(Uri.parse(prefixPath + image_url))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            mCropImageView.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }

        setAsIV = findViewById(R.id.setAsIV);
        setAsIV.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 24) {
                Utills.dialogSetWallpaperOption(CropWallActivity.this, mCropImageView.getCroppedImage());
            } else {
                Utills.dialogSetWallpaper(CropWallActivity.this, mCropImageView.getCroppedImage());
            }
        });

        backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> {
            onBackPressed();
        });
        MyApp.adManager.loadAds(this);

        RelativeLayout adContainer = findViewById(R.id.banner_container);
        MyApp.adManager.showBannerView(this, adContainer);

    }

    @Override
    public void onBackPressed() {
        MyApp.adManager.showInterstitial(this, false, new AdManager.adFinishedListener() {
            @Override
            public void onAdFinished() {
                CropWallActivity.super.onBackPressed();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}