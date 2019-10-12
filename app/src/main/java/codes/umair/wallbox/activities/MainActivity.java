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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import spencerstudios.com.jetdblib.JetDB;

public class MainActivity extends AppCompatActivity implements ImageListAdapter.OnItemClickListener {

    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_CREATOR = "creatorName";
    public static final String EXTRA_SIZE = "imgSize";
    public static final String EXTRA_LIKES = "likeCount";
    public static final String EXTRA_VIEWS = "viewsCount";

    Context ctx = MainActivity.this;
    private ScrollListener scrollListener;
    private APIInterface apiInterface;
    private RecyclerView rv;
    private Button btnApplyFilters;
    private SwipeRefreshLayout mSwipeRefresher;
    private ArrayList<Post> hits;
    private ImageListAdapter imageListAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private String currentQuery = "";
    private ConstraintLayout root;

    // Filter vars
    private Switch safeSearchSwitch;
    private Spinner ImageTypeSpinner;
    private Spinner OrderSpinner;
    private Spinner CategorySpinner;
    private String[] itemsOrder = {"latest", "popular"};
    private String[] itemsType = {"all", "photo", "illustration", "vector"};
    private String[] itemsCategory = {"fashion", "nature", "backgrounds", "science", "education", "people", "feelings", "religion", "health", "places", "animals", "industry", "food", "computer", "sports", "transportation", "travel", "buildings", "business", "music"};
    private boolean is_safe_search_on;

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
            LoadImages(1, currentQuery, is_safe_search_on, JetDB.getString(ctx, "selected_order", ""), JetDB.getString(ctx, "selected_type", ""), JetDB.getString(ctx, "selected_category", ""));

        } else {
            initSnackbar(R.string.no_internet);
        }

        mSwipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable()) {
                    LoadImages(1, currentQuery, is_safe_search_on, JetDB.getString(ctx, "selected_order", ""), JetDB.getString(ctx, "selected_type", ""), JetDB.getString(ctx, "selected_category", ""));
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
                    LoadImages(1, currentQuery, is_safe_search_on, JetDB.getString(ctx, "selected_order", ""), JetDB.getString(ctx, "selected_type", ""), JetDB.getString(ctx, "selected_category", ""));
                } else initSnackbar(R.string.no_internet);
            }
        });
        snackbar.show();
    }

    public void LoadImages(int page, String query, boolean is_safe_search_on, String result_order, String result_image_type, String result_category) {

        HashMap<String, String> map = new HashMap<>();
        map.put("key", "13799911-62a795ec2e29137d307467722");
        map.put("orientation", "vertical");
        map.put("per_page", "200");
        map.put("page", String.valueOf(page));
        map.put("order", result_order);
        map.put("safesearch", String.valueOf(is_safe_search_on));
        map.put("image_type", result_image_type);
        map.put("category", result_category);
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
                LoadImages(page, currentQuery, is_safe_search_on, JetDB.getString(ctx, "selected_order", ""), JetDB.getString(ctx, "selected_type", ""), JetDB.getString(ctx, "selected_category", ""));
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
                LoadImages(1, currentQuery, is_safe_search_on, JetDB.getString(ctx, "selected_order", ""), JetDB.getString(ctx, "selected_type", ""), JetDB.getString(ctx, "selected_category", ""));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newQuery) {
                if (newQuery.equals("")) {
                    resetImageList();
                    currentQuery = newQuery;
                    LoadImages(1, "", is_safe_search_on, JetDB.getString(ctx, "selected_order", ""), JetDB.getString(ctx, "selected_type", ""), JetDB.getString(ctx, "selected_category", ""));
                }
                return true;
            }

        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            openFilterDialog();

        }
        return super.onOptionsItemSelected(item);
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

    private void openFilterDialog() {
        bottomSheetDialog = new BottomSheetDialog(ctx, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.filter_dialog);
        safeSearchSwitch = bottomSheetDialog.findViewById(R.id.safe_search_switch);
        ImageTypeSpinner = bottomSheetDialog.findViewById(R.id.image_type_spinner);
        OrderSpinner = bottomSheetDialog.findViewById(R.id.image_order_spinner);
        CategorySpinner = bottomSheetDialog.findViewById(R.id.image_category_spinner);
        btnApplyFilters = bottomSheetDialog.findViewById(R.id.btn_apply);


        safeSearchSwitch.setChecked(is_safe_search_on);

//      Adapters
        ArrayAdapter orderAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, itemsOrder);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        OrderSpinner.setAdapter(orderAdapter);

        ArrayAdapter typeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, itemsType);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ImageTypeSpinner.setAdapter(typeAdapter);

        ArrayAdapter categoryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, itemsCategory);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CategorySpinner.setAdapter(categoryAdapter);


        //Really could'nt think of any better solution//////////////////////
        for (int i = 0; i < itemsOrder.length; i++) {
            if (itemsOrder[i].equals(JetDB.getString(ctx, "selected_order", ""))) {
                OrderSpinner.setSelection(i);
            }
        }
        for (int i = 0; i < itemsCategory.length; i++) {
            if (itemsCategory[i].equals(JetDB.getString(ctx, "selected_category", ""))) {
                CategorySpinner.setSelection(i);
            }
        }
        for (int i = 0; i < itemsType.length; i++) {
            if (itemsType[i].equals(JetDB.getString(ctx, "selected_type", ""))) {
                ImageTypeSpinner.setSelection(i);
            }
        }
        //////////////////////////////


        OrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                JetDB.putString(ctx, itemsOrder[i], "selected_order");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ImageTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                JetDB.putString(ctx, itemsType[i], "selected_type");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        CategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                JetDB.putString(ctx, itemsCategory[i], "selected_category");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        safeSearchSwitch.setChecked(is_safe_search_on);
        safeSearchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                is_safe_search_on = state;
            }
        });

        btnApplyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadImages(1, currentQuery, is_safe_search_on, JetDB.getString(ctx, "selected_order", ""), JetDB.getString(ctx, "selected_type", ""), JetDB.getString(ctx, "selected_category", ""));
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();

    }

    @Override
    protected void onDestroy() {
        JetDB.putString(ctx, "", "selected_category");
        JetDB.putString(ctx, "", "selected_order");
        JetDB.putString(ctx, "", "selected_type");

        super.onDestroy();
    }
}