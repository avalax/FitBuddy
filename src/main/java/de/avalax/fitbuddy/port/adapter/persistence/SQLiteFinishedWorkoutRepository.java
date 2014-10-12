package de.avalax.fitbuddy.port.adapter.persistence;

import android.database.sqlite.SQLiteOpenHelper;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.workout.Workout;

public class SQLiteFinishedWorkoutRepository implements FinishedWorkoutRepository {
    public SQLiteFinishedWorkoutRepository(SQLiteOpenHelper sqLiteOpenHelper) {
        //TODO: init
    }

    @Override
    public void save(Workout workout) {
        //TODO: persist workout with exercises and sets
    }
}
