package de.avalax.fitbuddy.presentation.workout.swipe_bar;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public abstract class SwipeBarOnTouchListener implements View.OnTouchListener {
    private final GestureDetectorCompat gdt;

    public SwipeBarOnTouchListener(Context context, View view, int swipeMoveMax) {
        final SwipeBarOnTouchListener touchListener = this;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        int minSwipeDistance = viewConfiguration.getScaledPagingTouchSlop();
        int velocity = viewConfiguration.getScaledMinimumFlingVelocity();

        gdt = new GestureDetectorCompat(context,
                new SwipeBarOnGestureListener(swipeMoveMax, view, minSwipeDistance, velocity) {
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
