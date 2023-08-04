package com.example.assignmentfull_server.api;

import com.example.assignmentfull_server.model.DataNasa;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("planetary/apod")
    Call<DataNasa> getNasaApod(@Query("api_key") String apiKey, @Query("date") String date);
}
