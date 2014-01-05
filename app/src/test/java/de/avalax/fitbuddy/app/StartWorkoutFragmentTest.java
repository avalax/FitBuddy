package de.avalax.fitbuddy.app;

import android.view.View;
import android.widget.Button;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowTextView;
import org.robolectric.util.FragmentTestUtil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class StartWorkoutFragmentTest {
    private StartWorkoutFragment startWorkoutFragment;

    @Before
    public void setUp() throws Exception {
        startWorkoutFragment = new StartWorkoutFragment();
        FragmentTestUtil.startFragment(startWorkoutFragment);
    }

    @Test
    public void testOnViewCreated_shouldRegisterOnClickEvents() throws Exception {
        assertOnClickListener(startWorkoutFragment.buttonEditWorkout);
        assertOnClickListener(startWorkoutFragment.buttonAddWorkout);
        assertOnClickListener(startWorkoutFragment.buttonSwitchWorkout);
    }

    private void assertOnClickListener(Button buttonEditWorkout) {
        ShadowTextView shadowTextView = shadowOf(buttonEditWorkout);
        View.OnClickListener onClickListener = shadowTextView.getOnClickListener();
        assertThat(onClickListener,notNullValue(View.OnClickListener.class));
    }
}
