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
import de.avalax.fitbuddy.domain.model.finished_set.FinishedSet;
import de.avalax.fitbuddy.domain.model.finished_set.FinishedSetRepository;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;
import de.avalax.fitbuddy.domain.model.set.Set;

public class SQLiteFinishedExerciseRepository implements FinishedExerciseRepository {
    private static final String TABLE_FINISHED_EXERCISE = "finished_exercise";
    private SQLiteOpenHelper sqLiteOpenHelper;
    private FinishedSetRepository finishedSetRepository;

    public SQLiteFinishedExerciseRepository(SQLiteOpenHelper sqLiteOpenHelper,
                                            FinishedSetRepository finishedSetRepository) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
        this.finishedSetRepository = finishedSetRepository;
    }


    @Override
    public void save(FinishedWorkoutId finishedWorkoutId, Exercise exercise) {
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(finishedWorkoutId, exercise);
        long id = database.insertOrThrow(TABLE_FINISHED_EXERCISE, null, contentValues);
        FinishedExerciseId finishedExerciseId = new FinishedExerciseId(String.valueOf(id));
        for (Set set : exercise.getSets()) {
            finishedSetRepository.save(finishedExerciseId, set);
        }
        database.close();
    }

    private ContentValues getContentValues(
            FinishedWorkoutId finishedWorkoutId,
            Exercise exercise) {
        ContentValues values = new ContentValues();
        values.put("finished_workout_id", finishedWorkoutId.getId());
        values.put("name", exercise.getName());
        return values;
    }

    @Override
    public List<FinishedExercise> allExercisesBelongsTo(FinishedWorkoutId finishedWorkoutId) {
        List<FinishedExercise> finishedExercises = new ArrayList<>();
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        String[] columns = {"id", "name"};
        String[] args = {finishedWorkoutId.getId()};
        Cursor cursor = database.query("finished_exercise", columns,
                "finished_workout_id=?", args, null, null, null);
        FinishedExerciseId finishedExerciseId;
        String name;
        List<FinishedSet> sets;
        FinishedExercise finishedExercise;
        while (cursor.moveToNext()) {
            finishedExerciseId = new FinishedExerciseId(cursor.getString(0));
            name = cursor.getString(1);
            sets = finishedSetRepository.allSetsBelongsTo(finishedExerciseId);
            finishedExercise = new BasicFinishedExercise(
                    finishedExerciseId,
                    finishedWorkoutId,
                    name,
                    sets);
            finishedExercises.add(finishedExercise);
        }
        cursor.close();
        database.close();
        return finishedExercises;
    }
}
