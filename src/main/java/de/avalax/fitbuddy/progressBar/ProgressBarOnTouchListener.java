package de.avalax.fitbuddy.progressBar;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public abstract class ProgressBarOnTouchListener implements View.OnTouchListener {
    private final GestureDetector gdt;

    public ProgressBarOnTouchListener(WindowManager windowManager) {
        gdt = new GestureDetector(new ProgressBarOnGestureListener(windowManager) {
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
