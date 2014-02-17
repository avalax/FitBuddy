package de.avalax.fitbuddy.app.swipeBar;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class SwipeBarOnGestureListener extends GestureDetector.SimpleOnGestureListener {
    private View swipeableView;
    private int swipeMoveMax;
    private int swipeMinDistance;
    private int swipeThresholdVelocity;

    public SwipeBarOnGestureListener(int swipeMoveMax, View swipeableView, int swipeMinDistance, int swipeThresholdVelocity) {
        this.swipeMoveMax = swipeMoveMax;
        this.swipeableView = swipeableView;
        this.swipeMinDistance = swipeMinDistance;
        this.swipeThresholdVelocity = swipeThresholdVelocity;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        onFlingEvent(1);
        return true;
    }

    @Override
    public boolean onFling(MotionEvent startMotionEvent, MotionEvent endMotionEvent, float velocityX, float velocityY) {
        float absVelocityY = Math.abs(velocityY);
        float moved = startMotionEvent.getY() - endMotionEvent.getY();
        int scaledBarHeight = swipeableView.getHeight() / 2;

        if (moved > swipeMinDistance && absVelocityY > swipeThresholdVelocity) {
            onFlingEvent(calculateMoved(moved, scaledBarHeight));
            return true;
        } else if (-moved > swipeMinDistance && absVelocityY > swipeMinDistance) {
            onFlingEvent(-calculateMoved(moved, scaledBarHeight));
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
