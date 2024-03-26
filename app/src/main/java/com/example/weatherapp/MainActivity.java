package com.example.weatherapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.weatherAPI.CurrentNode;
import com.example.weatherapp.weatherAPI.Msg;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "MainActivity";
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Gson gson = new Gson();
    private final CitiesDbOpenHelper citiesDbOpenHelper = new CitiesDbOpenHelper(this);
    private final SaveDbOpenHelper saveDbOpenHelper = new SaveDbOpenHelper(this);
    private int curPos;
    String strMsg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> cityList = new ArrayList<>();
        SQLiteDatabase db = citiesDbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("russia_cities_237", new String[]{"city_ru", "admin_name_ru"},
                null, null, null, null, null, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            cityList.add(cursor.getString(0) + ",\n" + cursor.getString(1));
            cursor.moveToNext();
        }
        db.close();

        Spinner spinner = findViewById(R.id.citySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cityList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        curPos = pos;
        connect();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void reload(View view) {
        connect();
    }

    private void showCurWeather(CurrentNode weather) {
        BackgroundView backgroundView = findViewById(R.id.backgroundView);
        backgroundView.getWeather(weather);
        backgroundView.invalidate();

        BackgroundAnimation animation = new BackgroundAnimation(backgroundView);
        animation.setDuration(5000 > 25000-1000*weather.wind_speed_10m
                ? 5000 : Math.round(25000-1000*weather.wind_speed_10m));
        animation.setRepeatCount(Animation.INFINITE);
        backgroundView.startAnimation(animation);

        TextView curTempTv = findViewById(R.id.curTempTv);
        TextView curAppTempTv = findViewById(R.id.curAppTempTv);
        TextView curWeatherTv = findViewById(R.id.curWeatherTv);
        TextView curHumidityTv = findViewById(R.id.curHumidityTv);
        TextView curSpeedTv = findViewById(R.id.curSpeedTv);
        TextView curCloudTv = findViewById(R.id.curCloud);
        TextView curSeaPressureTv = findViewById(R.id.curSeaPressureTv);
        TextView curSurfPressureTv = findViewById(R.id.curSurfPressureTv);
        TextView curGustsTv = findViewById(R.id.curGustsTv);

        curTempTv.setText(weather.temperature_2m+"°C");
        curAppTempTv.setText("Ощущается как " + weather.apparent_temperature+"°C");
        curCloudTv.setText("Облачность: " + weather.cloud_cover+"%");
        curSpeedTv.setText("Скорость ветра: " + weather.wind_speed_10m+"км/ч");
        curHumidityTv.setText("Влажность: " + weather.relative_humidity_2m+"%");
        curSeaPressureTv.setText("Давление на уровне моря: " + weather.pressure_msl+"гПа");
        curSurfPressureTv.setText("Поверхностное давление: " + weather.surface_pressure+"гПа");
        curGustsTv.setText("Порывы ветра: " + weather.wind_gusts_10m+"км/ч");
        switch(weather.weather_code) {
            case 0: curWeatherTv.setText(R.string.weather_0); break;
            case 1: curWeatherTv.setText(R.string.weather_1); break;
            case 2: curWeatherTv.setText(R.string.weather_2); break;
            case 3: curWeatherTv.setText(R.string.weather_3); break;
            case 45: case 48: curWeatherTv.setText(R.string.weather_4548); break;
            case 51: case 53: case 55: curWeatherTv.setText(R.string.weather_515355); break;
            case 56: case 57: curWeatherTv.setText(R.string.weather_5657); break;
            case 61: case 63: case 65: curWeatherTv.setText(R.string.weather_616365); break;
            case 66: case 67: curWeatherTv.setText(R.string.weather_6667); break;
            case 71: case 73: case 75: curWeatherTv.setText(R.string.weather_717375); break;
            case 77: curWeatherTv.setText(R.string.weather_77); break;
            case 80: case 81: case 82: curWeatherTv.setText(R.string.weather_808182); break;
            case 85: case 86: curWeatherTv.setText(R.string.weather_8586); break;
            case 95: curWeatherTv.setText(R.string.weather_95); break;
            case 9699: curWeatherTv.setText(R.string.weather_9699); break;
        }
        curTempTv.setTextColor((weather.is_day == 1) ? Color.BLACK : Color.parseColor("#FFF390"));
        curAppTempTv.setTextColor((weather.is_day == 1) ? Color.BLACK : Color.parseColor("#FFF390"));
        curWeatherTv.setTextColor((weather.is_day == 1) ? Color.BLACK : Color.parseColor("#FFF390"));
        curHumidityTv.setTextColor((weather.is_day == 1) ? Color.BLACK : Color.parseColor("#FFF390"));
        curSpeedTv.setTextColor((weather.is_day == 1) ? Color.BLACK : Color.parseColor("#FFF390"));
        curCloudTv.setTextColor((weather.is_day == 1) ? Color.BLACK : Color.parseColor("#FFF390"));
        curSeaPressureTv.setTextColor((weather.is_day == 1) ? Color.BLACK : Color.parseColor("#FFF390"));
        curSurfPressureTv.setTextColor((weather.is_day == 1) ? Color.BLACK : Color.parseColor("#FFF390"));
        curGustsTv.setTextColor((weather.is_day == 1) ? Color.BLACK : Color.parseColor("#FFF390"));
    }

    public void connect() {
        executor.execute(() -> {
            try {
                SQLiteDatabase db = citiesDbOpenHelper.getReadableDatabase();
                Cursor cursor = db.query("russia_cities_237", new String[]{"lat", "lng"},
                        null, null, null, null, null, null);
                cursor.moveToPosition(curPos);
                double lat = cursor.getDouble(0);
                double lng = cursor.getDouble(1);
                db.close();

                URL url = new URL("https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lng
                        + "&current=temperature_2m,relative_humidity_2m,apparent_temperature,is_day,weather_code,"
                        + "cloud_cover,pressure_msl,surface_pressure,wind_speed_10m,wind_gusts_10m&hourly=temperature_2m,"
                        + "apparent_temperature,weather_code" + "&timezone=" + TimeZone.getDefault().getID());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int responseCode = connection.getResponseCode();
                Log.i(TAG, "Server responded with: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    BufferedReader bufReader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder strBuilder = new StringBuilder();
                    String line;
                    while ((line = bufReader.readLine()) != null) {
                        strBuilder.append(line);
                    }
                    bufReader.close();
                    strMsg = strBuilder.toString();

                    Log.d(TAG, strMsg);
                    Msg msg = gson.fromJson(strMsg, Msg.class);
                    handler.post(() -> {
                        Toast.makeText(MainActivity.this, lat+", "+lng, Toast.LENGTH_LONG).show();
                        showCurWeather(msg.current);
                    });
                }
                else {
                    handler.post(() -> {
                        Toast.makeText(MainActivity.this, "Ошибка соединения " + responseCode, Toast.LENGTH_LONG).show();
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                handler.post(() -> {
                    Toast.makeText(MainActivity.this, "Ошибка соединения", Toast.LENGTH_LONG).show();
                });
            }

        });
    }

    public void showForecast(View view) {
        if (strMsg == "")
            return;
        Msg msg = gson.fromJson(strMsg, Msg.class);
        Intent intent = new Intent(this, ForecastActivity.class);
        intent.putExtra("time", msg.hourly.time);
        intent.putExtra("temp", msg.hourly.temperature_2m);
        intent.putExtra("appTemp", msg.hourly.apparent_temperature);
        intent.putExtra("weather", msg.hourly.weather_code);
        startActivity(intent);
    }

    public void save(View view) {
        SQLiteDatabase db = saveDbOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("str_msg", strMsg);
        db.update("save", contentValues, null, null);
    }
    public void restore(View view) {
        SQLiteDatabase db = saveDbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("save", new String[]{"str_msg"},
                null, null, null, null, null, null);
        cursor.moveToFirst();
        strMsg = cursor.getString(0);
        if (strMsg == "")
            return;
        Msg msg = gson.fromJson(strMsg, Msg.class);
        showCurWeather(msg.current);
    }
}