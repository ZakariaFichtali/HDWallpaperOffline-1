package com.kessi.wallzy.fragment;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.kessi.wallzy.ExploreActivity;
import com.kessi.wallzy.MyApp;
import com.kessi.wallzy.PagerPreviewActivity;
import com.kessi.wallzy.R;
import com.kessi.wallzy.SearchActivity;
import com.kessi.wallzy.adapters.WallAdapter;
import com.kessi.wallzy.util.AdManager;
import com.kessi.wallzy.util.Animatee;
import com.kessi.wallzy.util.Utills;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WallpaperFrag extends Fragment {

    List<String> wallArray, iRandomArray;
    List<String> trendArray;
    RecyclerView mRecyclerView;
    WallAdapter mAdapter;
    CarouselView carouselView;
    ScrollView scrollView;
    RelativeLayout loaderLay;
    LinearLayout exploreAll;
    EditText search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_wallpaper, container, false);

        carouselView = rootView.findViewById(R.id.carouselView);
        mRecyclerView = rootView.findViewById(R.id.wallList);

        scrollView = rootView.findViewById(R.id.scrollView);
        loaderLay = rootView.findViewById(R.id.loaderLay);

        View wallList1 = rootView.findViewById(R.id.wallList);



        new getWallAsync().execute();

        exploreAll = rootView.findViewById(R.id.exploreAll);
        exploreAll.setOnClickListener(view -> {
            MyApp.adManager.showInterstitial(getActivity(), false, new AdManager.adFinishedListener() {
                @Override
                public void onAdFinished() {
                    Intent intent = new Intent(getActivity(), ExploreActivity.class);
                    intent.putStringArrayListExtra("wallzy", (ArrayList<String>) wallArray);
                    startActivity(intent);
                    Animatee.animateSlideUp(getActivity());
                }
            });

        });

        search = rootView.findViewById(R.id.search);
        search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                if (search.getText() != null && !search.getText().toString().isEmpty()) {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    intent.putExtra("search", search.getText().toString());
                    startActivity(intent);
                }

                return true;
            }
            return false;
        });


        RelativeLayout adContainer = rootView.findViewById(R.id.banner_container);
        LinearLayout rl_native_ad = rootView.findViewById(R.id.rl_native_ad);
        MyApp.adManager.showBannerView(getActivity(), adContainer);
        MyApp.adManager.showSingleNative(getActivity(),rl_native_ad );
        return rootView;
    }


    class getWallAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loaderLay.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            wallArray = Utills.getAllWallpaper(getActivity());
            Collections.shuffle(wallArray);

            trendArray = Utills.getTrendWall(wallArray);
            Collections.shuffle(trendArray);

            iRandomArray = new ArrayList<>();
            iRandomArray = Utills.getRandomWallpaper(wallArray);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            new Handler().postDelayed(() -> {
                loaderLay.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                carouselView.setImageListener(imageListener);
                carouselView.setPageCount(trendArray.size());
                carouselView.setImageClickListener(position -> {
                    MyApp.adManager.showInterstitial(getActivity(), true, new AdManager.adFinishedListener() {
                        @Override
                        public void onAdFinished() {
                            Intent intent = new Intent(getActivity(), PagerPreviewActivity.class);
                            intent.putStringArrayListExtra("wallpapers", (ArrayList<String>) trendArray);
                            intent.putExtra("position", position);
                            intent.putExtra("prefix", "file:///android_asset/" + Utills.mFolderName + "/");
                            getActivity().startActivity(intent);
                            Animatee.animateSlideUp(getActivity());

                        }
                    });

                });




                mAdapter = new WallAdapter(iRandomArray, getActivity());
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                super.onPostExecute(aVoid);
                StaggeredGridLayoutManager wallList1 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(wallList1);
                mRecyclerView.setAdapter(mAdapter);

                
            },2000);
        }
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(getActivity()).
            load(Uri.parse("file:///android_asset/" + Utills.mFolderName + "/" + trendArray.get(position)))
                    .into(imageView);
        }
    };

}
