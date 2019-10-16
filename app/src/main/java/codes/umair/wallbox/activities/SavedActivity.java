package codes.umair.wallbox.activities;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

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
import umairayub.madialog.MaDialog;
import umairayub.madialog.MaDialogListener;

/*
 Created by Umair Ayub on 16 Oct 2019.
 */
public class SavedActivity extends AppCompatActivity implements SavedImagesAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    ArrayList<String> urlList = new ArrayList<>();
    File[] filesList;
    SavedImagesAdapter imageListAdapter;
    Context ctx = SavedActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        recyclerView = findViewById(R.id.saved_rv);

        getImageUrlsFromSDCard();


        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        imageListAdapter = new SavedImagesAdapter(this, urlList);
        imageListAdapter.setClickListener(this);
        recyclerView.setAdapter(imageListAdapter);

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
                                try {
                                    SetWallpaper(ctx, resource);
                                    Snackbar.make(recyclerView, "Wallpaper Changed", Snackbar.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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

    public void SetWallpaper(Context context, Bitmap bitmap) throws IOException {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wallpaperManager.setBitmap(bitmap, null, true);
            return;
        }
        wallpaperManager.setBitmap(bitmap);
    }
}
