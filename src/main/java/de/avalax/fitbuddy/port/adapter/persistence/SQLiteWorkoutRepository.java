package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;

public class SQLiteWorkoutRepository implements WorkoutRepository {
    private static final String TABLE_WORKOUT = "workout";
    private SQLiteOpenHelper sqLiteOpenHelper;
    private ExerciseRepository exerciseRepository;

    public SQLiteWorkoutRepository(
            SQLiteOpenHelper sqLiteOpenHelper,
            ExerciseRepository exerciseRepository) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public void save(Workout workout) throws ResourceException {
        if (workout.getExercises().size() == 0) {
            throw new WorkoutException("exercises must not be empty");
        }
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        if (workout.getWorkoutId() == null) {
            long id = database.insertOrThrow(TABLE_WORKOUT, null, getContentValues(workout));
            workout.setWorkoutId(new WorkoutId(String.valueOf(id)));
        } else {
            String[] args = {workout.getWorkoutId().getId()};
            database.update(TABLE_WORKOUT, getContentValues(workout), "id=?", args);
        }
        database.close();
        for (Exercise exercise : workout.getExercises()) {
            exerciseRepository.save(workout.getWorkoutId(), exercise);
        }
    }

    private ContentValues getContentValues(Workout workout) {
        ContentValues values = new ContentValues();
        values.put("name", workout.getName());
        return values;
    }

    @Override
    public Workout load(WorkoutId workoutId) throws WorkoutException {
        if (workoutId == null) {
            throw new WorkoutException();
        }
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query(TABLE_WORKOUT, new String[]
                        {"id", "name"},
                "id=?", new String[]{workoutId.getId()}, null, null, null);
        if (cursor.moveToFirst()) {
            Workout workout = createWorkout(cursor);
            cursor.close();
            database.close();
            return workout;
        } else {
            cursor.close();
            database.close();
            throw new WorkoutException();
        }
    }

    private Workout createWorkout(Cursor cursor) {
        WorkoutId workoutId = new WorkoutId(cursor.getString(0));
        List<Exercise> exercises = exerciseRepository.allExercisesBelongsTo(workoutId);
        return new BasicWorkout(workoutId, cursor.getString(1), exercises);
    }

    @Override
    public List<Workout> loadAll() {
        List<Workout> workoutList = new ArrayList<>();
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query(TABLE_WORKOUT, new String[]
                        {"id", "name"},
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Workout workout = createWorkout(cursor);
                workoutList.add(workout);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return workoutList;
    }

    @Override
    public void delete(WorkoutId workoutId) {
        if (workoutId == null) {
            return;
        }
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        database.delete(TABLE_WORKOUT, "id=" + workoutId.getId(), null);
        database.close();
    }
}
