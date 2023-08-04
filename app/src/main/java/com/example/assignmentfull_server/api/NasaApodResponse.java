package com.example.assignmentfull_server.api;

import com.google.gson.annotations.SerializedName;

public class NasaApodResponse {
    @SerializedName("url")
    private String imageUrl;

    // Các getters và setters cho imageUrl


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
