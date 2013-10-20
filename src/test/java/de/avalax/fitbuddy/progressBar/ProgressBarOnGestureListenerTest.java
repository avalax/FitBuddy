package de.avalax.fitbuddy.progressBar;

import android.view.MotionEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class ProgressBarOnGestureListenerTest {
    private static final int SWIPE_MOVE_MAX = 12;
    private static final int SWIPE_MIN_DISTANCE = 60;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;
    private static final Integer PROGRESS_BAR_HEIGHT = 240;
    private int onFlingEventMoved;
    private ProgressBarOnGestureListener progressBarOnGestureListener;


    @Before
    public void setUp() {
        VerticalProgressBar verticalProgressBar = mock(VerticalProgressBar.class);
        when(verticalProgressBar.getHeight()).thenReturn(PROGRESS_BAR_HEIGHT);
        progressBarOnGestureListener = new ProgressBarOnGestureListener(SWIPE_MOVE_MAX,verticalProgressBar, SWIPE_MIN_DISTANCE, SWIPE_THRESHOLD_VELOCITY) {
            @Override
            public void onFlingEvent(int moved) {
                onFlingEventMoved = moved;
            }
        };
    }

    @Test
    public void testOnFling_shouldReturnTrueWhenSwipingUp() throws Exception {
        MotionEvent startMotionEvent = getMotionEvent(SWIPE_MIN_DISTANCE + 1);
        MotionEvent endMotionEvent = getMotionEvent(0);

        boolean hasFling = progressBarOnGestureListener.onFling(startMotionEvent, endMotionEvent, 0, SWIPE_THRESHOLD_VELOCITY + 1);

        assertThat(hasFling, equalTo(true));
    }

    @Test
    public void testOnFling_shouldReturnFalseWhenMinDistanceNotReachedOnSwipingUp() throws Exception {
        MotionEvent startMotionEvent = getMotionEvent(SWIPE_MIN_DISTANCE);
        MotionEvent endMotionEvent = getMotionEvent(0);

        boolean hasFling = progressBarOnGestureListener.onFling(startMotionEvent, endMotionEvent, 0, SWIPE_THRESHOLD_VELOCITY + 1);

        assertThat(hasFling, equalTo(false));
    }

    @Test
    public void testOnFling_shouldReturnTrueWhenSwipingDown() throws Exception {
        MotionEvent startMotionEvent = getMotionEvent(0);
        MotionEvent endMotionEvent = getMotionEvent(SWIPE_MIN_DISTANCE + 1);

        boolean hasFling = progressBarOnGestureListener.onFling(startMotionEvent, endMotionEvent, 0, SWIPE_THRESHOLD_VELOCITY + 1);

        assertThat(hasFling, equalTo(true));
    }

    @Test
    public void testOnFling_shouldReturnFalseWhenMinDistanceNotReachedOnSwipingDown() throws Exception {
        MotionEvent startMotionEvent = getMotionEvent(0);
        MotionEvent endMotionEvent = getMotionEvent(SWIPE_MIN_DISTANCE);

        boolean hasFling = progressBarOnGestureListener.onFling(startMotionEvent, endMotionEvent, 0, SWIPE_THRESHOLD_VELOCITY + 1);

        assertThat(hasFling, equalTo(false));
    }

    @Test
    public void testOnFling_shouldReturnFalseWhenMinVelocityNotReachedOnSwiping() throws Exception {
        MotionEvent startMotionEvent = getMotionEvent(SWIPE_MIN_DISTANCE + 1);
        MotionEvent endMotionEvent = getMotionEvent(0);

        boolean hasFling = progressBarOnGestureListener.onFling(startMotionEvent, endMotionEvent, 0, SWIPE_THRESHOLD_VELOCITY);

        assertThat(hasFling, equalTo(false));
    }

    @Test
    public void testOnFling_shouldSetMovedTo1OnSwipingUp() throws Exception {
        MotionEvent startMotionEvent = getMotionEvent(SWIPE_MIN_DISTANCE + 1);
        MotionEvent endMotionEvent = getMotionEvent(0);

        progressBarOnGestureListener.onFling(startMotionEvent, endMotionEvent, 0, SWIPE_THRESHOLD_VELOCITY + 1);

        assertThat(onFlingEventMoved, equalTo(1));
    }

    @Test
    public void testOnFling_shouldSetMovedToMinus1OnSwipingDown() throws Exception {
        MotionEvent startMotionEvent = getMotionEvent(0);
        MotionEvent endMotionEvent = getMotionEvent(SWIPE_MIN_DISTANCE + 1);

        progressBarOnGestureListener.onFling(startMotionEvent, endMotionEvent, 0, SWIPE_THRESHOLD_VELOCITY + 1);

        assertThat(onFlingEventMoved, equalTo(-1));
    }

    @Test
    public void testOnFling_shouldSetMovedToMaxOnSwipingUp() throws Exception {
        MotionEvent startMotionEvent = getMotionEvent(PROGRESS_BAR_HEIGHT);
        MotionEvent endMotionEvent = getMotionEvent(0);

        progressBarOnGestureListener.onFling(startMotionEvent, endMotionEvent, 0, SWIPE_THRESHOLD_VELOCITY + 1);

        assertThat(onFlingEventMoved, equalTo(SWIPE_MOVE_MAX));
    }

    @Test
    public void testOnFling_shouldSetMovedToMaxOnSwipingDown() throws Exception {
        MotionEvent startMotionEvent = getMotionEvent(0);
        MotionEvent endMotionEvent = getMotionEvent(PROGRESS_BAR_HEIGHT);

        progressBarOnGestureListener.onFling(startMotionEvent, endMotionEvent, 0, SWIPE_THRESHOLD_VELOCITY + 1);

        assertThat(onFlingEventMoved, equalTo(-SWIPE_MOVE_MAX));
    }

    private MotionEvent getMotionEvent(float y) {
        MotionEvent e1 = mock(MotionEvent.class);
        when(e1.getY()).thenReturn(y);
        return e1;
    }
}
