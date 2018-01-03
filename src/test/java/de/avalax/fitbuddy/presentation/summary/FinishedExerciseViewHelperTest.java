package de.avalax.fitbuddy.presentation.summary;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import de.avalax.fitbuddy.BuildConfig;

import static de.avalax.fitbuddy.domain.model.finished_exercise.BasicFinishedExerciseBuilder.anFinishedExercise;
import static de.avalax.fitbuddy.domain.model.finished_set.BasicFinishedSetBuilder.aFinishedSet;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class FinishedExerciseViewHelperTest {
    private FinishedExerciseViewHelper viewHelper;

    @Before
    public void setUp() throws Exception {
        viewHelper = new FinishedExerciseViewHelper();
    }

    @Test
    public void shouldReturnExerciseSetAsSubtitle() throws Exception {
        assertThat(viewHelper.subtitle(anFinishedExercise().build())).isEqualTo("");
        assertThat(viewHelper.subtitle(anFinishedExercise().withFinishedSet(aFinishedSet().withMaxReps(12)).build())).isEqualTo("1 x 12");
        assertThat(viewHelper.subtitle(anFinishedExercise().withFinishedSet(aFinishedSet().withMaxReps(12)).withFinishedSet(aFinishedSet().withMaxReps(12)).build())).isEqualTo("2 x 12");
        assertThat(viewHelper.subtitle(anFinishedExercise().withFinishedSet(aFinishedSet().withMaxReps(12)).withFinishedSet(aFinishedSet().withMaxReps(10)).withFinishedSet(aFinishedSet().withMaxReps(8)).build())).isEqualTo("12 - 10 - 8");
    }
}