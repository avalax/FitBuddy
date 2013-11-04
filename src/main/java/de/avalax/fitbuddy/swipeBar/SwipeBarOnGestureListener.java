package de.avalax.fitbuddy.swipeBar;

import android.view.GestureDetector;
import android.view.MotionEvent;

public abstract class SwipeBarOnGestureListener extends GestureDetector.SimpleOnGestureListener {
    private SwipeableBar swipeableBar;
    private int swipeMoveMax;
    private int swipeMinDistance;
    private int swipeThresholdVelocity;

    public SwipeBarOnGestureListener(int swipeMoveMax, SwipeableBar swipeableBar, int swipeMinDistance, int swipeThresholdVelocity) {
        this.swipeMoveMax = swipeMoveMax;
        this.swipeableBar = swipeableBar;
        this.swipeMinDistance = swipeMinDistance;
        this.swipeThresholdVelocity = swipeThresholdVelocity;
    }

    @Override
    public void onLongPress(android.view.MotionEvent e) {
        if (e.getX() > swipeableBar.getWidth()/2) {
            onLongPressedRightEvent();
        }  else {
            onLongPressedLeftEvent();
        }
    }

    @Override
    public boolean onFling(MotionEvent startMotionEvent, MotionEvent endMotionEvent, float velocityX, float velocityY) {
        float absVelocityY = Math.abs(velocityY);
        float moved = startMotionEvent.getY() - endMotionEvent.getY();
        if (moved > swipeMinDistance && absVelocityY > swipeThresholdVelocity) {
            onFlingEvent(calculateMoved(moved, swipeableBar.getHeight()));
            return true;
        } else if (-moved > swipeMinDistance && absVelocityY > swipeMinDistance) {
            onFlingEvent(-calculateMoved(moved, swipeableBar.getHeight()));
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

    public abstract void onLongPressedLeftEvent();

    public abstract void onLongPressedRightEvent();
}
