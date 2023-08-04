package com.example.assignmentfull_server.api;

import android.os.AsyncTask;
import android.util.Log;

public class FetchImageTask extends AsyncTask<String, Void, String> {
    public String imageStringBase64;

    @Override
    protected String doInBackground(String... params) {
        String imageUrl = params[0];
        String base64Image = ImageUtils.convertImageToBase64(imageUrl);
//        Log.d("ImageBase64", "onResponse: Image base 64" + base64Image);

        return base64Image;
    }

    @Override
    protected void onPostExecute(String base64Image) {
        // Ở đây, bạn có thể sử dụng base64Image theo ý muốn sau khi đã chuyển đổi xong
//        Log.d("ImageBase64", "onResponse: Image base 64" + base64Image);
        if (base64Image != null) {
            imageStringBase64 = base64Image;
//            Log.d("Image", "onPostExecute: "+ imageStringBase64);
        }

    }
}

