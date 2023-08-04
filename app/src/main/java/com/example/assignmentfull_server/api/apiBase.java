package com.example.assignmentfull_server.api;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class apiBase {
    private static final String BASE_URL = "https://api.nasa.gov/";
    private static Retrofit retrofit;


    private static Retrofit getInstance(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("yyyy-MM-dd").create()))
                    .build();
        }
        return retrofit;
    }

    public static APIInterface getApiInterface() {
        return getInstance().create(APIInterface.class);
    }
}
