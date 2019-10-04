package codes.umair.wallbox.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import codes.umair.wallbox.R;

import static codes.umair.wallbox.activities.MainActivity.EXTRA_CREATOR;
import static codes.umair.wallbox.activities.MainActivity.EXTRA_LIKES;
import static codes.umair.wallbox.activities.MainActivity.EXTRA_URL;
import static codes.umair.wallbox.activities.MainActivity.EXTRA_VIEWS;


public class DetailActivity extends AppCompatActivity {

    ImageView img;
    TextView tv_likes, tv_creator, tv_views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        img = findViewById(R.id.imageViewDetail);
//        tv_creator = (TextView) findViewById(R.id.tv_creator);
//        tv_likes = (TextView) findViewById(R.id.tv_likes);
//        tv_views = (TextView) findViewById(R.id.tv_view);

        Intent i = getIntent();
        int likeCount = i.getIntExtra(EXTRA_LIKES, 0);
        int viewsCount = i.getIntExtra(EXTRA_VIEWS, 0);
        String creatorName = i.getStringExtra(EXTRA_CREATOR);
        String imgUrl = i.getStringExtra(EXTRA_URL);

        Glide.with(this).load(imgUrl).centerInside().into(img);
//        tv_views.setText("Views; " + viewsCount);
//        tv_likes.setText("Likes: " + likeCount);
//        tv_creator.setText(creatorName);


    }
}
