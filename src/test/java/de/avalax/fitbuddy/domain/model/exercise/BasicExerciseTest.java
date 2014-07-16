package de.avalax.fitbuddy.domain.model.exercise;


import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetNotAvailableException;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(HierarchicalContextRunner.class)
public class BasicExerciseTest {
    private Exercise exercise;

    @Before
    public void setUp() throws Exception {
        exercise = new BasicExercise();
    }

    @Test
    public void equalExerciseId_shouldHaveTheSameIdentity() throws Exception {
        ExerciseId exerciseId = new ExerciseId("42");
        exercise.setExerciseId(exerciseId);
        Exercise exercise2 = new BasicExercise();
        exercise2.setExerciseId(exerciseId);

        assertThat(exercise, equalTo(exercise2));
        assertThat(exercise.hashCode(), equalTo(exercise2.hashCode()));
    }

    @Test
    public void differentExerciseIds_shouldHaveDifferentIdentity() throws Exception {
        exercise.setExerciseId(new ExerciseId("21"));
        Exercise exercise2 = new BasicExercise();
        exercise2.setExerciseId(new ExerciseId("42"));

        assertThat(exercise, not(equalTo(exercise2)));
        assertThat(exercise.hashCode(), not(equalTo(exercise2.hashCode())));
    }

    @Test
    public void nullValue_shouldHaveDifferentIdentity() throws Exception {
        Exercise exercise2 = new BasicExercise();

        assertThat(exercise, not(equalTo(exercise2)));
        assertThat(exercise.hashCode(), not(equalTo(exercise2.hashCode())));
    }

    @Test
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    public void differntObject_shouldHaveDifferentIdentity() throws Exception {
        exercise.setExerciseId(new ExerciseId("42"));
        assertThat(exercise.equals("42"), is(false));
    }

    @Test
    public void nullValue_shouldSetNameToEmptyString() throws Exception {
        exercise.setName(null);

        assertThat(exercise.getName(), equalTo(""));
    }

    @Test
    public void setName_shouldSetNameOfExercise() throws Exception {
        String name = "newName";

        exercise.setName(name);

        assertThat(exercise.getName(), equalTo(name));
    }

    @Test
    public void setNameWithSpace_shouldSetTrimedName() throws Exception {
        String name = " newName ";

        exercise.setName(name);

        assertThat(exercise.getName(), equalTo("newName"));
    }

    @Test
    public void getId_shouldReturnId() throws Exception {
        ExerciseId exerciseId = new ExerciseId("42");
        exercise.setExerciseId(exerciseId);

        assertThat(exercise.getExerciseId(), equalTo(exerciseId));
    }

    @Test
    public void toString_shouldReturnSetInformations() throws Exception {
        String name = "NameOfExercise";
        exercise.setName(name);

        assertThat(exercise.toString(), equalTo("BasicExercise [name=" + name + "]"));
        ExerciseId exerciseId = new ExerciseId("42");
        exercise.setExerciseId(exerciseId);
        assertThat(exercise.toString(), equalTo("BasicExercise [name=" + name + ", exerciseId=" + exerciseId.toString() + "]"));
    }

    public class givenAnExerciseWithSets {
        @Before
        public void setUp() throws Exception {
            exercise = new BasicExercise();
        }

        @Test
        public void getName_shouldReturnNameFromInitialization() throws Exception {
            assertThat(exercise.getName(), equalTo(""));
        }

        @Test
        public void getSets_shouldReturnEmptyListOfSetsOnConstruction() throws Exception {
            assertThat(exercise.getSets(), emptyCollectionOf(Set.class));
        }

        @Test
        public void addSet_shouldAddSetToSets() throws Exception {
            Set set = mock(Set.class);

            exercise.addSet(set);

            List<Set> sets = exercise.getSets();
            assertThat(sets.get(sets.size() - 1), equalTo(set));
        }

        @Test
        public void removeSet_shouldRemoveSetFromSets() throws Exception {
            Set set = mock(Set.class);

            exercise.addSet(set);
            exercise.removeSet(set);
            assertThat(exercise.getSets(), not(hasItem(set)));
        }

