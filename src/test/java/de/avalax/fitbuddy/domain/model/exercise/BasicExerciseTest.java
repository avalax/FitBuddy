package de.avalax.fitbuddy.domain.model.exercise;


import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetNotAvailableException;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

@RunWith(HierarchicalContextRunner.class)
public class BasicExerciseTest {

    Exercise exercise;
    List<Set> sets;

    @Before
    public void setUp() throws Exception {
        sets = new ArrayList<>();
        exercise = new BasicExercise("Bankdrücken", sets);
    }

    @Test
    public void testSameIdentity() throws Exception {
        Exercise a1 = new BasicExercise("Bankdrücken", sets);
        ExerciseId exerciseId = new ExerciseId("42");
        a1.setExerciseId(exerciseId);
        Exercise a2 = new BasicExercise("Bankdrücken", sets);
        a2.setExerciseId(exerciseId);
        Assert.assertThat(a1, equalTo(a2));
        Assert.assertThat(a1.hashCode(), equalTo(a2.hashCode()));
    }

    @Test
    public void testDifferentIdentity() throws Exception {
        Exercise a1 = new BasicExercise("Bankdrücken", sets);
        a1.setExerciseId(new ExerciseId("21"));
        Exercise a2 = new BasicExercise("Bankdrücken", sets);
        a2.setExerciseId(new ExerciseId("42"));
        Assert.assertThat(a1, not(equalTo(a2)));
        Assert.assertThat(a1.hashCode(), not(equalTo(a2.hashCode())));
    }

    @Test
    public void testDifferentIdentityWithNoId() throws Exception {
        Exercise a1 = new BasicExercise("Bankdrücken", sets);
        Exercise a2 = new BasicExercise("Bankdrücken", sets);
        Assert.assertThat(a1, not(equalTo(a2)));
        Assert.assertThat(a1.hashCode(), not(equalTo(a2.hashCode())));
    }

    @Test
    public void getName_shouldReturnNameBankdrücken() throws Exception {
        assertThat(exercise.getName(), equalTo("Bankdrücken"));
    }

    @Test
    public void getId_shouldReturnId() throws Exception {
        ExerciseId exerciseId = new ExerciseId("42");

        exercise.setExerciseId(exerciseId);
        assertThat(exercise.getExerciseId(), equalTo(exerciseId));
    }

    @Test
    public void getSets_shouldReturnSets() throws Exception {
        assertThat(exercise.getSets(), equalTo(sets));
    }

    @Test
    public void setSets_shouldSetSets() throws Exception {
        List<Set> sets = new ArrayList<>();
        exercise.setSets(sets);
        assertThat(exercise.getSets(), equalTo(sets));
        assertThat(exercise.getSetNumber(), equalTo(1));
    }

    @Test
    public void setName_shouldSetName() throws Exception {
        String name = "newName";
        exercise.setName(name);
        assertThat(exercise.getName(), equalTo(name));
    }

    @Test
    public void getMaxSets_shouldReturnNumberOfSets() throws Exception {
        sets.add(mock(Set.class));
        sets.add(mock(Set.class));
        sets.add(mock(Set.class));
        sets.add(mock(Set.class));

        assertThat(exercise.getMaxSets(), equalTo(4));
    }

    @Test
    public void getMaxReps_shouldReturnMaxRepsFromCurrentSet() throws Exception {
        Set set = mock(Set.class);
        int maxReps = 12;
        when(set.getMaxReps()).thenReturn(maxReps);
        sets.add(set);

        assertThat(exercise.getMaxReps(), equalTo(maxReps));
    }

    @Test
    public void getReps_shouldReturnRepsFromSet() throws Exception {
        Set set = mock(Set.class);
        sets.add(set);
        when(set.getReps()).thenReturn(12);

        assertThat(exercise.getReps(), equalTo(12));
    }

    @Test
    public void getCurrentSet_shouldReturnCurrentSetOnStartup() throws Exception {
        Set set = mock(Set.class);
        sets.add(set);

        assertThat(exercise.getCurrentSet(), equalTo(set));
    }

