package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.finished_exercise.BasicFinishedExercise;
import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExercise;
import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExerciseId;
import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExerciseRepository;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;
import de.avalax.fitbuddy.domain.model.set.Set;

public class SQLiteFinishedExerciseRepository implements FinishedExerciseRepository {
    private static final String TABLE_FINISHED_EXERCISE = "finished_exercise";
    private SQLiteOpenHelper sqLiteOpenHelper;

    public SQLiteFinishedExerciseRepository(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }


    @Override
    public void save(FinishedWorkoutId finishedWorkoutId, Exercise exercise) {
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        for (Set set : exercise.setsOfExercise()) {
            ContentValues contentValues = getContentValues(finishedWorkoutId, exercise, set);
            database.insertOrThrow(TABLE_FINISHED_EXERCISE, null, contentValues);
        }
        database.close();
    }

    private ContentValues getContentValues(
            FinishedWorkoutId finishedWorkoutId,
            Exercise exercise,
            Set set) {
        ContentValues values = new ContentValues();
        values.put("finished_workout_id", finishedWorkoutId.getId());
        values.put("name", exercise.getName());
        values.put("weight", set.getWeight());
        values.put("reps", set.getReps());
        values.put("maxReps", set.getMaxReps());
        return values;
    }

    @Override
    public List<FinishedExercise> allSetsBelongsTo(FinishedWorkoutId finishedWorkoutId) {
        List<FinishedExercise> finishedExercises = new ArrayList<>();
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        String[] columns = {"id", "name", "weight", "reps", "maxReps"};
        String[] args = {finishedWorkoutId.getId()};
        Cursor cursor = database.query("finished_exercise", columns,
                "finished_workout_id=?", args, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                FinishedExerciseId finishedExerciseId = new FinishedExerciseId(cursor.getString(0));
                String name = cursor.getString(1);
                double weight = cursor.getDouble(2);
                int reps = cursor.getInt(3);
                int maxReps = cursor.getInt(4);
                FinishedExercise finishedExercise = new BasicFinishedExercise(
                        finishedExerciseId,
                        finishedWorkoutId,
                        name,
                        weight,
                        reps,
                        maxReps);
                finishedExercises.add(finishedExercise);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return finishedExercises;
    }
}
