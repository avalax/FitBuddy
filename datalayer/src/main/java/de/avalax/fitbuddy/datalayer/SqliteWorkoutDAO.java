package de.avalax.fitbuddy.datalayer;

import android.content.Context;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.datalayer.sqlite.WorkoutSQLiteOpenHelper;

import java.util.List;

public class SqliteWorkoutDAO implements WorkoutDAO {
    private static final String WORKOUT_DB = "workout";
    private static final int WORKOUT_DB_VERSION = 1;
    private WorkoutSQLiteOpenHelper workoutSQLiteOpenHelper;
    @Deprecated
    private FakeWorkoutDAO fakeWorkoutDAO;

    public SqliteWorkoutDAO(Context context,int createRessourceId) {
        this.workoutSQLiteOpenHelper = new WorkoutSQLiteOpenHelper(WORKOUT_DB, WORKOUT_DB_VERSION, context, createRessourceId);
        //TODO: use it ;)
        workoutSQLiteOpenHelper.getReadableDatabase();
        fakeWorkoutDAO = new FakeWorkoutDAO();
    }

    @Override
    public void save(Workout workout) {
        //TODO: implement me
        fakeWorkoutDAO.save(workout);
    }

    @Override
    public Workout load(int position) {
        //TODO: implement me
        return fakeWorkoutDAO.load(position);
    }

    @Override
    public List<String> getList() {
        //TODO: implement me
        return fakeWorkoutDAO.getList();
    }

    @Override
    public void remove(Workout workout) {
        //TODO: implement me
        fakeWorkoutDAO.remove(workout);
    }
}
