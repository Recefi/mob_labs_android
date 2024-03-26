package com.example.weatherapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {
    private String[] time;
    private double[] temp;
    private double[] appTemp;
    private int[] weather;

    public ForecastAdapter(String[] time, double[] temp, double[] appTemp, int[] weather) {
        this.time = time;
        this.temp = temp;
        this.appTemp = appTemp;
        this.weather = weather;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView timeTv;
        private final TextView tempTv;
        private final TextView appTempTv;
        private final ImageView weatherImg;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ForecastAdapter", "Element " + getAdapterPosition() + " clicked.");
                }
            });
            timeTv = (TextView) v.findViewById(R.id.forecastDate);
            tempTv = (TextView) v.findViewById(R.id.forecastTemp);
            appTempTv = (TextView) v.findViewById(R.id.forecastAppTemp);
            weatherImg = (ImageView) v.findViewById(R.id.forecastImg);
        }

        public TextView getTime() {
            return timeTv;
        }
        public TextView getTemp() {
            return tempTv;
        }
        public TextView getAppTemp() {
            return appTempTv;
        }
        public ImageView getWeatherImg() {
            return weatherImg;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.forecast_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTime().setText(time[position].substring(0, 10)+", "+time[position].substring(11, 16));
        viewHolder.getTemp().setText(temp[position]+"°C");
        viewHolder.getAppTemp().setText(appTemp[position]+"°C");
        switch (weather[position]) {
            case 0: case 1: viewHolder.getWeatherImg().setImageResource(R.drawable.weather_01); break;
            case 2: viewHolder.getWeatherImg().setImageResource(R.drawable.weather_2); break;
            case 3: viewHolder.getWeatherImg().setImageResource(R.drawable.weather_3); break;
            case 45: case 48: viewHolder.getWeatherImg().setImageResource(R.drawable.weather_4548); break;
            case 51: case 53: case 55: case 56: case 57: case 80: case 81: case 82:
                viewHolder.getWeatherImg().setImageResource(R.drawable.weather_5153555657808182); break;
            case 61: case 63: case 65: case 66: case 67:
                viewHolder.getWeatherImg().setImageResource(R.drawable.weather_6163656667); break;
            case 71: case 73: case 75: case 77: case 85: case 86:
                viewHolder.getWeatherImg().setImageResource(R.drawable.weather_717375778586); break;
            case 95: case 96: case 99:
                viewHolder.getWeatherImg().setImageResource(R.drawable.weather_959699); break;
        }
    }

    @Override
    public int getItemCount() {
        return time.length;
    }
}