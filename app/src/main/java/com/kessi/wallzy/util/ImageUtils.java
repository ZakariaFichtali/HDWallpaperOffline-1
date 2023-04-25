package com.kessi.wallzy.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.kessi.wallzy.R;

import java.io.File;
import java.io.FileOutputStream;

public class ImageUtils {

    public static String WALLPAPER_FOLDER = Environment.getExternalStorageDirectory().toString().concat("/Download/").concat("HD Wallpaper");

    public static void asyncSave(Context context, Bitmap bitmap, String path) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            AlertDialog alertDialog;
            String errMsg;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
                View view = layoutInflaterAndroid.inflate(R.layout.dialog_loading, null);
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(view);

                final CardView alertLay = view.findViewById(R.id.alertLay);

                alertDialog = alert.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                alertDialog.setCancelable(false);

                Render render = new Render(context);
                render.setAnimation(KSUtil.ZoomIn(alertLay));
                render.setDuration(400);
                render.start();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Bitmap image = bitmap;
//                    Calendar c = Calendar.getInstance();
//                    SimpleDateFormat df = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
//                    String formattedDate = df.format(c.getTime());
//                    String fileName = formattedDate.replaceAll(":", "-").concat(".png");
                    String fileName = path.substring(path.lastIndexOf("/") + 1);
//                    Log.e( "doInBackground: ", fileName);
                    File wallFolder = new File(WALLPAPER_FOLDER);
                    if (!wallFolder.exists()) {
                        wallFolder.mkdirs();
                    }
                    File photoFile = new File(wallFolder, fileName);
                    image.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(photoFile));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    errMsg = ex.getMessage();
                } catch (OutOfMemoryError err) {
                    err.printStackTrace();
                    errMsg = err.getMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void file) {
                super.onPostExecute(file);
                alertDialog.dismiss();
                Toast.makeText(context, "Wallpaper Saved Successfully...", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    public static void asyncSavenShare(Context context, Bitmap bitmap) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            AlertDialog alertDialog;
            String errMsg;
            File photoFile;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
                View view = layoutInflaterAndroid.inflate(R.layout.dialog_loading, null);
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(view);

                final CardView alertLay = view.findViewById(R.id.alertLay);

                alertDialog = alert.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                alertDialog.setCancelable(false);

                Render render = new Render(context);
                render.setAnimation(KSUtil.ZoomIn(alertLay));
                render.setDuration(400);
                render.start();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Bitmap image = bitmap;
                    String fileName = "wallpaper.png";
                    File wallFolder = new File(WALLPAPER_FOLDER + "/temp");
                    if (!wallFolder.exists()) {
                        wallFolder.mkdirs();
                    }
                    photoFile = new File(wallFolder, fileName);
                    image.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(photoFile));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    errMsg = ex.getMessage();
                } catch (OutOfMemoryError err) {
                    err.printStackTrace();
                    errMsg = err.getMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void file) {
                super.onPostExecute(file);
                alertDialog.dismiss();
                mShare(photoFile.getAbsolutePath(), context);
            }
        };
        task.execute();
    }

    public static void mShare(String filepath, Context activity) {
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse(String.valueOf(filepath)));
        File file = new File(filepath);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        activity.startActivity(Intent.createChooser(intent, "Share Image using"));
    }
}
