package de.avalax.fitbuddy.edit;

import de.avalax.fitbuddy.workout.Exercise;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExistingEditableExerciseTest {

    Exercise exercise;

    EditableExercise editableExercise;

    @Before
    public void setUp() throws Exception {
        exercise = mock(Exercise.class);
    }

    @Test
    public void testGetName() throws Exception {
        String name = "name from exercise";
        when(exercise.getName()).thenReturn(name);
        editableExercise = new ExistingEditableExercise(exercise);

        assertThat(editableExercise.getName(), equalTo(name));
    }

    @Test
    public void testSetName() throws Exception {
        String name = "new name";
        editableExercise = new ExistingEditableExercise(exercise);

        editableExercise.setName(name);

        assertThat(editableExercise.getName(), equalTo(name));
    }

    @Test
    public void testGetReps() throws Exception {
        int reps = 42;
        when(exercise.getMaxReps()).thenReturn(reps);
        editableExercise = new ExistingEditableExercise(exercise);

        assertThat(editableExercise.getReps(), equalTo(reps));
    }

    @Test
    public void testSetReps() throws Exception {
        int reps = 21;
        editableExercise = new ExistingEditableExercise(exercise);

        editableExercise.setReps(reps);

        assertThat(editableExercise.getReps(), equalTo(reps));
    }

    @Test
    public void testGetSets() throws Exception {
        int sets = 42;
        when(exercise.getMaxSets()).thenReturn(sets);
        editableExercise = new ExistingEditableExercise(exercise);

        assertThat(editableExercise.getSets(), equalTo(sets));
    }

    @Test
    public void testSetSets() throws Exception {
        int sets = 21;
        editableExercise = new ExistingEditableExercise(exercise);

        editableExercise.setSets(sets);

        assertThat(editableExercise.getSets(), equalTo(sets));
    }

    @Test
    public void testGetWeight() throws Exception {
        double weight = 12.5;
        when(exercise.getWeight()).thenReturn(weight);
        editableExercise = new ExistingEditableExercise(exercise);

        assertThat(editableExercise.getWeight(), equalTo(weight));
    }

    @Test
    public void testSetWeight() throws Exception {
        double weight = 21;
        editableExercise = new ExistingEditableExercise(exercise);

        editableExercise.setWeight(weight);

        assertThat(editableExercise.getWeight(), equalTo(weight));
    }

    @Test
    public void testGetWeightRaise() throws Exception {
        double weightRaise = 2.5;
        when(exercise.getWeightRaise()).thenReturn(weightRaise);
        editableExercise = new ExistingEditableExercise(exercise);

        assertThat(editableExercise.getWeightRaise(), equalTo(weightRaise));
    }

    @Test
    public void testSetWeightRaise() throws Exception {
        double weightRaise = 5;
        editableExercise = new ExistingEditableExercise(exercise);

        editableExercise.setWeightRaise(weightRaise);

        assertThat(editableExercise.getWeightRaise(), equalTo(weightRaise));
    }

    @Test
    public void testCreateExercise() throws Exception {
        editableExercise = new ExistingEditableExercise(exercise);
        editableExercise.setName("existing exercise");
        editableExercise.setWeight(5.0);
        editableExercise.setWeightRaise(2.5);
        editableExercise.setSets(3);
        editableExercise.setReps(12);

        Exercise exercise = editableExercise.createExercise();

        assertThat(exercise.getName(), equalTo(editableExercise.getName()));
        assertThat(exercise.getWeight(), equalTo(editableExercise.getWeight()));
        assertThat(exercise.getWeightRaise(), equalTo(editableExercise.getWeightRaise()));
        assertThat(exercise.getMaxSets(), equalTo(editableExercise.getSets()));
        assertThat(exercise.getMaxReps(), equalTo(editableExercise.getReps()));
    }
}
