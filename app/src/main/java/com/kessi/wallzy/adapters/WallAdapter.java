package com.kessi.wallzy.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kessi.wallzy.PagerPreviewActivity;
import com.kessi.wallzy.R;
import com.kessi.wallzy.util.Animatee;
import com.kessi.wallzy.util.ImageUtils;
import com.kessi.wallzy.util.Utills;

import java.util.ArrayList;
import java.util.List;

public class WallAdapter extends RecyclerView.Adapter<WallAdapter.ImageHolder>{
    List<String> categoryList;
    Context context;

    public WallAdapter(List<String> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
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

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wallpaper, parent, false);

        return new ImageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).
                load(Uri.parse("file:///android_asset/" + Utills.mFolderName+ "/" + categoryList.get(position)))
                .into(holder.wallIV);

        holder.wallIV.setOnClickListener(v -> {
            Intent intent = new Intent(context, PagerPreviewActivity.class);
            intent.putStringArrayListExtra("wallpapers", (ArrayList<String>) categoryList);
            intent.putExtra("position", position);
            intent.putExtra("prefix", "file:///android_asset/" + Utills.mFolderName+ "/");
            context.startActivity(intent);
            Animatee.animateSlideUp(context);
        });

        holder.downloadIV.setOnClickListener(v -> {
            if (Utills.hasPermissions(context, Utills.permissions)) {
                ActivityCompat.requestPermissions((Activity) context, Utills.permissions, Utills.perRequest);
            }else {
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
    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }


}
