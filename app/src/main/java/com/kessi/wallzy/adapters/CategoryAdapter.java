package com.kessi.wallzy.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.kessi.wallzy.CatListActivity;
import com.kessi.wallzy.R;
import com.kessi.wallzy.util.Utills;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    List<String> categoryList;
    Context context;



    public CategoryAdapter(List<String> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView categoryIV;
        TextView categoryTxt,catSizeTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIV = itemView.findViewById(R.id.categoryIV);
            categoryTxt = itemView.findViewById(R.id.categoryTxt);
            catSizeTxt = itemView.findViewById(R.id.catSizeTxt);
        }
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        CategoryAdapter.ViewHolder vh = new CategoryAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {

        RelativeLayout.LayoutParams tabp = new RelativeLayout.LayoutParams(context.getResources().getDisplayMetrics().widthPixels * 315 / 1080,
                context.getResources().getDisplayMetrics().heightPixels * 280 / 1920);
        holder.itemView.setLayoutParams(tabp);


        Glide.with(context).
                load(Uri.parse("file:///android_asset/" + Utills.getCatThumb(context, categoryList.get(position))))
                .into(holder.categoryIV);
        holder.categoryTxt.setText(categoryList.get(position));

        holder.categoryIV.setOnClickListener(v -> {
            Intent intent = new Intent(context, CatListActivity.class);
            intent.putExtra("folder", categoryList.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


}
