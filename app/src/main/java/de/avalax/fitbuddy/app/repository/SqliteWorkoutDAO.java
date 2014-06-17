package de.avalax.fitbuddy.app.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.avalax.fitbuddy.core.workout.*;
import de.avalax.fitbuddy.core.workout.basic.BasicExercise;
import de.avalax.fitbuddy.core.workout.basic.BasicSet;
import de.avalax.fitbuddy.core.workout.basic.BasicWorkout;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        if (workout.getWorkoutId() == null) {
            long id = database.insert("workout", null, getContentValues(workout));
            workout.setWorkoutId(new WorkoutId(String.valueOf(id)));
        } else {
            database.update("workout", getContentValues(workout), "id=?", new String[]{workout.getWorkoutId().id()});
        }
        database.close();
        for (Exercise exercise : workout.getExercises()) {
            saveExercise(workout.getWorkoutId(), exercise);
        }
    }

    @Override
    public void saveExercise(WorkoutId workoutId, Exercise exercise) {
        SQLiteDatabase database = workoutSQLiteOpenHelper.getWritableDatabase();
        if (exercise.getExerciseId() == null) {
            long id = database.insert("exercise", null, getContentValues(workoutId, exercise));
            exercise.setExerciseId(new ExerciseId(String.valueOf(id)));
        } else {
            database.update("exercise", getContentValues(workoutId, exercise), "id=?", new String[]{workoutId.id()});
        }
        database.close();
        for (Set set : exercise.getSets()) {
            saveSet(exercise.getExerciseId(), set);
        }
    }

    @Override
    public void deleteExercise(ExerciseId exerciseId) {
        if (exerciseId == null) {
            return;
        }
        SQLiteDatabase database = workoutSQLiteOpenHelper.getWritableDatabase();
        int deleteCount = database.delete("exercise", "id=?", new String[]{exerciseId.id()});
        Log.d("delete exercise with id" + exerciseId, String.valueOf(deleteCount));
        database.close();
    }

    @Override
    public void saveSet(ExerciseId exerciseId, Set set) {
        SQLiteDatabase database = workoutSQLiteOpenHelper.getWritableDatabase();
        if (set.getSetId() == null) {
            long id = database.insert("sets", null, getContentValues(exerciseId, set));
            set.setId(new SetId(String.valueOf(id)));
        } else {
            database.update("sets", getContentValues(exerciseId, set), "id=?", new String[]{set.getSetId().id()});
        }
        database.close();
    }

    @Override
    public void deleteSet(SetId id) {
        if (id == null) {
            return;
        }
        SQLiteDatabase database = workoutSQLiteOpenHelper.getWritableDatabase();
        int deleteCount = database.delete("sets", "id=?", new String[]{id.toString()});
        Log.d("delete set with id" + id, String.valueOf(deleteCount));
        database.close();
    }

    private ContentValues getContentValues(ExerciseId exerciseId, Set set) {
        ContentValues values = new ContentValues();
        values.put("exercise_id", exerciseId.id());
        values.put("weight", set.getWeight());
        values.put("reps", set.getMaxReps());
        return values;
    }

    private ContentValues getContentValues(WorkoutId workoutId, Exercise exercise) {
        ContentValues values = new ContentValues();
        values.put("workout_id", workoutId.id());
        values.put("name", exercise.getName());
        return values;
    }

    private ContentValues getContentValues(Workout workout) {
        ContentValues values = new ContentValues();
        values.put("name", workout.getName() != null ? workout.getName() : "");
        return values;
    }

    @Override
    public Workout load(WorkoutId workoutId) {
        Workout workout = null;
        SQLiteDatabase database = workoutSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("workout", new String[]{"id", "name"},
                "id=?", new String[]{workoutId.id()}, null, null, null);
        if (cursor.getCount() == 1 && cursor.moveToFirst()) {
            workout = createWorkout(database, cursor);
        }
        cursor.close();
        database.close();
        return workout;
    }

    private Workout createWorkout(SQLiteDatabase database, Cursor cursor) {
        Workout workout;
        LinkedList<Exercise> exercises = new LinkedList<>();
        workout = new BasicWorkout(exercises);
        workout.setWorkoutId(new WorkoutId(cursor.getString(0)));
        workout.setName(cursor.getString(1));
        addExercises(database, workout.getWorkoutId(), exercises);
        return workout;
    }

    private void addExercises(SQLiteDatabase database, WorkoutId workoutId, LinkedList<Exercise> exercises) {
        Cursor cursor = database.query("exercise", new String[]{"id", "name"},
                "workout_id=?", new String[]{String.valueOf(workoutId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                List<Set> sets = new ArrayList<>();
                Exercise exercise = new BasicExercise(cursor.getString(1), sets, 0.0);
                exercise.setExerciseId(new ExerciseId(cursor.getString(0)));
                addSets(database, exercise.getExerciseId(), sets);
                exercises.add(exercise);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
    }

    private void addSets(SQLiteDatabase database, ExerciseId exerciseId, List<Set> sets) {
        Cursor cursor = database.query("sets", new String[]{"id", "weight", "reps"},
                "exercise_id=?", new String[]{exerciseId.id()}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Set set = new BasicSet(cursor.getDouble(1), cursor.getInt(2));
                set.setId(new SetId(cursor.getString(0)));
                sets.add(set);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public List<Workout> getList() {
        List<Workout> workoutList = new ArrayList<>();
        SQLiteDatabase database = workoutSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("workout", new String[]{"id", "name"},
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                BasicWorkout workout = new BasicWorkout(new LinkedList<Exercise>());
                workout.setWorkoutId(new WorkoutId(cursor.getString(0)));
                workout.setName(cursor.getString(1));
                workoutList.add(workout);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return workoutList;
    }

    @Override
    public void delete(WorkoutId id) {
        if (id == null) {
            return;
        }
        SQLiteDatabase database = workoutSQLiteOpenHelper.getWritableDatabase();
        int deleteCount = database.delete("workout", "id=" + id, null);
        Log.d("delete workout with id " + id, String.valueOf(deleteCount));
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
        cursor.close();
        database.close();
        return workout;
    }

    @Override
    public void saveExercise(WorkoutId id, Exercise exercise, int position) {
        //TODO: save Exercise @position
        saveExercise(id, exercise);
    }
}
