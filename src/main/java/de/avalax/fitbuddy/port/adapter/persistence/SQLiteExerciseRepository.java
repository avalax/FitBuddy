package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.avalax.fitbuddy.domain.model.exercise.*;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import java.util.ArrayList;
import java.util.List;

public class SQLiteExerciseRepository implements ExerciseRepository {
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SetRepository setRepository;

    public SQLiteExerciseRepository(SQLiteOpenHelper sqLiteOpenHelper, SetRepository setRepository) {
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
        if (exercise.getExerciseId() == null) {
            long id = database.insertOrThrow("exercise", null, getContentValues(workoutId, position, exercise));
            exercise.setExerciseId(new ExerciseId(String.valueOf(id)));
        } else {
            database.update("exercise", getContentValues(workoutId, position, exercise), "id=?", new String[]{exercise.getExerciseId().id()});
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
