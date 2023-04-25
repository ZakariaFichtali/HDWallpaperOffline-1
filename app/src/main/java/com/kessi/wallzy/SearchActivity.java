package com.kessi.wallzy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kessi.wallzy.adapters.ExploreAdapter;
import com.kessi.wallzy.util.AdManager;
import com.kessi.wallzy.util.Utills;

import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    ImageView backIV;
    String search;
    RelativeLayout loaderLay;
    RecyclerView wallList;
    List<String> wallArray;
    ExploreAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        wallList = findViewById(R.id.wallList);
        loaderLay = findViewById(R.id.loaderLay);

        backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> {
            onBackPressed();
        });

        search = getIntent().getStringExtra("search");

        new searchAsync().execute();
        MyApp.adManager.loadAds(this);

        RelativeLayout adContainer = findViewById(R.id.banner_container);
        MyApp.adManager.showBannerView(this, adContainer);
    }

    class searchAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loaderLay.setVisibility(View.VISIBLE);
            wallList.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wallArray = Utills.searchWallpaper(SearchActivity.this, search);
            Collections.shuffle(wallArray);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            new Handler().postDelayed(() -> {

                if (wallArray.size() != 0) {
                    loaderLay.setVisibility(View.GONE);
                    wallList.setVisibility(View.VISIBLE);

                    searchAdapter = new ExploreAdapter(wallArray, SearchActivity.this,7);
                    final GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchActivity.this, 3);

                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            switch (searchAdapter.getItemViewType(position)) {
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
                    wallList.setAdapter(searchAdapter);
                } else {
                    loaderLay.setVisibility(View.GONE);
                    wallList.setVisibility(View.GONE);
                    Toast.makeText(SearchActivity.this, "No Wallpaper found.", Toast.LENGTH_SHORT).show();
                }
            }, 1600);
        }
    }

    @Override
    public void onBackPressed() {
        MyApp.adManager.showInterstitial(this, false, new AdManager.adFinishedListener() {
            @Override
            public void onAdFinished() {
                SearchActivity.super.onBackPressed();
            }
        });
    }

}