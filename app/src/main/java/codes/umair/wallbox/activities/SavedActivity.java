package codes.umair.wallbox.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import codes.umair.wallbox.R;
import codes.umair.wallbox.adapters.SavedImagesAdapter;
import codes.umair.wallbox.utils.Util;
import umairayub.madialog.MaDialog;
import umairayub.madialog.MaDialogListener;

/*
 Created by Umair Ayub on 16 Oct 2019.
 */
public class SavedActivity extends AppCompatActivity implements SavedImagesAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private TextView textView_noImgs;
    ArrayList<String> urlList = new ArrayList<>();
    File[] filesList;
    SavedImagesAdapter imageListAdapter;
    Context ctx = SavedActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        recyclerView = findViewById(R.id.saved_rv);
        textView_noImgs = findViewById(R.id.tv_chk_img);


        getImageUrlsFromSDCard();


        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        imageListAdapter = new SavedImagesAdapter(this, urlList);
        imageListAdapter.setClickListener(this);
        recyclerView.setAdapter(imageListAdapter);

        if (urlList.isEmpty()) {
            textView_noImgs.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void getImageUrlsFromSDCard() {
        File file = new File(Environment.getExternalStorageDirectory(), "AllPaper");
        if (file.isDirectory()) {
            filesList = file.listFiles();
            for (int i = 0; i < filesList.length; i++) {
                urlList.add(filesList[i].getAbsolutePath());
            }
        }
    }

    @Override
    public void onItemClick(View view, final int position) {

        new MaDialog.Builder(this)
                .setTitle("Set as Wallpaper?")
                .setMessage("Do you want to set this image as wallpaper?")
                .setPositiveButtonListener(new MaDialogListener() {
                    @Override
                    public void onClick() {
                        Glide.with(ctx).asBitmap().load(urlList.get(position)).into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                new AsyncTaskRunner().execute(resource);
                                    Snackbar.make(recyclerView, "Wallpaper Changed", Snackbar.LENGTH_LONG).show();

                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
                    }
                })
                .setNegativeButtonListener(new MaDialogListener() {
                    @Override
                    public void onClick() {

                    }
                })
                .setNegativeButtonText("No")
                .setPositiveButtonText("Yes")
                .build();

    }


    private class AsyncTaskRunner extends AsyncTask<Bitmap, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Bitmap... p1) {
            // TODO: Implement this method

            try {
                Util.SetWallpaper(ctx, p1[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(ctx,
                    "Just a Sec",
                    "Changing Wallpaper");


        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
        }


    }
}

