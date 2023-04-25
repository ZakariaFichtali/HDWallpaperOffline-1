package com.kessi.wallzy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.kessi.wallzy.MyApp;
import com.kessi.wallzy.PagerPreviewActivity;
import com.kessi.wallzy.R;
import com.kessi.wallzy.util.AdManager;
import com.kessi.wallzy.util.Animatee;
import com.kessi.wallzy.util.Utills;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CatListAdapter extends RecyclerView.Adapter{
    List<String> categoryList;
    Activity context;
    String folderName;

    public static final int AD_TYPE = 2;
    public static final int CONTENT_TYPE = 1;
    int nativeAdPos;

    public CatListAdapter(List<String> categoryList, String folderName, Activity context, int nativeAdPos) {
        this.categoryList = categoryList;
        this.context = context;
        this.folderName = folderName;

        this.nativeAdPos = nativeAdPos;

    }

    public static class ImageHolder extends RecyclerView.ViewHolder{
        ImageView catIV;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            catIV = itemView.findViewById(R.id.catIV);
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
                    .inflate(R.layout.item_cat_list, parent, false);

            return new ImageHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == CONTENT_TYPE) {
            RelativeLayout.LayoutParams tabp = new RelativeLayout.LayoutParams(context.getResources().getDisplayMetrics().widthPixels * 327 / 1080,
                    context.getResources().getDisplayMetrics().heightPixels * 455 / 1920);
            holder.itemView.setLayoutParams(tabp);

            Glide.with(context).
                    load(Uri.parse("file:///android_asset/" + Utills.mFolderName + "/" + folderName + "/" + categoryList.get(position)))
                    .into(((ImageHolder)holder).catIV);

            ((ImageHolder)holder).catIV.setOnClickListener(v -> {
                Intent intent = new Intent(context, PagerPreviewActivity.class);
                intent.putStringArrayListExtra("wallpapers", (ArrayList<String>) categoryList);
                intent.putExtra("position", position);
                intent.putExtra("prefix", "file:///android_asset/" + Utills.mFolderName + "/" + folderName + "/");
                context.startActivity(intent);
                Animatee.animateSlideUp(context);
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
