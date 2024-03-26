package com.example.weatherapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ForecastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        RecyclerView recyclerView = findViewById(R.id.forecastList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String[] time = this.getIntent().getStringArrayExtra("time");
        double[] temp = this.getIntent().getDoubleArrayExtra("temp");
        double[] appTemp = this.getIntent().getDoubleArrayExtra("appTemp");
        int[] weather = this.getIntent().getIntArrayExtra("weather");
        recyclerView.setAdapter(new ForecastAdapter(time, temp, appTemp, weather));
    }

}