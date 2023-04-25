package com.kessi.wallzy;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kessi.wallzy.adapters.CatListAdapter;
import com.kessi.wallzy.adapters.ExploreAdapter;
import com.kessi.wallzy.adapters.WallAdapter;
import com.kessi.wallzy.util.AdManager;
import com.kessi.wallzy.util.Utills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExploreActivity extends AppCompatActivity {

    RecyclerView wallList;
    List<String> wallArray;
    ExploreAdapter catListAdapter;
    ImageView backIV;
    TextView topTxt;
    RelativeLayout loaderLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_list);

        wallList = findViewById(R.id.wallList);
        loaderLay = findViewById(R.id.loaderLay);

        topTxt = findViewById(R.id.topTxt);
        topTxt.setText(getResources().getText(R.string.app_name));

        backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> {
            onBackPressed();
        });

        new getCatList().execute();
        MyApp.adManager.loadAds(this);

        RelativeLayout adContainer = findViewById(R.id.banner_container);
        MyApp.adManager.showBannerView(this, adContainer);
    }


    @Override
    public void onBackPressed() {
        MyApp.adManager.showInterstitial(this, false, new AdManager.adFinishedListener() {
            @Override
            public void onAdFinished() {
                ExploreActivity.super.onBackPressed();
            }
        });
    }


    class getCatList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loaderLay.setVisibility(View.VISIBLE);
            wallList.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wallArray = getIntent().getStringArrayListExtra("wallzy");
            Collections.shuffle(wallArray);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            new Handler().postDelayed(() -> {
                loaderLay.setVisibility(View.GONE);
                wallList.setVisibility(View.VISIBLE);
                catListAdapter = new ExploreAdapter(wallArray, ExploreActivity.this,10);
                final GridLayoutManager gridLayoutManager = new GridLayoutManager(ExploreActivity.this, 3);

                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        switch (catListAdapter.getItemViewType(position)) {
                            case 1:
                                return 1;
                            case 2:
                                return gridLayoutManager.getSpanCount();
                            default:
                                return -1;
                        }
                    }
                });
                wallList.setLayoutManager(gridLayoutManager);
                wallList.setItemAnimator(new DefaultItemAnimator());
                wallList.setAdapter(catListAdapter);
            }, 2000);
        }
    }


}