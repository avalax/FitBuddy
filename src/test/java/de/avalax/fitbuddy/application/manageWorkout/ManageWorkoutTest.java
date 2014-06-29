package de.avalax.fitbuddy.application.manageWorkout;

import android.view.View;
import de.avalax.fitbuddy.application.WorkoutSession;
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
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
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

    @Mock
    private Exercise exercise;

    private Workout workout;

    private LinkedList<Exercise> exercises;

    @Before
    public void setUp() throws Exception {
        exercises = new LinkedList<>();
        workout = new BasicWorkout("", exercises);
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
        when(workoutService.fromJson("jsonstring")).thenReturn(workout);
        manageWorkout.createWorkoutFromJson("jsonstring");
        verify(workoutRepository).save(workout);
        assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
    }

    @Test
    public void deleteUnknownWorkout_shouldDoNothing() throws Exception {
        manageWorkout.deleteWorkout();

        verifyNoMoreInteractions(workoutRepository);
    }

    @Test(expected = WorkoutNotFoundException.class)
    public void switchWorkoutWithoutAWorkout_shouldThrowException() throws Exception {
        manageWorkout.switchWorkout();
    }

    public class givenAWorkoutWithOneExercise {
        private WorkoutId workoutId;

        @Before
        public void setUp() throws Exception {
            workoutId = new WorkoutId("42");
            workout.setWorkoutId(workoutId);
            when(workoutRepository.load(workoutId)).thenReturn(workout);
            exercises.add(exercise);

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

            verify(workoutRepository).save(workout);
            assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
        }

        @Test
        public void createExerciseAfter_shouldPlaceTheExerciseAfterTheGiven() throws Exception {
            manageWorkout.createExerciseAfter(exercise);

            assertThat(workout.getExercises(), hasSize(2));
            int indexOfNewExercise = exercises.indexOf(exercise) + 1;
            Exercise newExercise = workout.getExercises().get(indexOfNewExercise);
            verify(workoutRepository).save(workout);
            assertThat(newExercise, not(equalTo(exercise)));
            assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
        }

        @Test
        public void createExerciseBefore_shouldPlaceTheExerciseBeforeTheGiven() throws Exception {
            manageWorkout.createExerciseBefore(exercise);

            assertThat(workout.getExercises(), hasSize(2));
            int indexOfNewExercise = exercises.indexOf(exercise) - 1;
            Exercise newExercise = workout.getExercises().get(indexOfNewExercise);
            verify(workoutRepository).save(workout);
            assertThat(newExercise, not(equalTo(exercise)));
            assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
        }

        @Test
        public void switchWorkout_shouldSetWorkout() throws Exception {
            manageWorkout.switchWorkout();

            verify(workoutSession).switchWorkoutById(workoutId);
            assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
        }

        public class exerciseManipulation {
            @Test
            public void replaceExercise_shouldSaveExerciseToPersistence() throws Exception {
                exercise = new BasicExercise();
                exercise.setExerciseId(new ExerciseId("42"));
                exercises.clear();
                exercises.add(exercise);
                Exercise exerciseToReplace = new BasicExercise();
                exerciseToReplace.setExerciseId(exercise.getExerciseId());
                exerciseToReplace.setName("replacement");

                manageWorkout.replaceExercise(42, exerciseToReplace);

                assertThat(exercises, hasSize(1));
                assertThat(exercises.get(0).getName(), equalTo("replacement"));
                verify(exerciseRepository).save(workout.getWorkoutId(), 42, exerciseToReplace);
                assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
            }

            @Test
            public void deleteExercise_shouldDeleteThePersistdExercise() throws Exception {
                ExerciseId exerciseId = new ExerciseId("42");
                when(exercise.getExerciseId()).thenReturn(exerciseId);

                manageWorkout.deleteExercise(exercise);

                assertThat(workout.getExercises(), not(hasItem(exercise)));
                verify(exerciseRepository).delete(exerciseId);
                assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.VISIBLE));
                assertThat(manageWorkout.hasDeletedExercise(), equalTo(true));
            }

            @Test
            public void undoDeleteExercise_shouldReinsertTheExerciseToThePersistence() throws Exception {
                int size = exercises.size();
                manageWorkout.deleteExercise(exercise);

                manageWorkout.undoDeleteExercise();

                verify(exerciseRepository).save(workout.getWorkoutId(), 0, exercise);
                assertThat(manageWorkout.getWorkout().getExercises(), hasSize(size));
                assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
                assertThat(manageWorkout.hasDeletedExercise(), equalTo(false));
            }

            @Test
            public void undoDeleteExercise_shouldReinsertTheExerciseAtOldPosition() throws Exception {
                Exercise exerciseToRestore = mock(Exercise.class);
                exercises.add(exerciseToRestore);
                exercises.add(mock(Exercise.class));

                manageWorkout.deleteExercise(exerciseToRestore);
                manageWorkout.undoDeleteExercise();

                assertThat(workout.getExercises().get(1), equalTo(exerciseToRestore));
            }

            @Test
            public void undoDeleteExerciseAfterDeleteAnWorkout_shouldReinsertTheExercise() throws Exception {
                manageWorkout.createWorkout();
                manageWorkout.deleteWorkout();
                manageWorkout.setWorkout(workout.getWorkoutId());
                manageWorkout.deleteExercise(exercise);

                manageWorkout.undoDeleteExercise();

                assertThat(manageWorkout.hasDeletedExercise(), equalTo(false));
                assertThat(manageWorkout.hasDeletedWorkout(), equalTo(false));
            }

            @Test
            public void undoDeleteWorkoutAfterDeleteAnExercise_shouldReinsertTheWorkout() throws Exception {
                manageWorkout.deleteExercise(exercise);
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
                assertThat(exercise.getSets(), hasSize(1));
                assertThat(exercise.getSets(), hasItem(setToAdd));
            }
        }
    }

}