package de.avalax.fitbuddy.presentation.workout.swipe_bar;

import android.view.MotionEvent;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import de.avalax.fitbuddy.BuildConfig;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PagerOnGestureListenerTest {
    private MotionEvent getMotionEvent(float x) {
        MotionEvent e1 = mock(MotionEvent.class);
        when(e1.getX()).thenReturn(x);
        return e1;
    }

    private static final int SWIPE_MIN_DISTANCE = 30;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;
    private static final Integer PROGRESS_BAR_HEIGHT = 240;
    private static final Integer PROGRESS_BAR_WIDTH = 300;

    private PagerOnGestureListener pagerOnGestureListener;
    private boolean swipeLeft = false;

    private boolean swipeRight = false;

    @Before
    public void setUp() {
        View verticalProgressBar = mock(View.class);
        when(verticalProgressBar.getHeight()).thenReturn(PROGRESS_BAR_HEIGHT);
        when(verticalProgressBar.getWidth()).thenReturn(PROGRESS_BAR_WIDTH);
        pagerOnGestureListener = new PagerOnGestureListener(SWIPE_MIN_DISTANCE, SWIPE_THRESHOLD_VELOCITY) {

            @Override
            public void onSwipeLeft() {
                swipeLeft = true;
            }

            @Override
            public void onSwipeRight() {
                swipeRight = true;
            }
        };
    }

    @Test
    public void onSwipeLeft_shouldReturnTrueAndCallSwipeLeft() throws Exception {
        MotionEvent startMotionEvent = getMotionEvent(SWIPE_MIN_DISTANCE + 1);
        MotionEvent endMotionEvent = getMotionEvent(0);

        boolean hasFling = pagerOnGestureListener.onFling(startMotionEvent, endMotionEvent, SWIPE_THRESHOLD_VELOCITY + 1, 0);

        assertThat(hasFling, equalTo(true));
        assertThat(swipeLeft, equalTo(true));
    }

    @Test
    public void onSwipeLeftThreshold_shouldReturnFalse() throws Exception {
        MotionEvent startMotionEvent = getMotionEvent(SWIPE_MIN_DISTANCE);
        MotionEvent endMotionEvent = getMotionEvent(0);

        boolean hasFling = pagerOnGestureListener.onFling(startMotionEvent, endMotionEvent, SWIPE_THRESHOLD_VELOCITY , 0);

        assertThat(hasFling, equalTo(false));
        assertThat(swipeLeft, equalTo(false));
    }

    @Test
    public void onSwipeRight_shouldReturnTrueAndCallSwipeRight() throws Exception {
        MotionEvent startMotionEvent = getMotionEvent(0);
        MotionEvent endMotionEvent = getMotionEvent(SWIPE_MIN_DISTANCE + 1);

        boolean hasFling = pagerOnGestureListener.onFling(startMotionEvent, endMotionEvent, SWIPE_THRESHOLD_VELOCITY + 1, 0);

        assertThat(hasFling, equalTo(true));
        assertThat(swipeLeft, equalTo(false));
        assertThat(swipeRight, equalTo(true));
    }

    @Test
    public void onSwipeRightThreshold_shouldReturnFalse() throws Exception {
        MotionEvent startMotionEvent = getMotionEvent(0);
        MotionEvent endMotionEvent = getMotionEvent(SWIPE_MIN_DISTANCE);

        boolean hasFling = pagerOnGestureListener.onFling(startMotionEvent, endMotionEvent, SWIPE_THRESHOLD_VELOCITY + 1, 0);

        assertThat(hasFling, equalTo(false));
        assertThat(swipeLeft, equalTo(false));
        assertThat(swipeRight, equalTo(false));
    }
}
