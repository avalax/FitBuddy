package de.avalax.fitbuddy.presentation.welcome_screen;

import android.content.Context;

import org.assertj.core.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Locale;

import de.avalax.fitbuddy.BuildConfig;

import static de.avalax.fitbuddy.domain.model.workout.BasicWorkoutBuilder.aWorkout;
import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class WorkoutViewHelperTest {
    private WorkoutViewHelper viewHelper;
    private long today;

    @Before
    public void setUp() throws Exception {
        today = DateUtil.parse("2017-12-31").getTime();
        Locale.setDefault(ENGLISH);
        Context context = RuntimeEnvironment.application.getApplicationContext();
        viewHelper = new WorkoutViewHelper(context) {
            @Override
            protected long getDate() {
                return today;
            }
        };
    }

    @Test
    public void shouldReturnLastExecutedDateFormatted() throws Exception {
        assertThat(viewHelper.lastExecutionDate(aWorkout().build())).isEqualTo("Never");
        assertThat(viewHelper.lastExecutionDate(aWorkout().withLastExecution(today).build())).isEqualTo("Today");
        assertThat(viewHelper.lastExecutionDate(aWorkout().withLastExecution(DateUtil.parse("2017-12-30").getTime()).build())).isEqualTo("Yesterday");
        assertThat(viewHelper.lastExecutionDate(aWorkout().withLastExecution(DateUtil.parse("2017-12-20").getTime()).build())).isEqualTo("Dec 20, 2017");
    }

    @Test
    public void shouldReturnExecutionCountFormatted() throws Exception {
        assertThat(viewHelper.executions(aWorkout().build())).isEqualTo("Executed 0 times");
        assertThat(viewHelper.executions(aWorkout().withFinishedCount(1).build())).isEqualTo("Executed 1 time");
        assertThat(viewHelper.executions(aWorkout().withFinishedCount(2).build())).isEqualTo("Executed 2 times");
    }
}