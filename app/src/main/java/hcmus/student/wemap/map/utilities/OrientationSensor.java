package hcmus.student.wemap.map.utilities;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public abstract class OrientationSensor {
    private SensorManager mSensorService;
    private Sensor mMagneticFieldSensor, mAccelerometerSensor;
    private SensorEventListener mSensorEventListener;
    private float[] mAccelerometer;
    private float[] mMagnetometer;


    public OrientationSensor(SensorManager sensorService) {
        mSensorService = sensorService;
        mMagneticFieldSensor = mSensorService.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mAccelerometerSensor = mSensorService.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccelerometer = null;
        mMagnetometer = null;

        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor == mAccelerometerSensor) {
                    mAccelerometer = event.values;
                } else if (event.sensor == mMagneticFieldSensor) {
                    mMagnetometer = event.values;
                }

                if (mAccelerometer != null && mMagnetometer != null) {
                    float[] rotationMatrix = new float[9];
                    float[] orientation = new float[3];
                    SensorManager.getRotationMatrix(rotationMatrix, null, mAccelerometer, mMagnetometer);
                    SensorManager.getOrientation(rotationMatrix, orientation);

                    //Calculate azimuth in degree
                    float azimuth = (float) Math.toDegrees(orientation[0]);
                    OrientationSensor.this.onSensorChanged(azimuth);
                }
            }
        };
    }

    public void register() {
        if (mAccelerometerSensor != null && mMagneticFieldSensor != null) {
            mSensorService.registerListener(mSensorEventListener, mAccelerometerSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
            mSensorService.registerListener(mSensorEventListener, mMagneticFieldSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void unregister() {
        if (mAccelerometerSensor != null && mMagneticFieldSensor != null)
            mSensorService.unregisterListener(mSensorEventListener);
    }

    public abstract void onSensorChanged(float rotation);

}
