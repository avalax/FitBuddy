package de.avalax.fitbuddy.datalayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Set;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.core.workout.basic.BasicExercise;
import de.avalax.fitbuddy.core.workout.basic.BasicSet;
import de.avalax.fitbuddy.core.workout.basic.BasicWorkout;
import de.avalax.fitbuddy.datalayer.sqlite.WorkoutSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
            for (Exercise exercise : workout.getExercises()) {
                saveExercise(workout, exercise);
            }
        } else {
            database.update("workout", getContentValues(workout), "id=?", new String[]{String.valueOf(workout.getId())});
        }
        database.close();
    }

    @Override
    public void saveExercise(Workout workout, Exercise exercise) {
        SQLiteDatabase database = workoutSQLiteOpenHelper.getWritableDatabase();
        if (exercise.getId() == null) {
            exercise.setId(database.insert("exercise", null, getContentValues(workout, exercise)));
            for (Set set: exercise.getSets()) {
                saveSet(exercise, set);
            }
        } else {
            database.update("exercise", getContentValues(workout, exercise), "id=?", new String[]{String.valueOf(workout.getId())});
        }
        database.close();
    }

    @Override
    public void deleteExercise(Exercise exercise) {
        if (exercise.getId() == null) {
            return;
        }
        SQLiteDatabase database = workoutSQLiteOpenHelper.getWritableDatabase();
        int deleteCount = database.delete("exercise", "id=?", new String[] {String.valueOf(exercise.getId())});
        Log.d("delete exercise with id" + exercise.getId(), String.valueOf(deleteCount));
        database.close();
    }

    @Override
    public void saveSet(Exercise exercise, Set set) {
        SQLiteDatabase database = workoutSQLiteOpenHelper.getWritableDatabase();
        if (set.getId() == null) {
            set.setId(database.insert("sets", null, getContentValues(exercise, set)));
        } else {
            database.update("sets", getContentValues(exercise, set), "id=?", new String[]{String.valueOf(set.getId())});
        }
        database.close();
    }

    @Override
    public void deleteSet(Set set) {
        if (set.getId() == null) {
            return;
        }
        SQLiteDatabase database = workoutSQLiteOpenHelper.getWritableDatabase();
        int deleteCount = database.delete("sets", "id=?", new String[] {String.valueOf(set.getId())});
        Log.d("delete set with id" + set.getId(), String.valueOf(deleteCount));
        database.close();
    }

    private ContentValues getContentValues(Exercise exercise, Set set) {
        ContentValues values = new ContentValues();
        values.put("exercise_id", exercise.getId());
        values.put("weight", set.getWeight());
        values.put("reps", set.getMaxReps());
        return values;
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
            workout = createWorkout(database, cursor);
        }
        database.close();
        return workout;
    }

    private Workout createWorkout(SQLiteDatabase database, Cursor cursor) {
        Workout workout;
        LinkedList<Exercise> exercises = new LinkedList<>();
        workout = new BasicWorkout(exercises);
        workout.setId(cursor.getLong(0));
        workout.setName(cursor.getString(1));
        addExercises(database, workout.getId(), exercises);
        return workout;
    }

    private void addExercises(SQLiteDatabase database, long workoutId, LinkedList<Exercise> exercises) {
        Cursor cursor = database.query("exercise", new String[]{"id", "name"},
                "workout_id=?", new String[]{String.valueOf(workoutId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                List<Set> sets = new ArrayList<>();
                Exercise exercise = new BasicExercise(cursor.getString(1), sets, 0.0);
                exercise.setId(cursor.getLong(0));
                addSets(database, exercise.getId(), sets);
                exercises.add(exercise);
            } while (cursor.moveToNext());
        }
    }

    private void addSets(SQLiteDatabase database, long exerciseId, List<Set> sets) {
        Cursor cursor = database.query("sets", new String[]{"id", "weight", "reps"},
                "exercise_id=?", new String[]{String.valueOf(exerciseId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Set set = new BasicSet(cursor.getDouble(1), cursor.getInt(2));
                set.setId(cursor.getLong(0));
                sets.add(set);
            } while (cursor.moveToNext());
        }
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
            workout = createWorkout(database, cursor);
        }
        database.close();
        return workout;
    }
}
