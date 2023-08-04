package com.example.assignmentfull_server.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.assignmentfull_server.R;
import com.example.assignmentfull_server.adapter.ApiNasaAdapter;
import com.example.assignmentfull_server.api.ApiServer;
import com.example.assignmentfull_server.model.DataNasa;
import com.example.assignmentfull_server.model.DataServer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataMyApiActivity extends AppCompatActivity {
    private List<DataNasa> nasaList;

    private ApiNasaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_my_api);
        initView();
    }

    private void initView() {
        nasaList = new ArrayList<>();
        adapter = new ApiNasaAdapter(this);
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData();
    }

    private void getData() {
        ApiServer.apiServer.getData().enqueue(new Callback<DataServer>() {
            @Override
            public void onResponse(Call<DataServer> call, Response<DataServer> response) {
                nasaList = response.body().getDataNasas();
                adapter.setData(nasaList);
                RecyclerView rcv = findViewById(R.id.rcv_items);
                rcv.setAdapter(adapter);
                Toast.makeText(DataMyApiActivity.this, "Get data from my api success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DataServer> call, Throwable t) {
                Toast.makeText(DataMyApiActivity.this, "Get data from my api false", Toast.LENGTH_SHORT).show();
            }
        });
    }
}