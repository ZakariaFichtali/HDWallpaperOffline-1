package com.kessi.wallzy.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.kessi.wallzy.MyApp;
import com.kessi.wallzy.PagerPreviewActivity;
import com.kessi.wallzy.R;
import com.kessi.wallzy.util.AdManager;
import com.kessi.wallzy.util.Animatee;
import com.kessi.wallzy.util.ImageUtils;
import com.kessi.wallzy.util.Utills;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExploreAdapter extends RecyclerView.Adapter{
    List<String> categoryList;
    Activity context;

    public static final int AD_TYPE = 2;
    public static final int CONTENT_TYPE = 1;
    int nativeAdPos;

    public ExploreAdapter(List<String> categoryList, Activity context, int nativeAdPos) {
        this.categoryList = categoryList;
        this.context = context;

        this.nativeAdPos = nativeAdPos;

    }

    public static class ImageHolder extends RecyclerView.ViewHolder{
        ImageView wallIV, downloadIV;
        CardView cardView;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            wallIV = itemView.findViewById(R.id.wallIV);
            downloadIV = itemView.findViewById(R.id.downloadIV);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }

    private static class ADViewHolder extends RecyclerView.ViewHolder {


        LinearLayout rl_native_ad;
        private ADViewHolder(View view) {
            super(view);
            rl_native_ad = view.findViewById(R.id.rl_native_ad);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == AD_TYPE) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_nads, parent, false);
            return new ADViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_explore, parent, false);

            return new ImageHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (getItemViewType(position) == CONTENT_TYPE) {
            RelativeLayout.LayoutParams tabp = new RelativeLayout.LayoutParams(context.getResources().getDisplayMetrics().widthPixels * 327 / 1080,
                    context.getResources().getDisplayMetrics().heightPixels * 455 / 1920);
            holder.itemView.setLayoutParams(tabp);


            Glide.with(context).
                    load(Uri.parse("file:///android_asset/" + Utills.mFolderName + "/" + categoryList.get(position)))
                    .into(((ImageHolder)holder).wallIV);

            ((ImageHolder)holder).wallIV.setOnClickListener(v -> {
                Intent intent = new Intent(context, PagerPreviewActivity.class);
                intent.putStringArrayListExtra("wallpapers", (ArrayList<String>) categoryList);
                intent.putExtra("position", position);
                intent.putExtra("prefix", "file:///android_asset/" + Utills.mFolderName + "/");
                context.startActivity(intent);
                Animatee.animateSlideUp(context);
            });

            ((ImageHolder)holder).downloadIV.setOnClickListener(v -> {
                if (Utills.hasPermissions(context, Utills.permissions)) {
                    ActivityCompat.requestPermissions((Activity) context, Utills.permissions, Utills.perRequest);
                } else {
                    Glide.with(context)
                            .asBitmap()
                            .load(Uri.parse("file:///android_asset/" + Utills.mFolderName + "/" + categoryList.get(position)))
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    ImageUtils.asyncSave(context, resource, categoryList.get(position));
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                }
            });
        }else {
            MyApp.adManager.showSingleNative(context, ((ADViewHolder) holder).rl_native_ad);

        }
    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if ((position+1) % nativeAdPos == 0){

            return AD_TYPE;
        } else {

            return CONTENT_TYPE;
        }

    }

}
