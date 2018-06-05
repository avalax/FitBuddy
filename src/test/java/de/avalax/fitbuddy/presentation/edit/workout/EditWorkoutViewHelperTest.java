package de.avalax.fitbuddy.presentation.edit.workout;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import de.avalax.fitbuddy.BuildConfig;

import static de.avalax.fitbuddy.domain.model.exercise.BasicExerciseBuilder.anExercise;
import static de.avalax.fitbuddy.domain.model.set.BasicSetBuilder.aSet;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EditWorkoutViewHelperTest {

    private EditWorkoutViewHelper viewHelper;

    @Before
    public void setUp() throws Exception {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        viewHelper = new EditWorkoutViewHelper(context);
    }

    @Test
    public void shouldReturnNameAsTitle() throws Exception {
        assertThat(viewHelper.title(anExercise().build())).isEqualTo("");
        assertThat(viewHelper.title(anExercise().withName("exercise name").build())).isEqualTo("exercise name");
    }

    @Test
    public void shouldReturnExerciseSetAsSubtitle() throws Exception {
        assertThat(viewHelper.subtitle(anExercise().build())).isEqualTo("");
        assertThat(viewHelper.subtitle(anExercise().withSet(aSet().withMaxReps(12)).build())).isEqualTo("1 x 12");
        assertThat(viewHelper.subtitle(anExercise().withSet(aSet().withMaxReps(12)).withSet(aSet().withMaxReps(12)).build())).isEqualTo("2 x 12");
        assertThat(viewHelper.subtitle(anExercise().withSet(aSet().withMaxReps(12)).withSet(aSet().withMaxReps(10)).withSet(aSet().withMaxReps(8)).build())).isEqualTo("12 - 10 - 8");
    }

    @Test
    public void shouldReturnMaxWeightOfSetAsWeight() throws Exception {
        assertThat(viewHelper.weight(anExercise().build())).isEqualTo("no weight");
        assertThat(viewHelper.weight(anExercise().withSet(aSet()).build())).isEqualTo("no weight");
        assertThat(viewHelper.weight(anExercise().withSet(aSet().withWeight(42)).build())).isEqualTo("42 kg");
        assertThat(viewHelper.weight(anExercise().withSet(aSet().withWeight(42.5)).build())).isEqualTo("42.5 kg");
        assertThat(viewHelper.weight(anExercise().withSet(aSet().withWeight(0)).withSet(aSet().withWeight(42)).withSet(aSet().withWeight(21)).build())).isEqualTo("42 kg");
    }
}