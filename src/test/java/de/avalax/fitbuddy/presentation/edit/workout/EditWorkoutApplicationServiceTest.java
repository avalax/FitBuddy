package de.avalax.fitbuddy.presentation.edit.workout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import de.avalax.fitbuddy.BuildConfig;

import static de.avalax.fitbuddy.domain.model.exercise.BasicExerciseBuilder.anExercise;
import static de.avalax.fitbuddy.domain.model.set.BasicSetBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EditWorkoutApplicationServiceTest {

    private EditWorkoutApplicationService service;

    @Before
    public void setUp() throws Exception {
        service = new EditWorkoutApplicationService();
    }

    @Test
    public void shouldReturnNameAsTitle() throws Exception {
        assertThat(service.title(anExercise().build())).isEqualTo("");
        assertThat(service.title(anExercise().withName("exercise name").build())).isEqualTo("exercise name");
    }

    @Test
    public void shouldReturnExerciseSetAsSubtitle() throws Exception {
        assertThat(service.subtitle(anExercise().build())).isEqualTo("");
        assertThat(service.subtitle(anExercise().withSet(aSet().withMaxReps(12)).build())).isEqualTo("1 x 12");
        assertThat(service.subtitle(anExercise().withSet(aSet().withMaxReps(12)).withSet(aSet().withMaxReps(12)).build())).isEqualTo("2 x 12");
        assertThat(service.subtitle(anExercise().withSet(aSet().withMaxReps(12)).withSet(aSet().withMaxReps(10)).withSet(aSet().withMaxReps(8)).build())).isEqualTo("12 - 10 - 8");
    }
}