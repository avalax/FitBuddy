package de.avalax.fitbuddy.presentation.summary;

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
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;

import static de.avalax.fitbuddy.domain.model.finished_workout.BasicFinishedWorkoutBuilder.aFinishedWorkout;
import static de.avalax.fitbuddy.domain.model.workout.WorkoutIdBuilder.aWorkoutId;
import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class FinishedWorkoutViewHelperTest {
    private FinishedWorkoutViewHelper viewHelper;
    private long today;
    private FinishedWorkoutRepository finishedWorkoutRepository;

    @Before
    public void setUp() throws Exception {
        today = DateUtil.parse("2017-12-31").getTime();
        Locale.setDefault(ENGLISH);
        Context context = RuntimeEnvironment.application.getApplicationContext();
        finishedWorkoutRepository = mock(FinishedWorkoutRepository.class);
        viewHelper = new FinishedWorkoutViewHelper(context, finishedWorkoutRepository) {
            @Override
            protected long getDate() {
                return today;
            }
        };
    }

    @Test
    public void shouldReturnCreationDateFormatted() throws Exception {
        assertThat(viewHelper.creationDate(aFinishedWorkout().withCreationDate(today).build())).isEqualTo("Today");
        assertThat(viewHelper.creationDate(aFinishedWorkout().withCreationDate(DateUtil.parse("2017-12-30").getTime()).build())).isEqualTo("Yesterday");
        assertThat(viewHelper.creationDate(aFinishedWorkout().withCreationDate(DateUtil.parse("2017-12-20").getTime()).build())).isEqualTo("Dec 20, 2017");
    }

    @Test
    public void shouldReturnExecutionCountFormatted() throws Exception {
        assertThat(viewHelper.executions(aWorkoutId(finishedWorkoutRepository).withId("42").build())).isEqualTo("Executed 0 times");
        assertThat(viewHelper.executions(aWorkoutId(finishedWorkoutRepository).withId("42").withFinishedCount(1, finishedWorkoutRepository).build())).isEqualTo("Executed 1 time");
        assertThat(viewHelper.executions(aWorkoutId(finishedWorkoutRepository).withId("42").withFinishedCount(2, finishedWorkoutRepository).build())).isEqualTo("Executed 2 times");
    }

    @Test
    public void shouldReturnLastExecutedDateFormatted() throws Exception {
        assertThat(viewHelper.lastExecutionDate(aWorkoutId(finishedWorkoutRepository).withId("42").build())).isEqualTo("Never");
        assertThat(viewHelper.lastExecutionDate(aWorkoutId(finishedWorkoutRepository).withId("42").withLastExecution(today).build())).isEqualTo("Today");
        assertThat(viewHelper.lastExecutionDate(aWorkoutId(finishedWorkoutRepository).withId("42").withLastExecution(DateUtil.parse("2017-12-30").getTime()).build())).isEqualTo("Yesterday");
        assertThat(viewHelper.lastExecutionDate(aWorkoutId(finishedWorkoutRepository).withId("42").withLastExecution(DateUtil.parse("2017-12-20").getTime()).build())).isEqualTo("Dec 20, 2017");
    }
}