package de.avalax.fitbuddy.presentation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class FixedViewPager extends android.support.v4.view.ViewPager {
    public FixedViewPager(Context context) {
        super(context);
    }

    public FixedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
