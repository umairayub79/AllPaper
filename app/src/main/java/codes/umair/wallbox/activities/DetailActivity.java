package codes.umair.wallbox.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import codes.umair.wallbox.R;

import static codes.umair.wallbox.activities.MainActivity.EXTRA_CREATOR;
import static codes.umair.wallbox.activities.MainActivity.EXTRA_LIKES;
import static codes.umair.wallbox.activities.MainActivity.EXTRA_SIZE;
import static codes.umair.wallbox.activities.MainActivity.EXTRA_URL;
import static codes.umair.wallbox.activities.MainActivity.EXTRA_VIEWS;


public class DetailActivity extends AppCompatActivity {

    private ImageView img;
    private TextView tv_likes, tv_creator, tv_views, tv_imgSize;
    private BottomSheetDialog bottomSheetDialog;
    private Button btnOpenDialog, btnSave, btnSetAsWall;
    Context ctx = DetailActivity.this;

    private int likeCount;
    private int viewsCount;

    private String creatorName;
    private String imgSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        img = findViewById(R.id.imageViewDetail);
        btnOpenDialog = findViewById(R.id.btnDialog);

        Intent i = getIntent();
        likeCount = i.getIntExtra(EXTRA_LIKES, 0);
        viewsCount = i.getIntExtra(EXTRA_VIEWS, 0);
        imgSize = i.getStringExtra(EXTRA_SIZE);
        creatorName = i.getStringExtra(EXTRA_CREATOR);
        String imgUrl = i.getStringExtra(EXTRA_URL);

        Glide.with(this).load(imgUrl).centerInside().into(img);

        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

    }

    public void openDialog() {
        bottomSheetDialog = new BottomSheetDialog(ctx, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.details_dialog);
        tv_creator = bottomSheetDialog.findViewById(R.id.tv_user);
        tv_likes = bottomSheetDialog.findViewById(R.id.tv_likes);
        tv_views = bottomSheetDialog.findViewById(R.id.tv_views);
        tv_imgSize = bottomSheetDialog.findViewById(R.id.tv_imgSize);
        btnSave = bottomSheetDialog.findViewById(R.id.btn_save);
        btnSetAsWall = bottomSheetDialog.findViewById(R.id.btn_setWallpaper);

        tv_creator.setText(creatorName);
        tv_likes.setText(likeCount + " likes");
        tv_views.setText(viewsCount + " views");
        tv_imgSize.setText("Size " + imgSize);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSetAsWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        bottomSheetDialog.show();

    }
}
