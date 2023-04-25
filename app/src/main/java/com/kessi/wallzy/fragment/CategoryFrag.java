package com.kessi.wallzy.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.kessi.wallzy.CatListActivity;
import com.kessi.wallzy.MyApp;
import com.kessi.wallzy.R;
import com.kessi.wallzy.adapters.CategoryAdapter;
import com.kessi.wallzy.util.AdManager;
import com.kessi.wallzy.util.Utills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CategoryFrag extends Fragment {

    RecyclerView categoryList;
    List<String> categoryArray;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_category, container, false);

        categoryList = rootView.findViewById(R.id.categoryList);
        categoryList.setNestedScrollingEnabled(false);
        new getCatAsync().execute();


        RelativeLayout adContainer = rootView.findViewById(R.id.banner_container);
        MyApp.adManager.showBannerView(getActivity(), adContainer);
        return rootView;
    }

    class getCatAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            categoryArray = new ArrayList<>(Arrays.asList(Utills.getCategories(getActivity())));
//            categorySizeArray = Utills.getCatSize(getActivity());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            categoryList.setLayoutManager(mLayoutManager);

            mAdapter = new CategoryAdapter(categoryArray, getActivity());
            categoryList.setAdapter(mAdapter);
            categoryList.getViewTreeObserver().addOnGlobalLayoutListener(() -> { });

        }
    }


}
