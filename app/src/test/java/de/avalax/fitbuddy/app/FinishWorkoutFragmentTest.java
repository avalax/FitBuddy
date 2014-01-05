package de.avalax.fitbuddy.app;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.FragmentTestUtil;

import static de.avalax.fitbuddy.app.Asserts.assertOnClickListenerRegistered;

@RunWith(RobolectricTestRunner.class)
public class FinishWorkoutFragmentTest {
    private FinishWorkoutFragment finishWorkoutFragment;

    @Before
    public void setUp() throws Exception {
        finishWorkoutFragment = new FinishWorkoutFragment();
        FragmentTestUtil.startFragment(finishWorkoutFragment);
    }

    @Test
    public void testOnViewCreated_shouldRegisterOnClickEvents() throws Exception {
        assertOnClickListenerRegistered(finishWorkoutFragment.buttonFinishWorkout);
        assertOnClickListenerRegistered(finishWorkoutFragment.buttonAddWorkout);
        assertOnClickListenerRegistered(finishWorkoutFragment.buttonSwitchWorkout);
    }
}
