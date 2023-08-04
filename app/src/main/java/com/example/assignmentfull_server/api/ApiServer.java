package com.example.assignmentfull_server.api;

import com.example.assignmentfull_server.model.DataApi;
import com.example.assignmentfull_server.model.DataNasa;
import com.example.assignmentfull_server.model.DataServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface ApiServer {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();


    ApiServer apiServer = new Retrofit.Builder()
            .baseUrl(localHost.URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServer.class);

    @POST("api/add")
    Call<Void> postData(@Body DataNasa dataNasa);

    @GET("/api")
    Call<DataServer> getData();

    @GET("api/details/{id}")
    Call<DataApi> getDataDetails(@Part("id") String id);

    @PUT("api/update/{id}")
    Call<Void> updateData(@Part("id") String id, @Body DataNasa dataNasa);

    @DELETE("api/delete/{id}")
    Call<Void> deleteData(@Part("id") String id);
}
