package com.example.weatherapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.weatherapp.weatherAPI.CurrentNode;


public class BackgroundView extends View {
    private CurrentNode weather = null;
    private float cloudX = getWidth()/4;

    public BackgroundView(Context context) {
        super(context);
    }
    public BackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public BackgroundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void getWeather(CurrentNode weather) {
        this.weather = weather;
    }

    public void setCloudX(float cloudX) {
        this.cloudX = cloudX;
    }

    private void drawCloud(Canvas canvas, Paint paint, float shiftX, float shiftY) {
        canvas.drawCircle(cloudX-getWidth()/20+shiftX, getHeight()/7+shiftY, getWidth()/20, paint);
        canvas.drawCircle(cloudX-5*getWidth()/40+shiftX, getHeight()/7+shiftY, getWidth()/20, paint);
        canvas.drawCircle(cloudX-4*getWidth()/20+shiftX, getHeight()/7+shiftY, getWidth()/20, paint);
        canvas.drawCircle(cloudX-7*getWidth()/80+shiftX, getHeight()/7-getWidth()/20+shiftY, getWidth()/20, paint);
        canvas.drawCircle(cloudX-13*getWidth()/80+shiftX, getHeight()/7-getWidth()/20+shiftY, getWidth()/20, paint);
        canvas.drawCircle(cloudX-10*getWidth()/80+shiftX, getHeight()/7-3*getWidth()/40+shiftY, getWidth()/20, paint);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (weather == null)
            return;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //paint.setSubpixelText(true);

        if (weather.is_day == 1) {
            paint.setColor(Color.parseColor("#ddefff"));
            canvas.drawPaint(paint);
            paint.setColor(Color.YELLOW);
            canvas.drawCircle(getWidth()/5, getHeight()/6, getWidth()/9, paint);
            if (weather.cloud_cover >= 0) {
                paint.setColor(Color.parseColor("#bbbbbb"));
                drawCloud(canvas, paint, 0, 0);
                drawCloud(canvas, paint, -5 * getWidth() / 8, getWidth() / 5);
                drawCloud(canvas, paint, getWidth() / 2, getWidth() / 12);
                if (weather.cloud_cover >= 25) {
                    drawCloud(canvas, paint, -getWidth() / 2, -getWidth() / 20);
                    if (weather.cloud_cover >= 50) {
                        drawCloud(canvas, paint, -getWidth() / 5, getWidth() / 6);
                        if (weather.cloud_cover >= 75) {
                            drawCloud(canvas, paint, getWidth() / 4, getWidth() / 5);
                        }
                    }
                }
            }
        } else {
            paint.setColor(Color.parseColor("#1c294a"));
            canvas.drawPaint(paint);
            paint.setColor(Color.parseColor("#cccccc"));
            canvas.drawCircle(getWidth()/5, getHeight()/6, getWidth()/9, paint);
            if (weather.cloud_cover >= 0) {
                paint.setColor(Color.GRAY);
                drawCloud(canvas, paint, 0, 0);
                drawCloud(canvas, paint, -5 * getWidth() / 8, getWidth() / 5);
                drawCloud(canvas, paint, getWidth() / 2, getWidth() / 12);
                if (weather.cloud_cover >= 25) {
                    drawCloud(canvas, paint, -getWidth() / 2, -getWidth() / 20);
                    if (weather.cloud_cover >= 50) {
                        drawCloud(canvas, paint, -getWidth() / 5, getWidth() / 6);
                        if (weather.cloud_cover >= 75) {
                            drawCloud(canvas, paint, getWidth() / 4, getWidth() / 5);
                        }
                    }
                }
            }
        }


    }
}