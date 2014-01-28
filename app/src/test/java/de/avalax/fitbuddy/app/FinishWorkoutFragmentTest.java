package de.avalax.fitbuddy.app;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.FragmentTestUtil;

import static de.avalax.fitbuddy.app.Asserts.assertNextStartedActivityForResult;
import static de.avalax.fitbuddy.app.Asserts.assertOnClickListenerRegistered;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class FinishWorkoutFragmentTest {
    private FinishWorkoutFragment finishWorkoutFragment;
    private WorkoutSession workoutSession;

    @Before
    public void setUp() throws Exception {
        finishWorkoutFragment = new FinishWorkoutFragment();
        FragmentTestUtil.startFragment(finishWorkoutFragment);
        workoutSession = mock(WorkoutSession.class);
        finishWorkoutFragment.workoutSession = workoutSession;
    }

    @Test
    public void shouldRegisterOnClickEvents() throws Exception {
        assertOnClickListenerRegistered(finishWorkoutFragment.buttonFinishWorkout);
        assertOnClickListenerRegistered(finishWorkoutFragment.buttonAddWorkout);
        assertOnClickListenerRegistered(finishWorkoutFragment.buttonSwitchWorkout);
    }

    @Test
    public void testFinishWorkout() throws Exception {
        finishWorkoutFragment.finishWorkout();

        verify(workoutSession).saveWorkout();
        assertNextStartedActivityForResult(finishWorkoutFragment, ManageWorkoutActivity.class);
    }
}
