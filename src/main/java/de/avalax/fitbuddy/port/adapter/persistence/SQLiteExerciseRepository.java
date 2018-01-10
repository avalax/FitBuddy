package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

public class SQLiteExerciseRepository implements ExerciseRepository {
    private static final String TABLE_EXERCISE = "exercise";
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SetRepository setRepository;

    public SQLiteExerciseRepository(
            SQLiteOpenHelper sqLiteOpenHelper,
            SetRepository setRepository) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
        this.setRepository = setRepository;
    }

    @Override
    public void delete(ExerciseId exerciseId) {
        if (exerciseId == null) {
            return;
        }
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        database.delete(TABLE_EXERCISE, "id=?", new String[]{exerciseId.getId()});
        database.close();
    }

    @Override
    public List<Exercise> allExercisesBelongsTo(WorkoutId workoutId) {
        List<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query(TABLE_EXERCISE, new String[]{"id", "name"},
                "workout_id=?", new String[]{workoutId.getId()}, null, null, null);
        ExerciseId exerciseId;
        List<Set> sets;
        Exercise exercise;
        while (cursor.moveToNext()) {
            exerciseId = new ExerciseId(cursor.getString(0));
            sets = setRepository.allSetsBelongsTo(exerciseId);
            exercise = new BasicExercise(exerciseId, cursor.getString(1), sets);
            exercises.add(exercise);
        }
        cursor.close();
        database.close();
        return exercises;
    }

    @Override
    public void save(WorkoutId workoutId, Exercise exercise) {
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(workoutId, exercise);
        if (exercise.getExerciseId() == null) {
            long id = database.insertOrThrow(TABLE_EXERCISE, null, contentValues);
            ExerciseId exerciseId = new ExerciseId(String.valueOf(id));
            exercise.setExerciseId(exerciseId);
        } else {
            String[] args = {exercise.getExerciseId().getId()};
            database.update(TABLE_EXERCISE, contentValues, "id=?", args);
        }
        database.close();
        for (Set set : exercise.getSets()) {
            setRepository.save(exercise.getExerciseId(), set);
        }
    }

    private ContentValues getContentValues(WorkoutId workoutId, Exercise exercise) {
        ContentValues values = new ContentValues();
        values.put("workout_id", workoutId.getId());
        values.put("name", exercise.getName());
        return values;
    }
}
