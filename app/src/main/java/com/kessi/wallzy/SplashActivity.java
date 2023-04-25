package com.kessi.wallzy;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.ironsource.mediationsdk.IronSource;
import com.kessi.wallzy.util.Animatee;
import com.kessi.wallzy.util.KSUtil;
import com.kessi.wallzy.util.Render;
import com.kessi.wallzy.util.SharedPrefs;
import com.kessi.wallzy.util.Utills;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;

public class SplashActivity extends AppCompatActivity {

    ImageView icons, mBg;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppCompatDelegate.setDefaultNightMode(SharedPrefs.getAppNightDayMode(this));


        if (Utills.getStatusBarHeight(SplashActivity.this) > Utills.convertDpToPixel(24)) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        Utills.changeStatusBarColor(SplashActivity.this, R.color.black);

        icons = findViewById(R.id.icons);
        Glide.with(this)
                .load(R.drawable.logo)
                .into(icons);

        mBg = findViewById(R.id.mBg);
        Glide.with(this)
                .load(R.drawable.spbgs)
                .into(mBg);

        Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
        mBg.setAnimation(zoomin);
        zoomin.setRepeatCount(-1);



        new Handler().postDelayed(() -> {
            // Set Animation
            Render render = new Render(SplashActivity.this);
            render.setAnimation(KSUtil.Swing(icons));
            render.start();
        }, 1400);

        final Handler handler = new Handler();

        //---------------------------------------------------
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(CustomUtils.dataIsLoaded == 1) {
                    IronSource.init(SplashActivity.this, CustomUtils.ironsource_api, IronSource.AD_UNIT.INTERSTITIAL, IronSource.AD_UNIT.BANNER);


                    startActivity(new Intent(SplashActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    Animatee.animateSlideUp(SplashActivity.this);
                    finish();
                }
                else if(CustomUtils.dataIsLoaded == 0) {
                    handler.postDelayed(this, 500);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error while loading data from server. Try again later!!", Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        },CustomUtils.TIME);
        //---------------------------------------------------

    }

    @Override
    public void onBackPressed() {

    }
}
