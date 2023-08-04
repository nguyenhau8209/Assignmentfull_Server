package com.example.assignmentfull_server;

import static com.example.assignmentfull_server.api.ImageUtils.convertImageToBase64;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.assignmentfull_server.Auth.LoginActivity;
import com.example.assignmentfull_server.api.APIInterface;
import com.example.assignmentfull_server.api.ApiServer;
import com.example.assignmentfull_server.api.FetchImageTask;
import com.example.assignmentfull_server.api.ImageUtils;
import com.example.assignmentfull_server.api.NasaApodResponse;
import com.example.assignmentfull_server.api.apiBase;
import com.example.assignmentfull_server.model.DataNasa;
import com.example.assignmentfull_server.screens.DataMyApiActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    //MainActivity
    private Spinner spYear, spMonth, spDay;
    private TextView tvTitle, tvDate, tvExplanation, tvNotification;

    private DataNasa dataNasa;
    private static final String API_KEY = "ur5tHgmfxdsM5TBC99EOwX6S7vC7b3lkfBARE8df";
    private APIInterface apiInterface;
    String base64UrlHd;
    String base64Url;
    Button button;
    ProgressBar progressBar;

    private String dateSelected, yearSlected, monthSelected, daySelected;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.signout);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

        dataNasa = new DataNasa();
        initView();


    }

    private void initView() {
        spYear = findViewById(R.id.spn_year);
        spMonth = findViewById(R.id.spn_month);
        spDay = findViewById(R.id.spn_date);
        tvTitle = findViewById(R.id.tv_title);
        tvDate = findViewById(R.id.tv_date);
        tvExplanation = findViewById(R.id.tv_explanation);
        tvNotification = findViewById(R.id.tv_notification);
        progressBar = findViewById(R.id.progressBar2);


        List<String> days = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            days.add(String.valueOf(i));
        }
        List<String> months = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            months.add(String.valueOf(i));
        }
        List<String> years = new ArrayList<>();

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= currentYear - 100; i--) {
            years.add(String.valueOf(i));
        }
        days.add(0, "days");
        months.add(0, "months");
        years.add(0, "years");
        ArrayAdapter<String> daysAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);

        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spYear.setAdapter(yearsAdapter);
        spMonth.setAdapter(monthsAdapter);
        spDay.setAdapter(daysAdapter);

        spDay.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spYear.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spMonth.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        findViewById(R.id.btn_get_nasa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApiGetDataFormNasa(API_KEY, dateSelected);
            }
        });

        findViewById(R.id.layout_show_data).setVisibility(View.GONE);

        findViewById(R.id.btn_push_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer();
            }
        });

        findViewById(R.id.btn_get_data_form_my_server).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DataMyApiActivity.class));
            }
        });
    }

    private void sendDataToServer() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (dataNasa.getHdurl() != null) {
                base64UrlHd = convertUrlToBase64(dataNasa.getHdurl());
            } else {
                base64UrlHd = "";
            }
            base64Url = convertUrlToBase64(dataNasa.getUrl());
        }

        dataNasa.setHdurl(base64UrlHd);
        dataNasa.setUrl(base64Url);

        Log.d("sendDataToServer", dataNasa.toString());
        ApiServer.apiServer.postData(dataNasa).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
//                tvNotification.setText("push data to my server successfully");
//                tvNotification.setTextColor(Color.parseColor("#198754"));
                Toast.makeText(MainActivity.this, "Post to my server success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
//                tvNotification.setText("post data to my server failed");
//                tvNotification.setTextColor(Color.RED);
                Toast.makeText(MainActivity.this, "Post to my server false", Toast.LENGTH_SHORT).show();
                Log.d("API", t.getMessage());
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String convertUrlToBase64(String url) {
        byte[] byteInput = url.getBytes();
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        String encodedString = base64Encoder.encodeToString(byteInput);
        return encodedString;
    }

    private void callApiGetDataFormNasa(String apiKey, String dateSelected) {
        progressBar.setVisibility(View.VISIBLE);
        apiInterface = apiBase.getApiInterface();
        apiInterface.getNasaApod(apiKey, dateSelected).enqueue(new Callback<DataNasa>() {
            @Override
            public void onResponse(Call<DataNasa> call, Response<DataNasa> response) {
                dataNasa = response.body();
                findViewById(R.id.layout_show_data).setVisibility(View.VISIBLE);
                tvTitle.setText(dataNasa.getTitle());
                tvDate.setText(dataNasa.getDate());
                tvExplanation.setText(dataNasa.getExplanation());
                ImageView imgHd = findViewById(R.id.img_hd);
                if (dataNasa.getHdurl() != null) {
                    progressBar.setVisibility(View.GONE);
                    Glide.with(MainActivity.this).load(dataNasa.getHdurl()).error(R.drawable.baseline_error_24).into(imgHd);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Glide.with(MainActivity.this).load(dataNasa.getUrl()).error(R.drawable.baseline_error_24).into(imgHd);
                }
//                tvNotification.setText("get data from Nasa successfully");
                Toast.makeText(MainActivity.this, "Get data from Nasa successfully", Toast.LENGTH_SHORT).show();
                tvNotification.setTextColor(Color.parseColor("#198754"));

                Log.d("callApiGetDataFormNasa", response.body().toString());
            }

            @Override
            public void onFailure(Call<DataNasa> call, Throwable t) {
                findViewById(R.id.layout_show_data).setVisibility(View.GONE);
                Log.d("EEE", t.getMessage());
//                tvNotification.setText("get data from Nasa failed");
                Toast.makeText(MainActivity.this, "Get data from Nasa false", Toast.LENGTH_SHORT).show();
                tvNotification.setTextColor(Color.RED);
            }
        });
    }

    private class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            daySelected = spDay.getSelectedItem().toString();
            monthSelected = spMonth.getSelectedItem().toString();
            yearSlected = spYear.getSelectedItem().toString();
            if (!daySelected.equals("days") && !monthSelected.equals("months") && !yearSlected.equals("years")) {
                dateSelected = yearSlected + "-" + monthSelected + "-" + daySelected;
                Log.d("Selected Date", dateSelected);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


}