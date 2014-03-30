package de.avalax.fitbuddy.app.editExercise;

import de.avalax.fitbuddy.core.workout.Exercise;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;


public class NewEditableExerciseTest {
    private EditableExercise editableExercise;

    @Before
    public void setUp() throws Exception {
        editableExercise = new NewEditableExercise();
    }

    @Test
    public void testSetName() throws Exception {
        String name = "new name";
        editableExercise.setName(name);

        assertThat(editableExercise.getName(), equalTo(name));
    }

    @Test
    public void testSetReps() throws Exception {
        int reps = 4;
        editableExercise.setReps(4);

        assertThat(editableExercise.getReps(), equalTo(reps));
    }

    @Test
    public void testSetSets() throws Exception {
        int sets = 4;
        editableExercise.setSets(4);

        assertThat(editableExercise.getSets(), equalTo(sets));
    }

    @Test
    public void testSetWeight() throws Exception {
        double weight = 42.5;
        editableExercise.setWeight(weight);

        assertThat(editableExercise.getWeight(), equalTo(weight));
    }

    @Test
    public void testSetWeightRaise() throws Exception {
        double weightRaise = 42.5;
        editableExercise.setWeightRaise(weightRaise);

        assertThat(editableExercise.getWeightRaise(), equalTo(weightRaise));
    }

    @Test
    public void testCreateExercise() throws Exception {
        editableExercise.setName("new exercise");
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
