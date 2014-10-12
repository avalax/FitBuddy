package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.Context;
import de.avalax.fitbuddy.presentation.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class SQLiteFinishedWorkoutRepositoryTest {
    private SQLiteFinishedWorkoutRepository sqLiteFinishedWorkoutRepository;

    @Before
    public void setUp() throws Exception {
        Context context = Robolectric.application.getApplicationContext();
        FitbuddySQLiteOpenHelper sqLiteOpenHelper = new FitbuddySQLiteOpenHelper("SQLiteSetRepositoryTest", 1, context, R.raw.fitbuddy_db);
        sqLiteFinishedWorkoutRepository = new SQLiteFinishedWorkoutRepository(sqLiteOpenHelper);
    }

    @Test
    public void nothing() throws Exception {
        sqLiteFinishedWorkoutRepository.save(null);
    }
}