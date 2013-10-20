package de.avalax.fitbuddy.progressBar;

import android.util.Log;
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
        float absVelocityY = Math.abs(velocityY);
        float moved = startMotionEvent.getY() - endMotionEvent.getY();
        Log.d("onFLing",String.valueOf(moved));
        if (moved > swipeMinDistance && absVelocityY > swipeThresholdVelocity) {
            onFlingEvent(calculateMoved(moved, windowManager.getDefaultDisplay().getHeight()));
            return true;
        } else if (-moved > swipeMinDistance && absVelocityY > swipeMinDistance) {
            onFlingEvent(-calculateMoved(moved, windowManager.getDefaultDisplay().getHeight()));
            return true;
        }
        return false;
    }

    private int calculateMoved(float moved, int height) {
        if (Math.abs(moved) + swipeMinDistance >= height) {
            return swipeMoveMax;
        }
        return 1;
    }

    public abstract void onFlingEvent(int moved);
}
