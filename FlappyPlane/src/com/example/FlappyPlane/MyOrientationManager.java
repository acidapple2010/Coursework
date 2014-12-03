package com.example.FlappyPlane;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MyOrientationManager implements SensorEventListener {

    private float[] accelData;

    public int getAngle() {
        return angle;
    }

    private int angle;

    public MyOrientationManager(Activity m) {
        SensorManager msensorManager = (SensorManager) m.getSystemService(Context.SENSOR_SERVICE);
        msensorManager.registerListener(this, msensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        accelData = new float[3];
        angle = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        final int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            accelData = event.values.clone();
        }

        angle = (int) Math.round(Math.toDegrees(Math.atan(accelData[0]/accelData[1])));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
