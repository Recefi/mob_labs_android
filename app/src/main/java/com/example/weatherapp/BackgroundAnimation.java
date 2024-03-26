package com.example.weatherapp;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class BackgroundAnimation extends Animation {
    private BackgroundView backgroundView;
    private float oldCloudX;
    private float newCloudX;

    public BackgroundAnimation(BackgroundView backgroundView) {
        this.oldCloudX = backgroundView.getWidth()/4;
        this.newCloudX = backgroundView.getWidth();
        this.backgroundView = backgroundView;
    }
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float cloudX = oldCloudX + ((newCloudX - oldCloudX) * interpolatedTime);

        backgroundView.setCloudX(cloudX);
        backgroundView.requestLayout();
    }
}
