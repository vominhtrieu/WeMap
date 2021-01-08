package hcmus.student.wemap.map.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MapWrapper extends FrameLayout {
    private OnMapWrapperTouch delegate;

    public MapWrapper(@NonNull Context context) {
        super(context);
        delegate = null;
    }

    public MapWrapper(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.delegate = null;
    }

    public void setOnMapWrapperTouch(OnMapWrapperTouch delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && delegate != null) {
            delegate.onMapWrapperTouch();
        }

        return super.dispatchTouchEvent(ev);
    }
}
