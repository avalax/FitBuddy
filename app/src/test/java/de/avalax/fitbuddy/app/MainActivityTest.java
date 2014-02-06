package de.avalax.fitbuddy.app;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static de.avalax.fitbuddy.app.Asserts.assertNextStartedActivityForResult;
import static de.avalax.fitbuddy.app.Asserts.assertOnClickListenerRegistered;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    private MainActivity mainActivity;
    private WorkoutSession workoutSession;

    @Before
    public void setUp() throws Exception {
        mainActivity = Robolectric.buildActivity(MainActivity.class).create().get();
        workoutSession = mock(WorkoutSession.class);
        mainActivity.workoutSession = workoutSession;
    }

    @Test
    public void shouldRegisterOnClickEvents() throws Exception {
        assertOnClickListenerRegistered(mainActivity.actionBarOverflow);
    }

    @Test
    public void testFinishWorkout() throws Exception {
        mainActivity.switchWorkout();

        verify(workoutSession).saveWorkout();
        assertNextStartedActivityForResult(mainActivity, ManageWorkoutActivity.class);
    }
}
