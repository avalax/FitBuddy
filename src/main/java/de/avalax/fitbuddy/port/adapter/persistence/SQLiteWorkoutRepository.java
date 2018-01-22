package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseException;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;

import static de.avalax.fitbuddy.port.adapter.persistence.SQLiteFinishedWorkoutRepository.TABLE_FINISHED_WORKOUT;

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
        try (SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase()) {
            if (workout.getWorkoutId() == null) {
                long id = database.insertOrThrow(TABLE_WORKOUT, null, getContentValues(workout));
                workout.setWorkoutId(new WorkoutId(String.valueOf(id)));
            } else {
                String[] args = {workout.getWorkoutId().getId()};
                database.update(TABLE_WORKOUT, getContentValues(workout), "id=?", args);
            }
        }
        removeDeletedExercisesFromRepository(workout);
        saveExercisesFromWorkout(workout);
    }

    private void removeDeletedExercisesFromRepository(Workout workout) {
        List<Exercise> exercises =
                exerciseRepository.allExercisesBelongsTo(workout.getWorkoutId());
        for (Exercise exercise : exercises) {
            if (!workout.getExercises().contains(exercise)) {
                exerciseRepository.delete(exercise.getExerciseId());
            }
        }
    }

    private void saveExercisesFromWorkout(Workout workout) throws ExerciseException {
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
        String sql = "SELECT id, name, (SELECT created FROM " + TABLE_FINISHED_WORKOUT
                + " WHERE workout_id=" + TABLE_WORKOUT + ".id ORDER BY created DESC LIMIT 1) created FROM "
                + TABLE_WORKOUT + " ORDER BY created DESC";
        try (SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase()) {
            try (Cursor cursor = database.rawQuery(sql, null)) {
                while (cursor.moveToNext()) {
                    Workout workout = createWorkout(cursor);
                    workoutList.add(workout);
                }
            }
        }
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
