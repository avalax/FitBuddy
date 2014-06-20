package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetId;
import de.avalax.fitbuddy.domain.model.set.SetRepository;

public class SQLiteSetRepository implements SetRepository {
    private SQLiteOpenHelper sqLiteOpenHelper;

    public SQLiteSetRepository(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    @Override
    public void save(ExerciseId exerciseId, Set set) {
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        if (set.getSetId() == null) {
            long id = database.insert("sets", null, getContentValues(exerciseId, set));
            set.setId(new SetId(String.valueOf(id)));
        } else {
            database.update("sets", getContentValues(exerciseId, set), "id=?", new String[]{set.getSetId().id()});
        }
        database.close();
    }

    @Override
    public void delete(SetId id) {
        if (id == null) {
            return;
        }
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        int deleteCount = database.delete("sets", "id=?", new String[]{id.toString()});
        Log.d("delete set with id" + id, String.valueOf(deleteCount));
        database.close();
    }

    private ContentValues getContentValues(ExerciseId exerciseId, Set set) {
        ContentValues values = new ContentValues();
        values.put("exercise_id", exerciseId.id());
        values.put("weight", set.getWeight());
        values.put("reps", set.getMaxReps());
        return values;
    }
}
