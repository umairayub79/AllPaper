package codes.umair.wallbox.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;

import codes.umair.wallbox.R;
import codes.umair.wallbox.adapters.ImageListAdapter;
import codes.umair.wallbox.api.APIClient;
import codes.umair.wallbox.api.APIInterface;
import codes.umair.wallbox.listener.ScrollListener;
import codes.umair.wallbox.models.Post;
import codes.umair.wallbox.models.PostList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ImageListAdapter.OnItemClickListener {

    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_CREATOR = "creatorName";
    public static final String EXTRA_SIZE = "imgSize";
    public static final String EXTRA_LIKES = "likeCount";
    public static final String EXTRA_VIEWS = "viewsCount";

    private ScrollListener scrollListener;
    private APIInterface apiInterface;
    private RecyclerView rv;
    private SwipeRefreshLayout mSwipeRefresher;
    private ConstraintLayout root;
    private ArrayList<Post> hits;
    private ImageListAdapter imageListAdapter;
    private String currentQuery = "";
    /*
    Created by Umair Ayub on 17 Sept 2019.
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        hits = new ArrayList<>();
        rv.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rv.setLayoutManager(mLayoutManager);
        imageListAdapter = new ImageListAdapter(this, hits);
        imageListAdapter.setOnItemClickListener(this);
        rv.setAdapter(imageListAdapter);

        initScrollListener(mLayoutManager);


        if (isNetworkAvailable()) {
            LoadImages(1, currentQuery);
        } else {
            initSnackbar(R.string.no_internet);
        }

        mSwipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable()) {
                    LoadImages(1, currentQuery);
                } else {
                    initSnackbar(R.string.no_internet);
                }
            }
        });

    }

    public void initViews() {
        rv = findViewById(R.id.rv);
        mSwipeRefresher = findViewById(R.id.mSwipeRefresh);
        root = findViewById(R.id.root);

    }

    private void initSnackbar(int messageId) {
        mSwipeRefresher.setRefreshing(false);
        Snackbar snackbar = Snackbar.make(rv, messageId, Snackbar.LENGTH_INDEFINITE).setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    resetImageList();
                    mSwipeRefresher.setRefreshing(true);
                    LoadImages(1, currentQuery);
                } else initSnackbar(R.string.no_internet);
            }
        });
        snackbar.show();
    }

    public void LoadImages(int page, String query) {

        HashMap<String, String> map = new HashMap<>();

        map.put("key", "13799911-62a795ec2e29137d307467722");
        map.put("orientation", "vertical");
        map.put("per_page", "200");
        map.put("page", String.valueOf(page));
        if (!query.equals("")) {
            map.put("q", query);
        }

        mSwipeRefresher.setRefreshing(true);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<PostList> call = apiInterface.getImageResults(map);
        call.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                PostList postList = response.body();
                addImagesToList(postList);
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                initSnackbar(R.string.error);
            }
        });

    }

    private void addImagesToList(PostList response) {
        mSwipeRefresher.setRefreshing(false);
        int position = hits.size();
        hits.addAll(response.getPosts());
        imageListAdapter.notifyItemRangeInserted(position, position + 200);
        if (hits.isEmpty()) {
            initSnackbar(R.string.no_results);
        }

    }

    private void initScrollListener(LinearLayoutManager mLayoutManager) {
        scrollListener = new ScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page) {
                mSwipeRefresher.setRefreshing(true);
                LoadImages(page, currentQuery);
            }
        };
        rv.addOnScrollListener(scrollListener);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                resetImageList();
                currentQuery = query;
                LoadImages(1, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newQuery) {
                if (newQuery.equals("")) {
                    resetImageList();
                    currentQuery = newQuery;
                    LoadImages(1, newQuery);
                }
                return true;
            }

        });
        return true;
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        Post clickedItem = hits.get(position);

        detailIntent.putExtra(EXTRA_URL, clickedItem.getFullHDURL());
        detailIntent.putExtra(EXTRA_CREATOR, clickedItem.getUser());
        detailIntent.putExtra(EXTRA_SIZE, "W " + clickedItem.getImageWidth() + " x H " + clickedItem.getImageHeight());
        detailIntent.putExtra(EXTRA_LIKES, clickedItem.getLikes());
        detailIntent.putExtra(EXTRA_VIEWS, clickedItem.getViews());

        startActivity(detailIntent);
    }

    private void resetImageList() {
        hits.clear();
        scrollListener.resetCurrentPage();
        imageListAdapter.notifyDataSetChanged();
    }
}