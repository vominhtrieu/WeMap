package hcmus.student.map;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.View;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
public class MapUtility{
    private static float rotation=0;

    public MapUtility(Context context) {

    }

    public static void updateData(float rotation) {
        MapUtility.rotation = rotation;
    }

//   return radius
    public static float getRotation(){
        return rotation;
    }
}
