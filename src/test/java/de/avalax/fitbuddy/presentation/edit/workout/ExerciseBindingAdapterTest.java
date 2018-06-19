package de.avalax.fitbuddy.presentation.edit.workout;

import android.content.Context;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Locale;

import de.avalax.fitbuddy.BuildConfig;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;

import static de.avalax.fitbuddy.domain.model.exercise.BasicExerciseBuilder.anExercise;
import static de.avalax.fitbuddy.domain.model.set.BasicSetBuilder.aSet;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ExerciseBindingAdapterTest {

    @Mock
    private TextView textView;

    @Before
    public void setUp() throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        MockitoAnnotations.initMocks(this);
        Context context = RuntimeEnvironment.application.getApplicationContext();
        doReturn(context).when(textView).getContext();
    }

    @Test
    public void noSets_shouldSetEmptyReps() throws Exception {
        Exercise exercise = anExercise()
                .build();

        ExerciseBindingAdapter.setRepsFromExercise(textView, exercise);

        verify(textView).setText("");
    }

    @Test
    public void oneSet_shouldSetRepsOfSet() throws Exception {
        Exercise exercise = anExercise()
                .withSet(aSet().withMaxReps(12))
                .build();

        ExerciseBindingAdapter.setRepsFromExercise(textView, exercise);

        verify(textView).setText("1 x 12");
    }

    @Test
    public void twoSetsWithSameReps_shouldSetRepsOfSets() throws Exception {
        Exercise exercise = anExercise()
                .withSet(aSet().withMaxReps(12))
                .withSet(aSet().withMaxReps(12))
                .build();

        ExerciseBindingAdapter.setRepsFromExercise(textView, exercise);

        verify(textView).setText("2 x 12");
    }

    @Test
    public void threeSetsWithDifferentReps_shouldSetSetsAsList() throws Exception {
        Exercise exercise = anExercise()
                .withSet(aSet().withMaxReps(12))
                .withSet(aSet().withMaxReps(10))
                .withSet(aSet().withMaxReps(8))
                .build();

        ExerciseBindingAdapter.setRepsFromExercise(textView, exercise);

        verify(textView).setText("12 - 10 - 8");
    }

    @Test
    public void noSet_shouldSetDefaultLabel() throws Exception {
        Exercise exercise = anExercise()
                .build();

        ExerciseBindingAdapter.setWeightFromExercise(textView, exercise);

        verify(textView).setText("no weight");
    }

    @Test
    public void oneSetWithNoWeight_shouldSetDefaultLabel() throws Exception {
        Exercise exercise = anExercise()
                .withSet(aSet())
                .build();

        ExerciseBindingAdapter.setWeightFromExercise(textView, exercise);

        verify(textView).setText("no weight");
    }

    @Test
    public void oneSet_shouldSetWeight() throws Exception {
        Exercise exercise = anExercise()
                .withSet(aSet().withWeight(42))
                .build();

        ExerciseBindingAdapter.setWeightFromExercise(textView, exercise);

        verify(textView).setText("42 kg");
    }

    @Test
    public void oneSetWithDoubleWeight_shouldSetWeight() throws Exception {
        Exercise exercise = anExercise()
                .withSet(aSet().withWeight(42.5))
                .build();

        ExerciseBindingAdapter.setWeightFromExercise(textView, exercise);

        verify(textView).setText("42.5 kg");
    }

    @Test
    public void twoSets_shouldSetHighestWeight() throws Exception {
        Exercise exercise = anExercise()
                .withSet(aSet().withWeight(0))
                .withSet(aSet().withWeight(42))
                .withSet(aSet().withWeight(21))
                .build();

        ExerciseBindingAdapter.setWeightFromExercise(textView, exercise);

        verify(textView).setText("42 kg");
    }
}