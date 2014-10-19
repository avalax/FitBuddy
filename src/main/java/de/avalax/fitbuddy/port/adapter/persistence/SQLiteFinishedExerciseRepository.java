package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.finishedExercise.BasicFinishedExercise;
import de.avalax.fitbuddy.domain.model.finishedExercise.FinishedExercise;
import de.avalax.fitbuddy.domain.model.finishedExercise.FinishedExerciseId;
import de.avalax.fitbuddy.domain.model.finishedExercise.FinishedExerciseRepository;
import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkoutId;
import de.avalax.fitbuddy.domain.model.set.Set;

import java.util.ArrayList;
import java.util.List;

public class SQLiteFinishedExerciseRepository implements FinishedExerciseRepository {
    private SQLiteOpenHelper sqLiteOpenHelper;

    public SQLiteFinishedExerciseRepository(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }


    @Override
    public void save(FinishedWorkoutId finishedWorkoutId, Exercise exercise) {
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        for (Set set : exercise.setsOfExercise()) {
            database.insertOrThrow("finished_exercise", null, getContentValues(finishedWorkoutId, exercise, set));
        }
        database.close();
    }

    private ContentValues getContentValues(FinishedWorkoutId finishedWorkoutId, Exercise exercise, Set set) {
        ContentValues values = new ContentValues();
        values.put("finished_workout_id", finishedWorkoutId.id());
        values.put("name", exercise.getName() != null ? exercise.getName() : "");
        values.put("weight", set.getWeight());
        values.put("reps", set.getReps());
        values.put("maxReps", set.getMaxReps());
        return values;
    }

    @Override
    public List<FinishedExercise> allSetsBelongsTo(FinishedWorkoutId finishedWorkoutId) {
        List<FinishedExercise> finishedExercises = new ArrayList<>();
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("finished_exercise", new String[]{"id", "name", "weight", "reps", "maxReps"},
                "finished_workout_id=?", new String[]{finishedWorkoutId.id()}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                FinishedExerciseId finishedExerciseId = new FinishedExerciseId(cursor.getString(0));
                String name = cursor.getString(1);
                double weight = cursor.getDouble(2);
                int reps = cursor.getInt(3);
                int maxReps = cursor.getInt(4);
                FinishedExercise finishedExercise = new BasicFinishedExercise(finishedExerciseId, finishedWorkoutId, name, weight, reps, maxReps);
                finishedExercises.add(finishedExercise);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return finishedExercises;
    }
}
