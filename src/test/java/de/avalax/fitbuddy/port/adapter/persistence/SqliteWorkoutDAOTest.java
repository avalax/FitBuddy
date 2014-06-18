package de.avalax.fitbuddy.port.adapter.persistence;

import de.avalax.fitbuddy.application.R;
import de.avalax.fitbuddy.application.manageWorkout.ManageWorkoutActivity;
import de.avalax.fitbuddy.domain.model.Exercise;
import de.avalax.fitbuddy.domain.model.Workout;
import de.avalax.fitbuddy.domain.model.WorkoutId;
import de.avalax.fitbuddy.domain.model.basic.BasicWorkout;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class SqliteWorkoutDAOTest {

    private SqliteWorkoutDAO sqliteWorkoutDAO;

    @Before
    public void setUp() throws Exception {
        ManageWorkoutActivity manageWorkoutActivity = Robolectric.buildActivity(ManageWorkoutActivity.class).create().get();
        sqliteWorkoutDAO = new SqliteWorkoutDAO(manageWorkoutActivity, R.raw.fitbuddy_db);
    }

    @Test
    public void saveUnpersistedWorkout_shouldAssignNewWorkoutId() {
        Workout workout = new BasicWorkout(new LinkedList<Exercise>());

        assertThat(workout.getWorkoutId(), nullValue());
        sqliteWorkoutDAO.save(workout);
        assertThat(workout.getWorkoutId(), any(WorkoutId.class));
    }

    @Test
    public void savePersistedWorkout_shouldKeepWorkoutId() {
        Workout workout = new BasicWorkout(new LinkedList<Exercise>());
        sqliteWorkoutDAO.save(workout);
        WorkoutId workoutId = workout.getWorkoutId();

        sqliteWorkoutDAO.save(workout);
        assertThat(workout.getWorkoutId(), equalTo(workoutId));
    }

    @Test
    public void loadUnknownWorkoutId_shouldReturnNullValue() {
        Workout workout = sqliteWorkoutDAO.load(new WorkoutId("21"));
        assertThat(workout, nullValue());
    }

    @Test
    public void loadWorkoutId_shouldReturnWorkoutWithWorkoutId() {
        Workout workout = new BasicWorkout(new LinkedList<Exercise>());
        sqliteWorkoutDAO.save(workout);
        WorkoutId workoutId = workout.getWorkoutId();

        Workout loadedWorkout = sqliteWorkoutDAO.load(workoutId);
        assertThat(loadedWorkout.getWorkoutId(), equalTo(workoutId));
    }

    @Test
    public void saveWorkout_shouldUpdateName() {
        Workout workout = new BasicWorkout(new LinkedList<Exercise>());
        sqliteWorkoutDAO.save(workout);
        WorkoutId workoutId = workout.getWorkoutId();

        Workout loadedWorkout = sqliteWorkoutDAO.load(workoutId);
        loadedWorkout.setName("new name");
        sqliteWorkoutDAO.save(loadedWorkout);
        Workout reloadedWorkout = sqliteWorkoutDAO.load(workoutId);
        assertThat(reloadedWorkout.getName(), equalTo("new name"));
    }
}