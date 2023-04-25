package com.kessi.wallzy.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kessi.wallzy.R;
import com.kessi.wallzy.adapters.DownloadAdapter;
import com.kessi.wallzy.util.ImageUtils;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class DownloadFrag extends Fragment {

    private File[] files;
    LinearLayout emptyStatus;
    RecyclerView downloadList;
    DownloadAdapter downloadAdapter;
    ArrayList<String> myWallapaer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_download, container, false);
        emptyStatus = rootView.findViewById(R.id.emptyStatus);
        downloadList = rootView.findViewById(R.id.downloadList);
        downloadList.setNestedScrollingEnabled(false);
        new getDownAsync().execute();
        return rootView;
    }

    public class getDownAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            myWallapaer = new ArrayList<>();
            myWallapaer = getMyWallpaper();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (myWallapaer != null &&  myWallapaer.size() != 0){
                emptyStatus.setVisibility(View.GONE);
            }else {
                emptyStatus.setVisibility(View.VISIBLE);
            }
            downloadAdapter = new DownloadAdapter(myWallapaer, DownloadFrag.this,7);
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (downloadAdapter.getItemViewType(position)) {
                        case 1:
                            return 1;
                        case 2:
                            return gridLayoutManager.getSpanCount();
                        default:
                            return -1;
                    }
                }
            });
            downloadList.setLayoutManager(gridLayoutManager);
            downloadList.setItemAnimator(new DefaultItemAnimator());
            downloadList.setAdapter(downloadAdapter);
        }
    }

    private ArrayList<String> getMyWallpaper() {
        ArrayList<String> mediaList = new ArrayList<>();
        String path = ImageUtils.WALLPAPER_FOLDER;
        File targetPath = new File(path);

        files = targetPath.listFiles();

        if (files != null && files.length != 0) {
            try {
                Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);

                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (!file.getAbsolutePath().contains("temp")) {
                        mediaList.add(file.getAbsolutePath());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mediaList;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        downloadAdapter.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 10) {
            downloadAdapter.notifyDataSetChanged();
            new getDownAsync().execute();
        }
    }
}
