package de.avalax.fitbuddy.presentation.workout.swipe_bar;

import android.app.Activity;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public abstract class PagerOnTouchListener implements View.OnTouchListener {
    private GestureDetectorCompat gdt;

    public PagerOnTouchListener(Activity activity) {
        PagerOnTouchListener touchListener = this;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(activity);
        int minSwipeDistance = viewConfiguration.getScaledPagingTouchSlop();
        int velocity = viewConfiguration.getScaledMinimumFlingVelocity();

        gdt = new GestureDetectorCompat(activity,
                new PagerOnGestureListener(minSwipeDistance, velocity) {

                    @Override
                    public void onSwipeLeft() {
                        touchListener.onSwipeLeft();
                    }

                    @Override
                    public void onSwipeRight() {
                        touchListener.onSwipeRight();
                    }
                });
    }

    protected abstract void onSwipeRight();

    protected abstract void onSwipeLeft();

    @Override
    public boolean onTouch(final View view, final MotionEvent event) {
        return !gdt.onTouchEvent(event);
    }
}
