package com.example.FlappyPlane;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MyOrientationManager implements SensorEventListener {

    private SensorManager msensorManager;

    private float[] rotationMatrix;
    private float[] accelData;
    private float[] magnetData;
    private float[] OrientationData;

    public int getXz() {
        return xz2;
    }

    public int getZy() {
        return zy2;
    }

    public int getXy() {

        return xy2;
    }

    int xy2,xz2,zy2;

    public MyOrientationManager(Activity m) {
        msensorManager = (SensorManager) m.getSystemService(Context.SENSOR_SERVICE); // Получаем менеджер сенсоров
        rotationMatrix = new float[16];
        accelData = new float[3];
        magnetData = new float[3];
        OrientationData = new float[3];
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        loadNewSensorData(event);
        SensorManager.getRotationMatrix(rotationMatrix, null, accelData, magnetData);
        SensorManager.getOrientation(rotationMatrix, OrientationData);

        xy2 = (int)  Math.round(Math.toDegrees(OrientationData[0]));
        xz2 = (int)  Math.round(Math.toDegrees(OrientationData[1]));
        zy2 = (int)  Math.round(Math.toDegrees(OrientationData[2]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void loadNewSensorData(SensorEvent event) {

        final int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            accelData = event.values.clone();
        }

        if (type == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetData = event.values.clone();
        }
    }

}
