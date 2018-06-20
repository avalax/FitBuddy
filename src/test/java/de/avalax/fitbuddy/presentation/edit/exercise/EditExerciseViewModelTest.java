package de.avalax.fitbuddy.presentation.edit.exercise;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import de.avalax.fitbuddy.BuildConfig;
import de.avalax.fitbuddy.domain.model.exercise.BasicExerciseBuilder;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;

import static de.avalax.fitbuddy.domain.model.exercise.BasicExerciseBuilder.anExercise;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EditExerciseViewModelTest {
    private Exercise exercise;

    private EditExerciseViewModel editExerciseViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        exercise = anExercise().withName("exercise name").build();
        editExerciseViewModel = new EditExerciseViewModel();
    }

    @Test
    public void onInit_shouldSetWeightAndReps() {
        editExerciseViewModel.init(exercise);

        assertThat(editExerciseViewModel.getName().getValue()).isEqualTo("exercise name");
        assertThat(editExerciseViewModel.getSets().getValue()).isEqualTo(exercise.getSets());
    }

}