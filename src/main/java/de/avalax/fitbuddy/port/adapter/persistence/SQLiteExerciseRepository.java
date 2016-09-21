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
        database.delete("exercise", "id=?", new String[]{exerciseId.id()});
        database.close();
    }

    @Override
    public List<Exercise> allExercisesBelongsTo(WorkoutId workoutId) {
        List<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("exercise", new String[]{"id", "name"},
                "workout_id=?", new String[]{workoutId.id()}, null, null, "position");
        if (cursor.moveToFirst()) {
            do {
                ExerciseId exerciseId = new ExerciseId(cursor.getString(0));
                List<Set> sets = setRepository.allSetsBelongsTo(exerciseId);
                Exercise exercise = new BasicExercise(exerciseId, cursor.getString(1), sets);
                exercises.add(exercise);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return exercises;
    }

    @Override
    public void save(WorkoutId workoutId, int position, Exercise exercise) {
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(workoutId, position, exercise);
        if (exercise.getExerciseId() == null) {
            long id = database.insertOrThrow("exercise", null, contentValues);
            ExerciseId exerciseId = new ExerciseId(String.valueOf(id));
            exercise.setExerciseId(exerciseId);
        } else {
            String[] args = {exercise.getExerciseId().id()};
            database.update("exercise", contentValues, "id=?", args);
        }
        database.close();
        for (Set set : exercise.setsOfExercise()) {
            setRepository.save(exercise.getExerciseId(), set);
        }
    }

    private ContentValues getContentValues(WorkoutId workoutId, int position, Exercise exercise) {
        ContentValues values = new ContentValues();
        values.put("workout_id", workoutId.id());
        values.put("name", exercise.getName());
        values.put("position", position);
        return values;
    }
}
