package codes.umair.wallbox.activities;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;

import codes.umair.wallbox.R;
import codes.umair.wallbox.adapters.ImageListAdapter;
import codes.umair.wallbox.api.APIClient;
import codes.umair.wallbox.api.APIInterface;
import codes.umair.wallbox.models.Post;
import codes.umair.wallbox.models.PostList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    APIInterface apiInterface;
    RecyclerView rv;
    ImageListAdapter adapter;
    HashMap<String, String> map = new HashMap<>();

    /*
 Created by Umair Ayub on 17 Sept 2019.
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = (RecyclerView) findViewById(R.id.rv);

//        // set a StaggeredGridLayoutManager with 3 number of columns and vertical orientation
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
//        rv.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rv.setLayoutManager(manager);
        //after you set this, you'll find there is a blank at the top when you scroll to the top. continue to set this

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).invalidateSpanAssignments();
            }
        });
        map.put("key", "13645069-e2a9dcafe9782433c1a9c88d3");
        map.put("q", "wallpaper");
        map.put("orientation", "vertical");
        map.put("image_type", "all");


        if (isNetworkAvailable()) {
            apiInterface = APIClient.getClient().create(APIInterface.class);
            Call<PostList> call = apiInterface.getImageResults(map);
            call.enqueue(new Callback<PostList>() {
                @Override
                public void onResponse(Call<PostList> call, Response<PostList> response) {
                    PostList postList = response.body();
                    List<Post> hits = postList.getPosts();
                    adapter = new ImageListAdapter(getApplicationContext(), hits);
                    rv.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<PostList> call, Throwable t) {
                    Snackbar snackbar = Snackbar
                            .make(rv, "Error requesting server, Please try again later", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                }
            });
        } else {
            rv.setVisibility(View.INVISIBLE);
            Snackbar snackbar = Snackbar
                    .make(rv, "No connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}