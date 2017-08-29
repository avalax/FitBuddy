package de.avalax.fitbuddy.presentation.workout;

import android.support.v4.view.PagerAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import de.avalax.fitbuddy.BuildConfig;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ExercisePagerTest {

    private ExercisePager exercisePager;

    @Before
    public void setUp() throws Exception {
        exercisePager = new ExercisePager(null, 42);
    }

    @Test
    public void getItem_shouldReturnAnInstanceOfExerciseFragment() throws Exception {
        assertThat(exercisePager.getItem(42), instanceOf(ExerciseFragment.class));
    }

    @Test
    public void getCount_shouldReturnCountFromInitialization() throws Exception {
        assertThat(exercisePager.getCount(), equalTo(42));
    }

    @Test
    public void getItemPosition_shouldAlwaysReturnPositionNone() throws Exception {
        assertThat(exercisePager.getItemPosition(null), equalTo(PagerAdapter.POSITION_NONE));
        assertThat(exercisePager.getItemPosition(0), equalTo(PagerAdapter.POSITION_NONE));
        assertThat(exercisePager.getItemPosition(42), equalTo(PagerAdapter.POSITION_NONE));
    }
}