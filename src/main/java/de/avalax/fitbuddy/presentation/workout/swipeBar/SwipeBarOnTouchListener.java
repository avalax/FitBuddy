package de.avalax.fitbuddy.presentation.workout.swipeBar;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public abstract class SwipeBarOnTouchListener implements View.OnTouchListener {
    private final GestureDetectorCompat gdt;

    public SwipeBarOnTouchListener(Context context, View swipeableView, int swipeMoveMax) {
        final SwipeBarOnTouchListener touchListener = this;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        int minSwipeDistance = viewConfiguration.getScaledPagingTouchSlop();
        int thresholdSwipeVelocity = viewConfiguration.getScaledMinimumFlingVelocity();

        gdt = new GestureDetectorCompat (context, new SwipeBarOnGestureListener(swipeMoveMax, swipeableView, minSwipeDistance, thresholdSwipeVelocity) {
            @Override
            public void onFlingEvent(int moved) {
                touchListener.onFlingEvent(moved);
            }
        });
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        gdt.onTouchEvent(event);
        return true;
    }

    protected abstract void onFlingEvent(int moved);
}
