package de.avalax.fitbuddy;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class ProgressBarGestureListener extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
        throw new RuntimeException();
    }
}
