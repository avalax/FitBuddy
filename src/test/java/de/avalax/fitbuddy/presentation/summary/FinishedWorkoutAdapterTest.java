package de.avalax.fitbuddy.presentation.summary;

import android.content.Context;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.avalax.fitbuddy.BuildConfig;
import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExercise;
import de.avalax.fitbuddy.domain.model.finished_workout.BasicFinishedWorkout;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = "src/main/AndroidManifest.xml", sdk=21)
public class FinishedWorkoutAdapterTest {
    private FinishedWorkoutAdapter finishedWorkoutAdapter;
    private FinishedWorkout finishedWorkout;

    @Before
    public void setUp() throws Exception {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        List<FinishedWorkout> finishedWorkouts = new ArrayList<>();
        finishedWorkout = new BasicFinishedWorkout(new FinishedWorkoutId("42"),new WorkoutId("21"),"name","2014-12-24", Collections.<FinishedExercise>emptyList());
        finishedWorkouts.add(finishedWorkout);
        finishedWorkoutAdapter = new FinishedWorkoutAdapter(context, R.layout.item_finished_workout, finishedWorkouts);
    }

    @Test
    public void viewHolder_shouldCreateANewViewHolderWithDefaultValues() throws Exception {
        View view = finishedWorkoutAdapter.getView(0, null, null);

        FinishedWorkoutAdapter.FinishedWorkoutViewHolder viewHolder = (FinishedWorkoutAdapter.FinishedWorkoutViewHolder) view.getTag();

        assertThat(viewHolder.name.getText().toString(),equalTo(finishedWorkout.getName()));
        assertThat(viewHolder.date.getText().toString(),equalTo("2014-12-24"));
    }

    @Test
    public void viewHolder_shouldReuseViewHolderWithDefaultValues() throws Exception {
        View view = finishedWorkoutAdapter.getView(0, null, null);

        View reusedView = finishedWorkoutAdapter.getView(0, view, null);

        assertThat(reusedView,equalTo(view));
    }
}