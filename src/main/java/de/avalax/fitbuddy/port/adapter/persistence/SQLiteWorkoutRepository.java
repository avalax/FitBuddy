package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.workout.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SQLiteWorkoutRepository implements WorkoutRepository {
    private SQLiteOpenHelper sqLiteOpenHelper;
    private ExerciseRepository exerciseRepository;

    public SQLiteWorkoutRepository(SQLiteOpenHelper sqLiteOpenHelper, ExerciseRepository exerciseRepository) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
        this.exerciseRepository = exerciseRepository;
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
        List<Exercise> exercises = workout.getExercises();
        for (int i = 0; i < exercises.size(); i++) {
            exerciseRepository.save(workout.getWorkoutId(), i, exercises.get(i));
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
        if (cursor.moveToFirst()) {
            workout = createWorkout(cursor);
        }
        cursor.close();
        database.close();
        return workout;
    }

    private Workout createWorkout(Cursor cursor) {
        WorkoutId workoutId = new WorkoutId(cursor.getString(0));
        LinkedList<Exercise> exercises = exerciseRepository.allExercisesBelongsTo(workoutId);
        Workout workout = new BasicWorkout(exercises);
        workout.setWorkoutId(workoutId);
        workout.setName(cursor.getString(1));
        return workout;
    }

    @Override
    public List<WorkoutListEntry> getWorkoutList() {
        List<WorkoutListEntry> workoutList = new ArrayList<>();
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("workout", new String[]{"id", "name"},
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                WorkoutId workoutId = new WorkoutId(cursor.getString(0));
                String name = cursor.getString(1);
                WorkoutListEntry entry = new WorkoutListEntry(workoutId, name);
                workoutList.add(entry);
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
        database.delete("workout", "id=" + id.id(), null);
        database.close();
    }
}
