package codes.umair.wallbox.api;

import java.util.Map;

import codes.umair.wallbox.models.PostList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/*
 Created by Umair Ayub on 17 Sept 2019.
 */
public interface APIInterface {

    @GET("/api/")
    Call<PostList> getImageResults(@QueryMap Map<String, String> parameter);
}


