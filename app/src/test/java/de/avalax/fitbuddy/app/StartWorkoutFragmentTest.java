package de.avalax.fitbuddy.app;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.FragmentTestUtil;

import static de.avalax.fitbuddy.app.Asserts.assertOnClickListenerRegistered;

@RunWith(RobolectricTestRunner.class)
public class StartWorkoutFragmentTest {
    private StartWorkoutFragment startWorkoutFragment;

    @Before
    public void setUp() throws Exception {
        startWorkoutFragment = new StartWorkoutFragment();
        FragmentTestUtil.startFragment(startWorkoutFragment);
    }

    @Test
    public void shouldRegisterOnClickEvents() throws Exception {
        assertOnClickListenerRegistered(startWorkoutFragment.buttonEditWorkout);
        assertOnClickListenerRegistered(startWorkoutFragment.buttonAddWorkout);
        assertOnClickListenerRegistered(startWorkoutFragment.buttonSwitchWorkout);
    }
}
