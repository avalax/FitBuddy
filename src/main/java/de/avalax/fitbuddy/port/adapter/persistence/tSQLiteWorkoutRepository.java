package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SQLiteWorkoutRepository implements WorkoutRepository {
    private SQLiteOpenHelper sqLiteOpenHelper;

    public SQLiteWorkoutRepository(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    @Override
    public void save(Workout workout) {
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        if (workout.getWorkoutId() == null) {
            long id = database.insert("workout", null, getContentValues(workout));
            workout.setWorkoutId(new WorkoutId(String.valueOf(id)));
        } else {
            database.update("workout", getContentValues(workout), "id=?", new String[]{workout.getWorkoutId().id()});
        }
        database.close();
        for (Exercise exercise : workout.getExercises()) {
            //saveExercise(workout.getWorkoutId(), exercise);
        }
    }

    private ContentValues getContentValues(Workout workout) {
        ContentValues values = new ContentValues();
        values.put("name", workout.getName() != null ? workout.getName() : "");
        return values;
    }

    @Override
    public Workout load(WorkoutId workoutId) {
        Workout workout = null;
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
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
        //addExercises(database, workout.getWorkoutId(), exercises);
        return workout;
    }

    @Override
    public List<Workout> getList() {
        List<Workout> workoutList = new ArrayList<>();
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
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
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        int deleteCount = database.delete("workout", "id=" + id, null);
        Log.d("delete workout with id " + id, String.valueOf(deleteCount));
        database.close();
    }

    @Override
    public Workout getFirstWorkout() {
        Workout workout = null;
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("workout", new String[]{"id", "name"},
                null, null, null, null, null);
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            workout = createWorkout(database, cursor);
        }
        cursor.close();
        database.close();
        return workout;
    }
}
