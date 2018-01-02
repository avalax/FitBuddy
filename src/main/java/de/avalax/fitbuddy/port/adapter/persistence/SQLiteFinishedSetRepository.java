package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExerciseId;
import de.avalax.fitbuddy.domain.model.finished_set.BasicFinishedSet;
import de.avalax.fitbuddy.domain.model.finished_set.FinishedSet;
import de.avalax.fitbuddy.domain.model.finished_set.FinishedSetRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetId;

public class SQLiteFinishedSetRepository implements FinishedSetRepository {
    private static final String TABLE_FINISHED_SET = "finished_sets";
    private SQLiteOpenHelper sqLiteOpenHelper;

    public SQLiteFinishedSetRepository(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    @Override
    public void save(FinishedExerciseId finishedExerciseId, Set set) {
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        long id = database.insertOrThrow(TABLE_FINISHED_SET, null, getContentValues(finishedExerciseId, set));
        set.setSetId(new SetId(String.valueOf(id)));

        database.close();
    }

    @Override
    public List<FinishedSet> allSetsBelongsTo(FinishedExerciseId finishedExerciseId) {
        List<FinishedSet> sets = new ArrayList<>();
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query(TABLE_FINISHED_SET, new String[]{"weight", "reps", "max_reps"},
                "finished_exercise_id=?", new String[]{finishedExerciseId.getId()}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                double weight = cursor.getDouble(0);
                int reps = cursor.getInt(1);
                int maxReps = cursor.getInt(2);
                FinishedSet set = new BasicFinishedSet(weight, reps, maxReps);
                sets.add(set);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return sets;
    }

    private ContentValues getContentValues(FinishedExerciseId exerciseId, Set set) {
        ContentValues values = new ContentValues();
        values.put("finished_exercise_id", exerciseId.getId());
        values.put("weight", set.getWeight());
        values.put("reps", set.getReps());
        values.put("max_reps", set.getMaxReps());
        return values;
    }
}
