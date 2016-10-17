package de.avalax.fitbuddy.application.edit.workout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import de.avalax.fitbuddy.application.workout.WorkoutSession;
import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetId;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutListEntry;
import de.avalax.fitbuddy.domain.model.workout.WorkoutParserService;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.bechte.junit.runners.context.HierarchicalContextRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(HierarchicalContextRunner.class)
public class EditWorkoutApplicationServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private SetRepository setRepository;

    @Mock
    private WorkoutParserService workoutParserService;

    @Mock
    private WorkoutSession workoutSession;

    @Mock
    private FinishedWorkoutRepository finishedWorkoutRepository;

    @InjectMocks
    private EditWorkoutApplicationService editWorkoutApplicationService;

    private Workout workout;

    @Before
    public void setUp() throws Exception {
        workout = new BasicWorkout();
        MockitoAnnotations.initMocks(this);
        workout.setWorkoutId(new WorkoutId("123456"));
    }

    @Test
    public void afterInitialization_shouldHideUnsavedChanges() throws Exception {
        assertThat(editWorkoutApplicationService.hasUnsavedChanges()).isFalse();
    }

    @Test
    public void getWorkoutList_shouldWorkoutListFromRepository() throws Exception {
        List<WorkoutListEntry> workoutListEntries = new ArrayList<>();
        workoutListEntries.add(new WorkoutListEntry(new WorkoutId("42"), "new workout"));
        when(workoutRepository.getWorkoutList()).thenReturn(workoutListEntries);
        List<WorkoutListEntry> workoutList = editWorkoutApplicationService.getWorkoutList();
        assertThat(workoutList).containsAll(workoutListEntries);
    }

    @Test
    public void createWorkout_shouldPersistTheCreatedWorkout() throws Exception {
        Workout newWorkout = editWorkoutApplicationService.createWorkout();
        verify(workoutRepository).save(newWorkout);
        assertThat(newWorkout).isNotEqualTo(workout);
        assertThat(editWorkoutApplicationService.hasUnsavedChanges()).isFalse();
    }

    @Test
    public void createWorkoutFromJson_shouldPersistTheCreatedWorkout() throws Exception {
        when(workoutParserService.workoutFromJson("jsonString")).thenReturn(workout);
        editWorkoutApplicationService.createWorkoutFromJson("jsonString");
        verify(workoutRepository).save(workout);
        assertThat(editWorkoutApplicationService.hasUnsavedChanges()).isFalse();
    }

    public class givenAWorkoutWithOneExercise {
        private WorkoutId workoutId;

        private Exercise exercise;

        @Before
        public void setUp() throws Exception {
            workoutId = new WorkoutId("42");
            workout.setWorkoutId(workoutId);
            when(workoutRepository.load(workoutId)).thenReturn(workout);
            exercise = workout.getExercises().createExercise();
            exercise.setName("ExerciseOne");

            editWorkoutApplicationService.loadWorkout(workoutId);
        }

        @Test
        public void changeWorkoutName_shouldSavePersistence() throws Exception {
            String name = "new name";
            editWorkoutApplicationService.changeName(workout, name);

            assertThat(workout.getName()).isEqualTo(name);
            verify(workoutRepository).save(workout);
            assertThat(editWorkoutApplicationService.hasUnsavedChanges()).isFalse();
        }

        @Test
        public void deleteWorkout_shouldRemoveTheWorkoutFromThePersistence() throws Exception {
            editWorkoutApplicationService.deleteWorkout(workout);

            verify(workoutRepository).delete(workoutId);
            assertThat(editWorkoutApplicationService.hasUnsavedChanges()).isTrue();
            assertThat(editWorkoutApplicationService.hasDeletedWorkout()).isTrue();
        }

        @Test
        public void undoDeleteWorkout_shouldReinsertTheWorkoutToThePersistence() throws Exception {
            editWorkoutApplicationService.deleteWorkout(workout);

            editWorkoutApplicationService.undoDeleteWorkout();

            verify(workoutRepository).save(workout);
            assertThat(editWorkoutApplicationService.hasUnsavedChanges()).isFalse();
            assertThat(editWorkoutApplicationService.hasDeletedWorkout()).isFalse();
        }

        @Test
        public void createExercise_shouldPersistTheExercise() throws Exception {
            editWorkoutApplicationService.createExercise(workout);

            Exercise expectedExercise = workout.getExercises().exerciseAtPosition(1);
            verify(exerciseRepository).save(workout.getWorkoutId(), 1, expectedExercise);
            assertThat(editWorkoutApplicationService.hasUnsavedChanges()).isFalse();
        }

        @Test
        public void saveExercise_shouldSaveExerciseInRepository() throws Exception {
            Exercise exercise = workout.getExercises().createExercise();
            exercise.setExerciseId(new ExerciseId("42"));
            Exercise changedExercise = new BasicExercise();
            changedExercise.setName("changed exercise");
            changedExercise.setExerciseId(exercise.getExerciseId());

            editWorkoutApplicationService.saveExercise(workout.getWorkoutId(), changedExercise, 1);

            verify(exerciseRepository).save(workout.getWorkoutId(), 1, changedExercise);
            assertThat(editWorkoutApplicationService.hasUnsavedChanges()).isFalse();
        }

        @Test
        public void switchWorkout_shouldSetWorkout() throws Exception {
            when(workoutSession.getWorkout()).thenThrow(new WorkoutException());
            editWorkoutApplicationService.switchWorkout(workout);

            verify(workoutSession).switchWorkout(workout);
            assertThat(editWorkoutApplicationService.hasUnsavedChanges()).isFalse();
        }

        @Test
        public void switchWorkout_shouldPersistCurrentWorkout() throws Exception {
            BasicWorkout currentWorkoutToPersist = new BasicWorkout();
            currentWorkoutToPersist.setName("currentWorkoutToPersist");
            when(workoutSession.hasWorkout()).thenReturn(true);
            when(workoutSession.getWorkout()).thenReturn(currentWorkoutToPersist);

            editWorkoutApplicationService.switchWorkout(workout);

            verify(finishedWorkoutRepository).saveWorkout(currentWorkoutToPersist);
        }

        public class moveExercises {
            @Test
            public void moveFirstExerciseAtPositionUp_shouldDoNothing() throws Exception {
                editWorkoutApplicationService.moveExerciseAtPositionUp(workout, 0);

                verify(workoutRepository, never()).save(workout);
                assertThat(workout.getExercises()).containsExactly(exercise);
                assertThat(editWorkoutApplicationService.hasUnsavedChanges()).isFalse();
            }

            @Test
            public void moveExerciseAtPositionUp_shouldPlaceTheExerciseAtPosition0()
                    throws Exception {
                Exercise exerciseToMove = workout.getExercises().createExercise();
                exerciseToMove.setName("ExerciseTwo");

                editWorkoutApplicationService.moveExerciseAtPositionUp(workout, 1);

                verify(workoutRepository).save(workout);
                assertThat(workout.getExercises()).containsExactly(exerciseToMove, exercise);
                assertThat(editWorkoutApplicationService.hasUnsavedChanges()).isFalse();
            }

            @Test
            public void moveLastExerciseAtPositionDown_shouldDoNothing() throws Exception {
                Exercise lastExercise = workout.getExercises().createExercise();

                editWorkoutApplicationService.moveExerciseAtPositionDown(workout, 1);

                verify(workoutRepository, never()).save(workout);
                assertThat(workout.getExercises()).containsExactly(exercise, lastExercise);
                assertThat(editWorkoutApplicationService.hasUnsavedChanges()).isFalse();
            }

            @Test
            public void moveExerciseAtPositionDown_shouldPlaceTheExerciseAtTheRightPosition()
                    throws Exception {
                Exercise exerciseToMove = workout.getExercises().createExercise();
                exerciseToMove.setName("ExerciseToMove");
                Exercise lastExercise = workout.getExercises().createExercise();
                lastExercise.setName("ExerciseLast");

                editWorkoutApplicationService.moveExerciseAtPositionDown(workout, 1);

                verify(workoutRepository).save(workout);
                assertThat(workout.getExercises())
                        .containsExactly(exercise, lastExercise, exerciseToMove);
                assertThat(editWorkoutApplicationService.hasUnsavedChanges()).isFalse();
            }
        }

        public class exerciseManipulation {
            private List<Integer> positions;

            @Before
            public void setUp() throws Exception {
                positions = new ArrayList<>();
            }

            @Test
            public void deleteExercise_shouldDeleteThePersistedExercise() throws Exception {
                ExerciseId exerciseId = new ExerciseId("42");
                exercise.setExerciseId(exerciseId);
                positions.add(0);

                editWorkoutApplicationService.deleteExercises(workout, positions);

                assertThat(workout.getExercises()).isEmpty();
                verify(exerciseRepository).delete(exerciseId);
                assertThat(editWorkoutApplicationService.hasUnsavedChanges()).isTrue();
                assertThat(editWorkoutApplicationService.hasDeletedExercise()).isTrue();
            }

            @Test
            public void undoDeleteExercise_shouldReinsertTheExerciseToThePersistence()
                    throws Exception {
                positions.add(0);
                editWorkoutApplicationService.deleteExercises(workout, positions);

                editWorkoutApplicationService.undoDeleteExercise(workout);

                verify(workoutRepository).save(workout);
                assertThat(editWorkoutApplicationService.hasUnsavedChanges()).isFalse();
                assertThat(editWorkoutApplicationService.hasDeletedExercise()).isFalse();
            }

            @Test
            public void undoDeleteExercise_shouldReinsertTheExerciseAtOldPosition()
                    throws Exception {
                Exercise exerciseToRestore = workout.getExercises().createExercise();
                Exercise lastExercise = workout.getExercises().createExercise();
                positions.add(1);

                editWorkoutApplicationService.deleteExercises(workout, positions);
                editWorkoutApplicationService.undoDeleteExercise(workout);

                assertThat(workout.getExercises())
                        .containsExactly(exercise, exerciseToRestore, lastExercise);
            }

            @Test
            public void undoDeleteExercises_shouldReinsertBothExercisesAtOldPosition()
                    throws Exception {
                Exercise exerciseToRestore = workout.getExercises().createExercise();
                exerciseToRestore.setName("exerciseToRestore");
                Exercise secondExerciseToRestore = workout.getExercises().createExercise();
                secondExerciseToRestore.setName("secondExercise");
                positions.add(1);
                positions.add(0);

                editWorkoutApplicationService.deleteExercises(workout, positions);
                editWorkoutApplicationService.undoDeleteExercise(workout);

                assertThat(workout.getExercises())
                        .containsExactly(exercise, exerciseToRestore, secondExerciseToRestore);
            }

            @Test
            public void undoDeleteExerciseAfterDeleteAnWorkout_shouldReinsertTheExercise()
                    throws Exception {
                positions.add(0);
                editWorkoutApplicationService.createWorkout();
                editWorkoutApplicationService.deleteWorkout(workout);
                editWorkoutApplicationService.loadWorkout(workout.getWorkoutId());
                editWorkoutApplicationService.deleteExercises(workout, positions);

                editWorkoutApplicationService.undoDeleteExercise(workout);

                assertThat(editWorkoutApplicationService.hasDeletedExercise()).isFalse();
                assertThat(editWorkoutApplicationService.hasDeletedWorkout()).isFalse();
            }

            @Test
            public void undoDeleteWorkoutAfterDeleteAnExercise_shouldReinsertTheWorkout()
                    throws Exception {
                positions.add(0);
                editWorkoutApplicationService.deleteExercises(workout, positions);
                editWorkoutApplicationService.deleteWorkout(workout);

                editWorkoutApplicationService.undoDeleteWorkout();
                assertThat(editWorkoutApplicationService.hasDeletedExercise()).isFalse();
                assertThat(editWorkoutApplicationService.hasDeletedWorkout()).isFalse();
            }

            @Test
            public void changeSetAmountToSameAmount_shouldDoNothing() throws Exception {
                Exercise exercise = new BasicExercise();
                exercise.getSets().createSet();

                editWorkoutApplicationService.changeSetAmount(exercise, 1);

                verifyNoMoreInteractions(setRepository);
            }

            @Test
            public void changeSetAmountWithoutSets_shouldAddOneSet() throws Exception {
                ExerciseId exerciseId = new ExerciseId("21");
                Exercise exercise = new BasicExercise();
                exercise.setExerciseId(exerciseId);

                editWorkoutApplicationService.changeSetAmount(exercise, 1);

                verify(setRepository).save(exerciseId, exercise.getSets().setAtPosition(0));
            }

            @Test
            public void changeSetAmountWithoutSets_shouldAddTwoSets() throws Exception {
                ExerciseId exerciseId = new ExerciseId("21");
                Exercise exercise = new BasicExercise();
                exercise.setExerciseId(exerciseId);

                editWorkoutApplicationService.changeSetAmount(exercise, 2);

                assertThat(exercise.getSets().countOfSets()).isEqualTo(2);
            }

            @Test
            public void changeSetAmountToOne_shouldRemoveOneSet() throws Exception {
                ExerciseId exerciseId = new ExerciseId("21");
                Exercise exercise = new BasicExercise();
                exercise.setExerciseId(exerciseId);
                Set set1 = exercise.getSets().createSet();
                set1.setSetId(new SetId("42"));
                Set set2 = exercise.getSets().createSet();

                editWorkoutApplicationService.changeSetAmount(exercise, 1);

                assertThat(exercise.getSets().setAtPosition(0)).isEqualTo(set2);
                verify(setRepository).delete(set1.getSetId());
            }

            @Test
            public void changeSetAmountToOne_shouldRemoveTwoSets() throws Exception {
                ExerciseId exerciseId = new ExerciseId("21");
                Exercise exercise = new BasicExercise();
                exercise.setExerciseId(exerciseId);
                exercise.getSets().createSet();
                exercise.getSets().createSet();
                exercise.getSets().createSet();

                editWorkoutApplicationService.changeSetAmount(exercise, 1);

                assertThat(exercise.getSets().countOfSets()).isEqualTo(1);
            }

            @Test
            public void changeSetAmountWithOneSet_shouldAddASecondEqualSet() throws Exception {
                ExerciseId exerciseId = new ExerciseId("21");
                Exercise exercise = new BasicExercise();
                exercise.setExerciseId(exerciseId);
                Set set = exercise.getSets().createSet();
                set.setMaxReps(12);
                set.setWeight(42.0);

                editWorkoutApplicationService.changeSetAmount(exercise, 2);

                Set newSet = exercise.getSets().setAtPosition(1);

                assertThat(newSet.getMaxReps()).isEqualTo(12);
                assertThat(newSet.getWeight()).isEqualTo(42.0);
                verify(setRepository).save(exerciseId, newSet);
            }
        }
    }
}