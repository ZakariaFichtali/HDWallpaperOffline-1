package com.kessi.wallzy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kessi.wallzy.adapters.FullscreenImageAdapter;
import com.kessi.wallzy.util.AdManager;
import com.kessi.wallzy.util.Animatee;
import com.kessi.wallzy.util.DepthTransformation;
import com.kessi.wallzy.util.ImageUtils;
import com.kessi.wallzy.util.KSUtil;
import com.kessi.wallzy.util.Render;
import com.kessi.wallzy.util.Utills;

import java.io.File;
import java.util.ArrayList;

public class PagerPreviewActivity extends AppCompatActivity {

    ViewPager viewPager;
    ArrayList<String> imageList;
    int position;
    ImageView shareIV, setAsIV, downloadIV, deleteIV;
    TextView downloadText, deleteText;
    FullscreenImageAdapter fullscreenImageAdapter;
    ImageView backIV;
    String prefixPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_preview);

        if (Utills.getStatusBarHeight(PagerPreviewActivity.this) > Utills.convertDpToPixel(24)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        MyApp.adManager.loadAds(this);

        backIV = findViewById(R.id.backIV);
        viewPager = findViewById(R.id.viewPager);
        shareIV = findViewById(R.id.shareIV);
        downloadIV = findViewById(R.id.downloadIV);
        downloadText = findViewById(R.id.downloadText);
        deleteText = findViewById(R.id.deleteText);
        deleteIV = findViewById(R.id.deleteIV);
        setAsIV = findViewById(R.id.setAsIV);

        imageList = getIntent().getStringArrayListExtra("wallpapers");
        position = getIntent().getIntExtra("position", 0);
        prefixPath = getIntent().getStringExtra("prefix");

        fullscreenImageAdapter = new FullscreenImageAdapter(PagerPreviewActivity.this, imageList, prefixPath);
        viewPager.setAdapter(fullscreenImageAdapter);
        viewPager.setCurrentItem(position);

        viewPager.setPageTransformer(true, new DepthTransformation());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MyApp.adManager.showInterstitial(PagerPreviewActivity.this, true, new AdManager.adFinishedListener() {
                    @Override
                    public void onAdFinished() {

                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        downloadIV.setOnClickListener(clickListener);
        deleteText.setOnClickListener(clickListener);
        downloadText.setOnClickListener(clickListener);
        shareIV.setOnClickListener(clickListener);
        backIV.setOnClickListener(clickListener);
        setAsIV.setOnClickListener(clickListener);
        deleteIV.setOnClickListener(clickListener);

        if (prefixPath.equals("")) {

            downloadIV.setVisibility(View.GONE);
            downloadText.setVisibility(View.GONE);
            deleteIV.setVisibility(View.VISIBLE);
        } else {
            downloadIV.setVisibility(View.VISIBLE);
            deleteIV.setVisibility(View.GONE);
            deleteText.setVisibility(View.GONE);
        }

        MyApp.adManager.loadAds(this);

        RelativeLayout adContainer = findViewById(R.id.banner_container);
        MyApp.adManager.showBannerView(this, adContainer);

        Utills.changeStatusBarColor(PagerPreviewActivity.this, R.color.black);
    }


    @Override
    public void onBackPressed() {
        MyApp.adManager.showInterstitial(this, false, new AdManager.adFinishedListener() {
            @Override
            public void onAdFinished() {
                PagerPreviewActivity.super.onBackPressed();
                Animatee.animateSlideDown(PagerPreviewActivity.this);
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backIV:
                    onBackPressed();
                    break;

                case R.id.deleteIV:
                    if (imageList.size() > 0) {
                        final Dialog exitDialog = new Dialog(PagerPreviewActivity.this);
                        exitDialog.setContentView(R.layout.delete_popup_lay);

                        exitDialog.getWindow().setBackgroundDrawable(
                                new ColorDrawable(android.graphics.Color.TRANSPARENT));

                        LinearLayout alertLay = exitDialog.findViewById(R.id.alertLay);
                        TextView noBtn = exitDialog.findViewById(R.id.noBtn);
                        TextView yesBtn = exitDialog.findViewById(R.id.yesBtn);

                        noBtn.setOnClickListener(arg0 -> exitDialog.dismiss());

                        yesBtn.setOnClickListener(arg0 -> {
                            exitDialog.dismiss();
                            int currentItem = 0;

                            File file = new File(imageList.get(viewPager.getCurrentItem()));
                            if (file.exists()) {
                                boolean del = file.delete();
                                if (imageList.size() > 0 && viewPager.getCurrentItem() < imageList.size()) {
                                    currentItem = viewPager.getCurrentItem();
                                }
                                imageList.remove(viewPager.getCurrentItem());
                                fullscreenImageAdapter = new FullscreenImageAdapter(PagerPreviewActivity.this, imageList, prefixPath);
                                viewPager.setAdapter(fullscreenImageAdapter);

                                Intent intent = new Intent();
                                setResult(10, intent);

                                if (imageList.size() > 0) {
                                    viewPager.setCurrentItem(currentItem);
                                } else {
                                    finish();
                                }
                            }
                        });
                        exitDialog.show();
                        Render render = new Render(PagerPreviewActivity.this);
                        render.setAnimation(KSUtil.ZoomIn(alertLay));
                        render.setDuration(400);
                        render.start();
                    } else {
                        finish();
                    }
                    break;

                case R.id.downloadIV:
                    if (!Utills.hasPermissions(PagerPreviewActivity.this, Utills.permissions)) {
                        ActivityCompat.requestPermissions((Activity) PagerPreviewActivity.this, Utills.permissions, Utills.perRequest);
                    } else {
                        Glide.with(PagerPreviewActivity.this)
                                .asBitmap()
                                .load(Uri.parse(prefixPath + imageList.get(viewPager.getCurrentItem())))
                                .into(new CustomTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        ImageUtils.asyncSave(PagerPreviewActivity.this, resource, imageList.get(viewPager.getCurrentItem()));
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                    }
                                });
                        Intent intent = new Intent();
                        setResult(10, intent);
                    }
                    break;

                case R.id.shareIV:
                    if (!Utills.hasPermissions(PagerPreviewActivity.this, Utills.permissions)) {
                        ActivityCompat.requestPermissions((Activity) PagerPreviewActivity.this, Utills.permissions, Utills.perRequest);
                    } else {
                        if (prefixPath.equals("")) {
                            ImageUtils.mShare(prefixPath + imageList.get(viewPager.getCurrentItem()), PagerPreviewActivity.this);
                        } else {
                            Glide.with(PagerPreviewActivity.this)
                                    .asBitmap()
                                    .load(Uri.parse(prefixPath + imageList.get(viewPager.getCurrentItem())))
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            ImageUtils.asyncSavenShare(PagerPreviewActivity.this, resource);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                        }
                                    });
                        }
                    }
                    break;


                case R.id.setAsIV:
                    if (!Utills.hasPermissions(PagerPreviewActivity.this, Utills.permissions)) {
                        ActivityCompat.requestPermissions((Activity) PagerPreviewActivity.this, Utills.permissions, Utills.perRequest);
                    } else {
                        Utills.dialogApplyOption(PagerPreviewActivity.this, prefixPath, imageList.get(viewPager.getCurrentItem()));
                    }
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
