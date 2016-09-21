package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.set.BasicSet;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetId;
import de.avalax.fitbuddy.domain.model.set.SetRepository;

import java.util.ArrayList;
import java.util.List;

public class SQLiteSetRepository implements SetRepository {
    private SQLiteOpenHelper sqLiteOpenHelper;

    public SQLiteSetRepository(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    @Override
    public void save(ExerciseId exerciseId, Set set) {
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        if (set.getSetId() == null) {
            long id = database.insertOrThrow("sets", null, getContentValues(exerciseId, set));
            set.setSetId(new SetId(String.valueOf(id)));
        } else {
            String[] args = {set.getSetId().id()};
            database.update("sets", getContentValues(exerciseId, set), "id=?", args);
        }
        database.close();
    }

    @Override
    public void delete(SetId id) {
        if (id == null) {
            return;
        }
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        database.delete("sets", "id=?", new String[]{id.id()});
        database.close();
    }

    @Override
    public List<Set> allSetsBelongsTo(ExerciseId exerciseId) {
        List<Set> sets = new ArrayList<>();
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("sets", new String[]{"id", "weight", "reps"},
                "exercise_id=?", new String[]{exerciseId.id()}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                SetId setId = new SetId(cursor.getString(0));
                double weight = cursor.getDouble(1);
                int maxReps = cursor.getInt(2);
                Set set = new BasicSet(setId, weight, maxReps);
                sets.add(set);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return sets;
    }

    private ContentValues getContentValues(ExerciseId exerciseId, Set set) {
        ContentValues values = new ContentValues();
        values.put("exercise_id", exerciseId.id());
        values.put("weight", set.getWeight());
        values.put("reps", set.getMaxReps());
        return values;
    }
}
