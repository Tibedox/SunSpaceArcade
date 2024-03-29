package com.mygdx.sunspacearcade;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyApi {
    @FormUrlEncoded
    @POST("/arcade.php")
    Call<List<RecordFromDB>> sendData(@Field("name") String name, @Field("score") int score);

    @FormUrlEncoded
    @POST("/arcade.php")
    Call<List<RecordFromDB>> readData();

}
