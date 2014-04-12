package de.avalax.fitbuddy.app;

import android.view.MenuItem;
import de.avalax.fitbuddy.app.manageWorkout.ManageWorkoutActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static de.avalax.fitbuddy.app.Asserts.assertNextStartedActivityForResult;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    private MainActivity mainActivity;
    private WorkoutSession workoutSession;

    @Before
    public void givenAMainActivity() throws Exception {
        mainActivity = Robolectric.buildActivity(MainActivity.class).create().get();
        workoutSession = mock(WorkoutSession.class);
        mainActivity.workoutSession = workoutSession;
    }

    @Test
    public void whenSwitchWorkoutClicked_thenManageWorkoutActivityIsStarted() throws Exception {
        MenuItem menuItem = mock(MenuItem.class);
        when(menuItem.getItemId()).thenReturn(R.id.action_switch_workout);
        when(menuItem.getTitle()).thenReturn(mainActivity.actionSwitchWorkout);
        mainActivity.onOptionsItemSelected(menuItem);

        verify(workoutSession).saveWorkout();
        assertNextStartedActivityForResult(mainActivity, ManageWorkoutActivity.class);
    }
}
