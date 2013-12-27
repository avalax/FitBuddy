package de.avalax.fitbuddy.app.swipeBar;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public abstract class SwipeBarOnTouchListener implements View.OnTouchListener {
    private final GestureDetector gdt;

    public SwipeBarOnTouchListener(Context context, View swipeableView, int swipeMoveMax) {
        final SwipeBarOnTouchListener touchListener = this;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        int minSwipeDistance = viewConfiguration.getScaledPagingTouchSlop();
        int thresholdSwipeVelocity = viewConfiguration.getScaledMinimumFlingVelocity();

        gdt = new GestureDetector(context, new SwipeBarOnGestureListener(swipeMoveMax, swipeableView, minSwipeDistance, thresholdSwipeVelocity) {
            @Override
            public void onFlingEvent(int moved) {
                touchListener.onFlingEvent(moved);
            }

            @Override
            public void onLongPressedLeftEvent() {
                touchListener.onLongPressedLeftEvent();
            }

            @Override
            public void onLongPressedRightEvent() {
                touchListener.onLongPressedRightEvent();
            }
        });
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        gdt.onTouchEvent(event);
        return true;
    }

    protected abstract void onFlingEvent(int moved);

    protected abstract void onLongPressedLeftEvent();

    protected abstract void onLongPressedRightEvent();
}
