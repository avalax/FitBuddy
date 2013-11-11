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
    private static int SWIPE_MIN_DISTANCE;
    private static int SWIPE_THRESHOLD_VELOCITY;

    private final GestureDetector gdt;

    public SwipeBarOnTouchListener(Context context, SwipeableBar swipeableBar, int swipeMoveMax) {
        final SwipeBarOnTouchListener touchListener = this;
	      final ViewConfiguration vc = ViewConfiguration.get(context);
	      SWIPE_MIN_DISTANCE = vc.getScaledPagingTouchSlop();
	      SWIPE_THRESHOLD_VELOCITY = vc.getScaledMinimumFlingVelocity();

	      Log.d("", "swipeMinDistance = " + SWIPE_MIN_DISTANCE);
	      Log.d("", "swipeThresholdVelocity = " + SWIPE_THRESHOLD_VELOCITY);

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
