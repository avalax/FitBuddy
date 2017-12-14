package de.avalax.fitbuddy.application.edit.workout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.bechte.junit.runners.context.HierarchicalContextRunner;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(HierarchicalContextRunner.class)
public class EditWorkoutApplicationServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @InjectMocks
    private EditWorkoutApplicationService editWorkoutApplicationService;

    private Workout workout;

    @Before
    public void setUp() throws Exception {
        workout = new BasicWorkout();
        MockitoAnnotations.initMocks(this);
        workout.setWorkoutId(new WorkoutId("123456"));
    }

    public class givenAWorkoutWithOneExercise {
        private WorkoutId workoutId;

        private Exercise exercise;

        @Before
        public void setUp() throws Exception {
            workoutId = new WorkoutId("42");
            workout.setWorkoutId(workoutId);
            when(workoutRepository.loadAll()).thenReturn(singletonList(workout));
            exercise = workout.getExercises().createExercise();
            exercise.setName("ExerciseOne");
        }

        @Test
        public void loadAllWorkouts_shouldLoadWorkoutsFromRepository() throws Exception {
            assertThat(editWorkoutApplicationService.loadAllWorkouts()).containsExactly(workout);
        }

        @Test
        public void saveWorkout_shouldSaveWorkoutInRepository() {
            editWorkoutApplicationService.saveWorkout(workout);

            verify(workoutRepository).save(workout);
        }

        @Test
        public void deleteWorkout_shouldRemoveTheWorkoutFromThePersistence() throws Exception {
            editWorkoutApplicationService.deleteWorkout(workout);

            verify(workoutRepository).delete(workoutId);
        }
    }
}