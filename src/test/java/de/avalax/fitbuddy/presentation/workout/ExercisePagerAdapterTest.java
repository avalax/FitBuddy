package de.avalax.fitbuddy.presentation.workout;

import android.support.v4.view.PagerAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class ExercisePagerAdapterTest {

    private ExercisePagerAdapter exercisePagerAdapter;

    @Before
    public void setUp() throws Exception {
        exercisePagerAdapter = new ExercisePagerAdapter(null, 42);
    }

    @Test
    public void getItem_shouldReturnAnInstanceOfExerciseFragment() throws Exception {
        assertThat(exercisePagerAdapter.getItem(42), instanceOf(ExerciseFragment.class));
    }

    @Test
    public void getCount_shouldReturnCountFromInitialization() throws Exception {
        assertThat(exercisePagerAdapter.getCount(), equalTo(42));
    }

    @Test
    public void getItemPosition_shouldAlwaysReturnPositionNone() throws Exception {
        assertThat(exercisePagerAdapter.getItemPosition(null), equalTo(PagerAdapter.POSITION_NONE));
        assertThat(exercisePagerAdapter.getItemPosition(0), equalTo(PagerAdapter.POSITION_NONE));
        assertThat(exercisePagerAdapter.getItemPosition(42), equalTo(PagerAdapter.POSITION_NONE));
    }
}