package de.avalax.fitbuddy.progressBar;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

public abstract class ProgressBarOnGestureListener extends GestureDetector.SimpleOnGestureListener{
    private int swipeMoveMax;
    private final WindowManager windowManager;
    private int swipeMinDistance;
    private int swipeThresholdVelocity;

    public ProgressBarOnGestureListener(int swipeMoveMax, WindowManager windowManager, int swipeMinDistance, int swipeThresholdVelocity) {
        this.swipeMoveMax = swipeMoveMax;
        this.windowManager = windowManager;
        this.swipeMinDistance = swipeMinDistance;
        this.swipeThresholdVelocity = swipeThresholdVelocity;
    }

    @Override
    public boolean onFling(MotionEvent startMotionEvent, MotionEvent endMotionEvent, float velocityX, float velocityY) {
        if (startMotionEvent.getY() - endMotionEvent.getY() > swipeMinDistance && Math.abs(velocityY) > swipeThresholdVelocity) {
            onFlingEvent(1);
            return true;
        } else if (endMotionEvent.getY() - startMotionEvent.getY() > swipeMinDistance && Math.abs(velocityY) > swipeMinDistance) {
            onFlingEvent(-1);
            return true;
        }
        return false;
    }

    public abstract void onFlingEvent(int moved);
}
