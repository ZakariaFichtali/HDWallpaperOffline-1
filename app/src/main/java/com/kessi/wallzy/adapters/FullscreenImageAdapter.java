package com.kessi.wallzy.adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.kessi.wallzy.R;

import java.util.ArrayList;

public class FullscreenImageAdapter extends PagerAdapter {
    Activity activity;
    ArrayList<String> imageList;
    String prefixPath;

    public FullscreenImageAdapter(Activity activity, ArrayList<String> imageList, String prefixPath) {
        this.activity = activity;
        this.imageList = imageList;
        this.prefixPath = prefixPath;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.preview_list_item, container, false);

        PhotoView imageView = itemView.findViewById(R.id.imageView);

        if (prefixPath.equals("")) {
            Glide.with(activity)
                    .load(prefixPath + imageList.get(position))
                    .into(imageView);
        }else {
            Glide.with(activity)
                    .load(Uri.parse(prefixPath + imageList.get(position)))
                    .into(imageView);
        }
        container.addView(itemView);

        return itemView;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }
}
