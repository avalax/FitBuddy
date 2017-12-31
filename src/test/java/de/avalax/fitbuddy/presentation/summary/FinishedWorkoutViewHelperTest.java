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

import static de.avalax.fitbuddy.domain.model.finished_workout.BasicFinishedWorkoutBuilder.aFinishedWorkout;
import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class FinishedWorkoutViewHelperTest {
    private FinishedWorkoutViewHelper viewHelper;
    private long today;

    @Before
    public void setUp() throws Exception {
        today = DateUtil.parse("2017-12-31").getTime();
        Locale.setDefault(ENGLISH);
        Context context = RuntimeEnvironment.application.getApplicationContext();
        viewHelper = new FinishedWorkoutViewHelper(context) {
            @Override
            protected long getDate() {
                return today;
            }
        };
    }

    @Test
    public void shouldReturnExecutedDateFormatted() throws Exception {
        assertThat(viewHelper.executionDate(aFinishedWorkout().build())).isEqualTo("Never");
        assertThat(viewHelper.executionDate(aFinishedWorkout().withCreationDate(today).build())).isEqualTo("Today");
        assertThat(viewHelper.executionDate(aFinishedWorkout().withCreationDate(DateUtil.parse("2017-12-30").getTime()).build())).isEqualTo("Yesterday");
        assertThat(viewHelper.executionDate(aFinishedWorkout().withCreationDate(DateUtil.parse("2017-12-20").getTime()).build())).isEqualTo("Dec 20, 2017");
    }
}