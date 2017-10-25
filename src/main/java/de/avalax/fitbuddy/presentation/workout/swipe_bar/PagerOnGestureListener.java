package de.avalax.fitbuddy.presentation.workout.swipe_bar;

import android.view.GestureDetector;
import android.view.MotionEvent;

public abstract class PagerOnGestureListener extends GestureDetector.SimpleOnGestureListener {
    private int minDistance;
    private int velocity;

    public PagerOnGestureListener(int minDistance, int velocity) {
        this.minDistance = minDistance;
        this.velocity = velocity;
    }

    @Override
    public boolean onFling(MotionEvent startEvent, MotionEvent endEvent, float x, float y) {
        float absX = Math.abs(x);
        float distance = startEvent.getX() - endEvent.getX();

        if (distance > minDistance && absX > velocity) {
            onSwipeLeft();
            return true;
        } else if (-distance > minDistance && absX > minDistance) {
            onSwipeRight();
            return true;
        }
        return false;
    }

    public abstract void onSwipeLeft();

    public abstract void onSwipeRight();
}
