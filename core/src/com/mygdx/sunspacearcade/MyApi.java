package com.mygdx.sunspacearcade;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyApi {
    @GET("/arcade.php")
    Call<List<RecordFromDB>> sendData(@Query("name") String name, @Query("score") int score);

    @GET("/arcade.php")
    Call<List<RecordFromDB>> readData();
}
