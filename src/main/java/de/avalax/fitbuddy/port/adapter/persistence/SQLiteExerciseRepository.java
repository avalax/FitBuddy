package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.set.BasicSet;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetId;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SQLiteExerciseRepository implements ExerciseRepository {
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SetRepository setRepository;

    public SQLiteExerciseRepository(SQLiteOpenHelper sqLiteOpenHelper, SetRepository setRepository) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
        this.setRepository = setRepository;
    }

    @Override
    public void save(WorkoutId workoutId, Exercise exercise) {
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        if (exercise.getExerciseId() == null) {
            long id = database.insert("exercise", null, getContentValues(workoutId, exercise));
            exercise.setExerciseId(new ExerciseId(String.valueOf(id)));
        } else {
            database.update("exercise", getContentValues(workoutId, exercise), "id=?", new String[]{workoutId.id()});
        }
        database.close();
        for (Set set : exercise.getSets()) {
            setRepository.save(exercise.getExerciseId(), set);
        }
    }

    @Override
    public void delete(ExerciseId exerciseId) {
        if (exerciseId == null) {
            return;
        }
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        int deleteCount = database.delete("exercise", "id=?", new String[]{exerciseId.id()});
        Log.d("delete exercise with id" + exerciseId, String.valueOf(deleteCount));
        database.close();
    }

    @Override
    public LinkedList<Exercise> allExercisesBelongsTo(WorkoutId workoutId) {
        LinkedList<Exercise> exercises = new LinkedList<>();
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("exercise", new String[]{"id", "name"},
                "workout_id=?", new String[]{workoutId.id()}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                List<Set> sets = new ArrayList<>();
                Exercise exercise = new BasicExercise(cursor.getString(1), sets);
                exercise.setExerciseId(new ExerciseId(cursor.getString(0)));
                addSets(database, exercise.getExerciseId(), sets);
                exercises.add(exercise);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return exercises;
    }

    private ContentValues getContentValues(WorkoutId workoutId, Exercise exercise) {
        ContentValues values = new ContentValues();
        values.put("workout_id", workoutId.id());
        values.put("name", exercise.getName());
        return values;
    }

    private void addSets(SQLiteDatabase database, ExerciseId exerciseId, List<Set> sets) {
        Cursor cursor = database.query("sets", new String[]{"id", "weight", "reps"},
                "exercise_id=?", new String[]{exerciseId.id()}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Set set = new BasicSet(cursor.getDouble(1), cursor.getInt(2));
                set.setId(new SetId(cursor.getString(0)));
                sets.add(set);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }


    @Override
    public void save(WorkoutId id, Exercise exercise, int position) {
        //TODO: save Exercise @position
        save(id, exercise);
    }

    @Override
    public Exercise load(ExerciseId exerciseId) {
        Exercise exercise = null;
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("exercise", new String[]{"id", "name"},
                "id=?", new String[]{exerciseId.id()}, null, null, null);
        if (cursor.moveToFirst()) {
                List<Set> sets = setRepository.allSetsBelongsTo(exerciseId);
                exercise = new BasicExercise(cursor.getString(1), sets);
                exercise.setExerciseId(new ExerciseId(cursor.getString(0)));
        }
        cursor.close();
        database.close();
        return exercise;
    }
}
