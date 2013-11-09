package de.avalax.fitbuddy.app.swipeBar;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class SwipeBarOnTouchListener implements View.OnTouchListener {
    private static final int SWIPE_MIN_DISTANCE = 60;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;

    private final GestureDetector gdt;

    public SwipeBarOnTouchListener(Context context, SwipeableBar swipeableBar, int swipeMoveMax) {
        final SwipeBarOnTouchListener touchListener = this;
        gdt = new GestureDetector(context,new SwipeBarOnGestureListener(swipeMoveMax, swipeableBar, SWIPE_MIN_DISTANCE, SWIPE_THRESHOLD_VELOCITY) {
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
