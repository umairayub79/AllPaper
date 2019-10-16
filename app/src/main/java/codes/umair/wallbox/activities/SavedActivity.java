package codes.umair.wallbox.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import codes.umair.wallbox.R;

/*
 Created by Umair Ayub on 16 Oct 2019.
 */
public class SavedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        recyclerView = findViewById(R.id.saved_rv);
    }

}
