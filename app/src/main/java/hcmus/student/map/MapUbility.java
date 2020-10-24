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
public class MapUbility extends View{
    private static float position=0;
    private Paint paint;





    public MapUbility(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setTextSize(25);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
    }


    @Override
    protected void onDraw(Canvas canvas) {

    }

    public void updateData(float position) {
        this.position = position;
        invalidate();
    }

//   return radius
    public static float getPositon(){
        return position;
    }
}