    @Test
    public void getCurrentSet_shouldReturnSecondSet() throws Exception {
        Set set = mock(Set.class);

        sets.add(mock(Set.class));
        sets.add(set);

        exercise.setCurrentSet(2);

        assertThat(exercise.getCurrentSet(), equalTo(set));
    }

    @Test
    public void setSetNumber_shouldSetSetNumberToZero() throws Exception {
        sets.add(mock(Set.class));

        exercise.setCurrentSet(-1);

        assertThat(exercise.getSetNumber(), equalTo(1));
    }

    @Test
    public void setCurrentSet_ShouldSetToLastSetWhenSizeExceeded() throws Exception {
        sets.add(mock(Set.class));

        exercise.setCurrentSet(sets.size() + 1);
    }

    @Test
    public void incrementCurrentSet_shouldIncrementToTheNextSet() throws Exception {
        Set set = mock(Set.class);

        sets.add(mock(Set.class));
        sets.add(set);

        exercise.incrementCurrentSet();

        assertThat(exercise.getCurrentSet(), equalTo(set));
        assertThat(exercise.getSetNumber(), equalTo(2));
    }

    @Test
    public void setReps_shouldCallSetRepsForCurrentSet() throws Exception {
        Set set = mock(Set.class);
        sets.add(set);

        exercise.setReps(12);

        verify(set).setReps(12);
    }

    @Test
    public void getWeight_shouldReturnWeightOfCurrentSet() throws Exception {
        Set set = mock(Set.class);
        sets.add(set);

        when(set.getWeight()).thenReturn(50.0);

        assertThat(exercise.getWeight(), equalTo(50.0));
    }

    @Test(expected = SetNotAvailableException.class)
    public void getCurrentSet_shouldThrowSetNotFoundExceptionWhenNoSetsAvailable() throws Exception {
        exercise.getCurrentSet();
    }

    public class givenAnExerciseProgress {
        @Test(expected = SetNotAvailableException.class)
        public void withoutSets_shouldHaveZeroProgress() throws Exception {
            exercise.getProgress();
        }

        @Test
        public void oneSetWithoutReps_shouldHaveZeroProgress() throws Exception {
            Set set = mock(Set.class);
            when(set.getMaxReps()).thenReturn(100);
            when(set.getReps()).thenReturn(0);
            sets.add(set);
            assertThat(exercise.getProgress(), equalTo(0.0));
        }

        @Test
        public void oneSetWithMaxReps_shouldHaveFullProgress() throws Exception {
            Set set = mock(Set.class);
            when(set.getMaxReps()).thenReturn(100);
            when(set.getReps()).thenReturn(100);
            sets.add(set);
            assertThat(exercise.getProgress(), equalTo(1.0));
        }

        @Test
        public void oneSetWithMaxReps_shouldHaveHalfProgress() throws Exception {
            Set set = mock(Set.class);
            when(set.getMaxReps()).thenReturn(100);
            when(set.getReps()).thenReturn(50);
            sets.add(set);
            assertThat(exercise.getProgress(), equalTo(0.5));
        }

        @Test
        public void twoSetsWithoutReps_shouldHaveHalfProgress() throws Exception {
            sets.add(mock(Set.class));
            Set set = mock(Set.class);
            when(set.getMaxReps()).thenReturn(100);
            when(set.getReps()).thenReturn(0);
            sets.add(set);
            exercise.setCurrentSet(2);
            assertThat(exercise.getProgress(), equalTo(0.5));
        }

        @Test
        public void twoSetsWithMaxReps_shouldHaveFallProgress() throws Exception {
            sets.add(mock(Set.class));
            Set set = mock(Set.class);
            when(set.getMaxReps()).thenReturn(100);
            when(set.getReps()).thenReturn(100);
            sets.add(set);
            exercise.setCurrentSet(2);
            assertThat(exercise.getProgress(), equalTo(1.0));
        }

        @Test
        public void twoSetsWithHalfReps_shouldHave75Progress() throws Exception {
            sets.add(mock(Set.class));
            Set set = mock(Set.class);
            when(set.getMaxReps()).thenReturn(100);
            when(set.getReps()).thenReturn(50);
            sets.add(set);
            exercise.setCurrentSet(2);
            assertThat(exercise.getProgress(), equalTo(0.75));
        }
    }
}
