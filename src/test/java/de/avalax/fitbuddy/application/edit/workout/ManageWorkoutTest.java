package de.avalax.fitbuddy.application.edit.workout;

import android.view.View;
import de.avalax.fitbuddy.application.workout.WorkoutSession;
import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetId;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.*;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.*;

@RunWith(HierarchicalContextRunner.class)
public class ManageWorkoutTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private SetRepository setRepository;

    @Mock
    private WorkoutService workoutService;

    @Mock
    private WorkoutSession workoutSession;

    @InjectMocks
    private ManageWorkout manageWorkout;

    private Workout workout;

    @Before
    public void setUp() throws Exception {
        workout = new BasicWorkout();
        MockitoAnnotations.initMocks(this);
        workout.setWorkoutId(new WorkoutId("123456"));
    }

    @Test
    public void afterInitialization_shouldHideUnsavedChanges() throws Exception {
        assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
    }

    @Test
    public void getWorkoutList_shouldWorkoutListFromRepository() throws Exception {
        List<WorkoutListEntry> workoutListEntries = new ArrayList<>();
        when(workoutRepository.getWorkoutList()).thenReturn(workoutListEntries);
        List<WorkoutListEntry> workoutList = manageWorkout.getWorkoutList();
        assertThat(workoutList, equalTo(workoutListEntries));
    }

    @Test
    public void createWorkout_shouldPersistTheCreatedWorkout() throws Exception {
        Workout newWorkout = manageWorkout.createWorkout();
        verify(workoutRepository).save(newWorkout);
        assertThat(newWorkout, not(equalTo(workout)));
        assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
    }

    @Test
    public void createWorkoutFromJson_shouldPersistTheCreatedWorkout() throws Exception {
        when(workoutService.workoutFromJson("jsonstring")).thenReturn(workout);
        manageWorkout.createWorkoutFromJson("jsonstring");
        verify(workoutRepository).save(workout);
        assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
    }

    @Test
    public void deleteUnknownWorkout_shouldDoNothing() throws Exception {
        manageWorkout.deleteWorkout();

        verifyNoMoreInteractions(workoutRepository);
    }

    public class givenAWorkoutWithOneExercise {
        private WorkoutId workoutId;

        private Exercise exercise;

        @Before
        public void setUp() throws Exception {
            workoutId = new WorkoutId("42");
            workout.setWorkoutId(workoutId);
            when(workoutRepository.load(workoutId)).thenReturn(workout);
            exercise = workout.createExercise();

            manageWorkout.setWorkout(workoutId);
        }

        @Test
        public void changeWorkoutName_shouldSavePersistence() throws Exception {
            String name = "new name";
            manageWorkout.changeName(name);

            assertThat(workout.getName(), equalTo(name));
            verify(workoutRepository).save(workout);
            assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
        }

        @Test
        public void deleteWorkout_shouldRemoveTheWorkoutFromThePersistence() throws Exception {
            manageWorkout.deleteWorkout();

            verify(workoutRepository).delete(workoutId);
            assertThat(manageWorkout.getWorkout(), equalTo(null));
            assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.VISIBLE));
            assertThat(manageWorkout.hasDeletedWorkout(), equalTo(true));
        }

        @Test
        public void undoDeleteWorkout_shouldReinsertTheWorkoutToThePersistence() throws Exception {
            manageWorkout.deleteWorkout();

            manageWorkout.undoDeleteWorkout();

            verify(workoutRepository).save(workout);
            assertThat(manageWorkout.getWorkout(), equalTo(workout));
            assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
            assertThat(manageWorkout.hasDeletedWorkout(), equalTo(false));
        }

        @Test
        public void createExercise_shouldPersistTheExercise() throws Exception {
            manageWorkout.createExercise();

            verify(exerciseRepository).save(workout.getWorkoutId(), 1, workout.exerciseAtPosition(1));
            assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
        }

        @Test
        public void createExerciseAfter_shouldPlaceTheExerciseAfterTheGiven() throws Exception {
            manageWorkout.createExerciseAfter(0);

            assertThat(workout.countOfExercises(), equalTo(2));
            Exercise newExercise = workout.exerciseAtPosition(1);
            verify(workoutRepository).save(workout);
            assertThat(newExercise, not(equalTo(exercise)));
            assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
        }

        @Test
        public void createExerciseBefore_shouldPlaceTheExerciseBeforeTheGiven() throws Exception {
            manageWorkout.createExerciseBefore(0);

            assertThat(workout.countOfExercises(), equalTo(2));
            Exercise newExercise = workout.exerciseAtPosition(0);
            verify(workoutRepository).save(workout);
            assertThat(newExercise, not(equalTo(exercise)));
            assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
        }

        @Test
        public void saveExercise_shouldSaveExerciseInRepository() throws Exception {
            Exercise exercise = workout.createExercise();
            exercise.setExerciseId(new ExerciseId("42"));
            Exercise changedExercise = new BasicExercise();
            changedExercise.setName("changed exercise");
            changedExercise.setExerciseId(exercise.getExerciseId());

            manageWorkout.saveExercise(changedExercise, 1);

            assertThat(workout.exerciseAtPosition(1).getName(), equalTo("changed exercise"));
            verify(exerciseRepository).save(workout.getWorkoutId(), 1, changedExercise);
            assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
        }

        @Test
        public void switchWorkout_shouldSetWorkout() throws Exception {
            manageWorkout.switchWorkout();

            verify(workoutSession).switchWorkout(workout);
            assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
        }

        public class exerciseManipulation {

            @Test
            public void deleteExercise_shouldDeleteThePersistdExercise() throws Exception {
                ExerciseId exerciseId = new ExerciseId("42");
                exercise.setExerciseId(exerciseId);

                manageWorkout.deleteExercise(exercise, 0);

                assertThat(workout.countOfExercises(), equalTo(0));
                verify(exerciseRepository).delete(exerciseId);
                assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.VISIBLE));
                assertThat(manageWorkout.hasDeletedExercise(), equalTo(true));
            }

            @Test
            public void undoDeleteExercise_shouldReinsertTheExerciseToThePersistence() throws Exception {
                int size = workout.countOfExercises();
                manageWorkout.deleteExercise(exercise, 0);

                manageWorkout.undoDeleteExercise();

                verify(exerciseRepository).save(workout.getWorkoutId(), 0, exercise);
                assertThat(manageWorkout.getWorkout().countOfExercises(), equalTo(size));
                assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
                assertThat(manageWorkout.hasDeletedExercise(), equalTo(false));
            }

            @Test
            public void undoDeleteExercise_shouldReinsertTheExerciseAtOldPosition() throws Exception {
                Exercise exerciseToRestore = workout.createExercise();
                workout.createExercise();

                manageWorkout.deleteExercise(exerciseToRestore, 1);
                manageWorkout.undoDeleteExercise();

                assertThat(workout.exerciseAtPosition(1), equalTo(exerciseToRestore));
            }

            @Test
            public void undoDeleteExerciseAfterDeleteAnWorkout_shouldReinsertTheExercise() throws Exception {
                manageWorkout.createWorkout();
                manageWorkout.deleteWorkout();
                manageWorkout.setWorkout(workout.getWorkoutId());
                manageWorkout.deleteExercise(exercise, 0);

                manageWorkout.undoDeleteExercise();

                assertThat(manageWorkout.hasDeletedExercise(), equalTo(false));
                assertThat(manageWorkout.hasDeletedWorkout(), equalTo(false));
            }

            @Test
            public void undoDeleteWorkoutAfterDeleteAnExercise_shouldReinsertTheWorkout() throws Exception {
                manageWorkout.deleteExercise(exercise, 0);
                manageWorkout.deleteWorkout();

                manageWorkout.undoDeleteWorkout();
                assertThat(manageWorkout.hasDeletedExercise(), equalTo(false));
                assertThat(manageWorkout.hasDeletedWorkout(), equalTo(false));
            }

            @Test
            public void replaceSets_shouldDeleteOldSetsAndSaveNewSets() throws Exception {
                ExerciseId exerciseId = new ExerciseId("21");
                Exercise exercise = new BasicExercise();
                exercise.setExerciseId(exerciseId);
                SetId setIdToDelete = new SetId("42");
                Set setToDelete = mock(Set.class);
                when(setToDelete.getSetId()).thenReturn(setIdToDelete);
                exercise.addSet(setToDelete);
                List<Set> setsToAdd = new ArrayList<>();
                Set setToAdd = mock(Set.class);
                setsToAdd.add(setToAdd);

                manageWorkout.replaceSets(exercise, setsToAdd);

                InOrder inOrder = inOrder(setRepository);
                inOrder.verify(setRepository).delete(setIdToDelete);
                inOrder.verify(setRepository).save(exerciseId, setToAdd);
                assertThat(exercise.countOfSets(), equalTo(1));
                assertThat(exercise.setAtPosition(0), equalTo(setToAdd));
            }
        }
    }

}