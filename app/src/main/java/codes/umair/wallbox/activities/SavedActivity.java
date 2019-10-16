package codes.umair.wallbox.activities;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import codes.umair.wallbox.R;
import codes.umair.wallbox.adapters.SavedImagesAdapter;

/*
 Created by Umair Ayub on 16 Oct 2019.
 */
public class SavedActivity extends AppCompatActivity implements SavedImagesAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    ArrayList<String> urlList = new ArrayList<>();
    File[] filesList;
    SavedImagesAdapter imageListAdapter;

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
    public void onItemClick(View view, int position) {
        String imgUrl = urlList.get(position);
        Toast.makeText(this, imgUrl, Toast.LENGTH_SHORT).show();
    }
}