        @Test
        public void getCurrentSet_shouldReturnCurrent() throws Exception {
            Set set = mock(Set.class);
            exercise.addSet(set);

            assertThat(exercise.getCurrentSet(), equalTo(set));
        }

        @Test
        public void getCurrentSet_shouldReturnSecondSet() throws Exception {
            Set set = mock(Set.class);

            exercise.addSet(mock(Set.class));
            exercise.addSet(set);

            exercise.setCurrentSet(1);

            assertThat(exercise.getCurrentSet(), equalTo(set));
        }

        @Test
        public void setCurrentSetWithoutSets_shouldDoNothing() throws Exception {
            exercise.setCurrentSet(1);
        }

        @Test
        public void setSetNumber_shouldSetSetNumberToZero() throws Exception {
            exercise.addSet(mock(Set.class));

            exercise.setCurrentSet(-1);

            assertThat(exercise.indexOfCurrentSet(), equalTo(0));
        }

        @Test
        public void setCurrentSet_ShouldSetToLastSetWhenSizeExceeded() throws Exception {
            Set set = mock(Set.class);
            exercise.addSet(set);

            exercise.setCurrentSet(exercise.getSets().size() + 1);

            assertThat(exercise.getCurrentSet(), equalTo(set));
        }

        @Test(expected = SetNotAvailableException.class)
        public void getCurrentSet_shouldThrowSetNotFoundExceptionWhenNoSetsAvailable() throws Exception {
            exercise.getCurrentSet();
        }

        public class givenAnExerciseProgress {
            @Test
            public void withoutSets_shouldHaveZeroProgress() throws Exception {
                assertThat(exercise.getProgress(), equalTo(0.0));
            }

            @Test
            public void oneSetWithoutReps_shouldHaveZeroProgress() throws Exception {
                Set set = mock(Set.class);
                when(set.getMaxReps()).thenReturn(100);
                when(set.getReps()).thenReturn(0);
                exercise.addSet(set);
                assertThat(exercise.getProgress(), equalTo(0.0));
            }

            @Test
            public void oneSetWithMaxReps_shouldHaveFullProgress() throws Exception {
                Set set = mock(Set.class);
                when(set.getMaxReps()).thenReturn(100);
                when(set.getReps()).thenReturn(100);
                exercise.addSet(set);
                assertThat(exercise.getProgress(), equalTo(1.0));
            }

            @Test
            public void oneSetWithHalfReps_shouldHaveHalfProgress() throws Exception {
                Set set = mock(Set.class);
                when(set.getMaxReps()).thenReturn(100);
                when(set.getReps()).thenReturn(50);
                exercise.addSet(set);
                assertThat(exercise.getProgress(), equalTo(0.5));
            }

            @Test
            public void twoSetsWithoutReps_shouldHaveHalfProgress() throws Exception {
                exercise.addSet(mock(Set.class));
                Set set = mock(Set.class);
                when(set.getMaxReps()).thenReturn(100);
                when(set.getReps()).thenReturn(0);
                exercise.addSet(set);
                exercise.setCurrentSet(1);
                assertThat(exercise.getProgress(), equalTo(0.5));
            }

            @Test
            public void twoSetsWithMaxReps_shouldHaveFallProgress() throws Exception {
                exercise.addSet(mock(Set.class));
                Set set = mock(Set.class);
                when(set.getMaxReps()).thenReturn(100);
                when(set.getReps()).thenReturn(100);
                exercise.addSet(set);
                exercise.setCurrentSet(1);
                assertThat(exercise.getProgress(), equalTo(1.0));
            }

            @Test
            public void twoSetsWithHalfReps_shouldHave75Progress() throws Exception {
                exercise.addSet(mock(Set.class));
                Set set = mock(Set.class);
                when(set.getMaxReps()).thenReturn(100);
                when(set.getReps()).thenReturn(50);
                exercise.addSet(set);
                exercise.setCurrentSet(1);
                assertThat(exercise.getProgress(), equalTo(0.75));
            }
        }
    }
}
