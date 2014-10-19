package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.finishedExercise.FinishedExercise;
import de.avalax.fitbuddy.domain.model.finishedExercise.FinishedExerciseRepository;
import de.avalax.fitbuddy.domain.model.finishedWorkout.*;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import java.util.List;

public class SQLiteFinishedWorkoutRepository implements FinishedWorkoutRepository {
    private SQLiteOpenHelper sqLiteOpenHelper;
    private FinishedExerciseRepository finishedExerciseRepository;

    public SQLiteFinishedWorkoutRepository(SQLiteOpenHelper sqLiteOpenHelper, FinishedExerciseRepository finishedExerciseRepository) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
        this.finishedExerciseRepository = finishedExerciseRepository;
    }

    @Override
    public FinishedWorkoutId saveWorkout(Workout workout) {
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        long id = database.insertOrThrow("finished_workout", null, getContentValues(workout));
        FinishedWorkoutId finishedWorkoutId = new FinishedWorkoutId(String.valueOf(id));
        for (Exercise exercise : workout.exercisesOfWorkout()) {
            finishedExerciseRepository.save(finishedWorkoutId, exercise);
        }
        database.close();
        return finishedWorkoutId;
    }

    @Override
    public FinishedWorkout load(FinishedWorkoutId finishedWorkoutId) throws FinishedWorkoutNotFoundException {
        if (finishedWorkoutId == null) {
            throw new FinishedWorkoutNotFoundException();
        }
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("finished_workout", new String[]{"id", "workout_id", "name", "created"},
                "id=?", new String[]{finishedWorkoutId.id()}, null, null, null);
        if (cursor.moveToFirst()) {
            FinishedWorkout finishedWorkout = createFinishedWorkout(cursor);
            cursor.close();
            database.close();
            return finishedWorkout;
        } else {
            cursor.close();
            database.close();
            throw new FinishedWorkoutNotFoundException();
        }
    }

    private FinishedWorkout createFinishedWorkout(Cursor cursor) {
        FinishedWorkoutId finishedWorkoutId = new FinishedWorkoutId(cursor.getString(0));
        WorkoutId workoutId = new WorkoutId(cursor.getString(1));
        List<FinishedExercise> finishedExercises = addFinishedExercises(finishedWorkoutId);

        return new BasicFinishedWorkout(finishedWorkoutId, workoutId, cursor.getString(2), cursor.getString(3), finishedExercises);
    }

    private List<FinishedExercise> addFinishedExercises(FinishedWorkoutId finishedWorkoutId) {
        return finishedExerciseRepository.allSetsBelongsTo(finishedWorkoutId);
    }

    private ContentValues getContentValues(Workout workout) {
        ContentValues values = new ContentValues();
        values.put("name", workout.getName() != null ? workout.getName() : "");
        values.put("workout_id", workout.getWorkoutId().id());
        return values;
    }
}
