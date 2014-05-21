package de.avalax.fitbuddy.datalayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.core.workout.basic.BasicWorkout;
import de.avalax.fitbuddy.datalayer.sqlite.WorkoutSQLiteOpenHelper;

import java.util.LinkedList;
import java.util.TreeMap;

public class SqliteWorkoutDAO implements WorkoutDAO {
    private static final String WORKOUT_DB = "workout";
    private static final int WORKOUT_DB_VERSION = 1;
    private WorkoutSQLiteOpenHelper workoutSQLiteOpenHelper;

    public SqliteWorkoutDAO(Context context, int createRessourceId) {
        this.workoutSQLiteOpenHelper = new WorkoutSQLiteOpenHelper(WORKOUT_DB, WORKOUT_DB_VERSION, context, createRessourceId);
    }

    @Override
    public void save(Workout workout) {
        SQLiteDatabase database = workoutSQLiteOpenHelper.getWritableDatabase();
        if (workout.getId() == null) {
            workout.setId(database.insert("workout", null, getContentValues(workout)));
            saveExercises(database, workout);
        } else {
            database.update("workout", getContentValues(workout), "id=?", new String[]{String.valueOf(workout.getId())});
        }
        database.close();
    }

    private void saveExercises(SQLiteDatabase database, Workout workout) {
        for (Exercise exercise : workout.getExercises()) {
            if (exercise.getId() == null) {
                exercise.setId(database.insert("exercise", null, getContentValues(workout, exercise)));
            } else {
                database.update("exercise", getContentValues(workout, exercise), "id=?", new String[]{String.valueOf(workout.getId())});
            }
            //TODO: add sets
        }
    }

    private ContentValues getContentValues(Workout workout, Exercise exercise) {
        ContentValues values = new ContentValues();
        values.put("workout_id", workout.getId());
        values.put("name", exercise.getName());
        return values;
    }

    private ContentValues getContentValues(Workout workout) {
        ContentValues values = new ContentValues();
        values.put("name", workout.getName());
        return values;
    }

    @Override
    public Workout load(long position) {
        Workout workout = null;
        SQLiteDatabase database = workoutSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("workout", new String[]{"id", "name"},
                "id=?", new String[]{String.valueOf(position)}, null, null, null);
        if (cursor.getCount() == 1 && cursor.moveToFirst()) {
            workout = new BasicWorkout(new LinkedList<Exercise>());
            workout.setId(cursor.getLong(0));
            workout.setName(cursor.getString(1));
            //TODO: add exercises
        }
        database.close();
        return workout;
    }

    @Override
    public TreeMap<Long, String> getList() {
        TreeMap<Long, String> workoutList = new TreeMap<>();
        SQLiteDatabase database = workoutSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("workout", new String[]{"id", "name"},
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                workoutList.put(cursor.getLong(0), cursor.getString(1));
            } while (cursor.moveToNext());
        }
        database.close();
        return workoutList;
    }

    @Override
    public void delete(Workout workout) {
        if (workout.getId() == null) {
            return;
        }
        SQLiteDatabase database = workoutSQLiteOpenHelper.getWritableDatabase();
        int deleteCount = database.delete("workout", "id=" + workout.getId(), null);
        Log.d("delete workout with id" + workout.getId(), String.valueOf(deleteCount));
        database.close();
    }

    @Override
    public Workout getFirstWorkout() {
        Workout workout = null;
        SQLiteDatabase database = workoutSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("workout", new String[]{"id", "name"},
                null, null, null, null, null);
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            workout = new BasicWorkout(new LinkedList<Exercise>());
            workout.setId(cursor.getLong(0));
            workout.setName(cursor.getString(1));

        }
        database.close();
        return workout;
    }
}
