package de.avalax.fitbuddy.app.swipeBar;

import android.content.Context;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

public abstract class SwipeBarOnTouchListener implements View.OnTouchListener {
    private int minSwipeDistance;
    private int thresholdSwipeVelocity;
		private ViewConfiguration viewConfiguration;

    private final GestureDetector gdt;

    public SwipeBarOnTouchListener(Context context, SwipeableBar swipeableBar, int swipeMoveMax) {
        final SwipeBarOnTouchListener touchListener = this;
	      viewConfiguration = ViewConfiguration.get(context);
	      minSwipeDistance = viewConfiguration.getScaledPagingTouchSlop();
	      thresholdSwipeVelocity = viewConfiguration.getScaledMinimumFlingVelocity();

        gdt = new GestureDetector(context,new SwipeBarOnGestureListener(swipeMoveMax, swipeableBar, minSwipeDistance, thresholdSwipeVelocity) {
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
