package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.avalax.fitbuddy.domain.model.finishedWorkout.*;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

public class SQLiteFinishedWorkoutRepository implements FinishedWorkoutRepository {
    private SQLiteOpenHelper sqLiteOpenHelper;

    public SQLiteFinishedWorkoutRepository(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    @Override
    public FinishedWorkoutId saveWorkout(Workout workout) {
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        long id = database.insert("finished_workout", null, getContentValues(workout));
        //TODO: save exercises with set details
        return new FinishedWorkoutId(String.valueOf(id));
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
        FinishedWorkout finishedWorkout = new BasicFinishedWorkout(finishedWorkoutId, workoutId, cursor.getString(2), cursor.getString(3));
        //TODO: add finished exercises
        return finishedWorkout;
    }

    private ContentValues getContentValues(Workout workout) {
        ContentValues values = new ContentValues();
        values.put("name", workout.getName() != null ? workout.getName() : "");
        values.put("workout_id", workout.getWorkoutId().id());
        return values;
    }
}
