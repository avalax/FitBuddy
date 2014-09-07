package de.avalax.fitbuddy.presentation.edit.workout;

import android.content.Context;
import android.view.View;
import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.presentation.R;
import de.avalax.fitbuddy.presentation.helper.ExerciseViewHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class ExerciseAdapterTest {
    private ExerciseAdapter exerciseAdapter;
    private Exercise exercise;

    @Before
    public void setUp() throws Exception {
        Context context = Robolectric.application.getApplicationContext();
        List<Exercise> exercises = new ArrayList<>();
        exercise = new BasicExercise();
        exercises.add(exercise);
        exerciseAdapter = new ExerciseAdapter(context, R.layout.item_exercise, exercises);
        exerciseAdapter.exerciseViewHelper = new ExerciseViewHelper(Locale.ENGLISH);
    }

    @Test
    public void viewHolder_shouldCreateANewViewHolderWithDefaultValues() throws Exception {
        View view = exerciseAdapter.getView(0, null, null);

        ExerciseAdapter.ExerciseViewHolder viewHolder = (ExerciseAdapter.ExerciseViewHolder) view.getTag();

        assertThat(viewHolder.name.getText().toString(),equalTo(exerciseAdapter.exerciseViewHelper.nameOfExercise(exercise)));
        assertThat(viewHolder.reps.getText().toString(),equalTo(String.valueOf(exerciseAdapter.exerciseViewHelper.maxRepsOfExercise(exercise))));
        assertThat(viewHolder.sets.getText().toString(),equalTo(String.valueOf(exerciseAdapter.exerciseViewHelper.setCountOfExercise(exercise))));
        assertThat(viewHolder.weight.getText().toString(),equalTo(exerciseAdapter.exerciseViewHelper.weightOfExercise(exercise)));
    }

    @Test
    public void viewHolder_shouldReuseViewHolderWithDefaultValues() throws Exception {
        View view = exerciseAdapter.getView(0, null, null);

        View reusedView = exerciseAdapter.getView(0, view, null);

        assertThat(reusedView,equalTo(view));
    }
}