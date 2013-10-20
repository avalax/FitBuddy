package de.avalax.fitbuddy.progressBar;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public abstract class ProgressBarOnTouchListener implements View.OnTouchListener {
    private static final int SWIPE_MIN_DISTANCE = 60;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;

    private final GestureDetector gdt;

    public ProgressBarOnTouchListener(int swipeMoveMax, WindowManager windowManager) {
        gdt = new GestureDetector(new ProgressBarOnGestureListener(swipeMoveMax, windowManager, SWIPE_MIN_DISTANCE, SWIPE_THRESHOLD_VELOCITY) {
            @Override
            public void onFlingEvent(int moved) {
                onGestureFlingEvent(moved);
            }
        });
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        gdt.onTouchEvent(event);
        return true;
    }

    public abstract void onGestureFlingEvent(int moved);
}
