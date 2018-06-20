package de.avalax.fitbuddy.presentation.edit.workout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import de.avalax.fitbuddy.BuildConfig;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseViewModel;

import static de.avalax.fitbuddy.domain.model.workout.BasicWorkoutBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EditWorkoutViewModelTest {
    private Workout workout;

    private EditWorkoutViewModel editWorkoutViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        workout = aWorkout().withName("workout name").build();
        editWorkoutViewModel = new EditWorkoutViewModel();
    }

    @Test
    public void onInit_shouldSetWeightAndReps() {
        editWorkoutViewModel.init(workout);

        assertThat(editWorkoutViewModel.getName().getValue()).isEqualTo("workout name");
        assertThat(editWorkoutViewModel.getExercises().getValue()).isEqualTo(workout.getExercises());
    }

}