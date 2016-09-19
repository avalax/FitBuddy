package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.InputStream;

import de.avalax.fitbuddy.BuildConfig;
import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.port.adapter.persistence.exception.DatabaseResourceNotFoundException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = "src/main/AndroidManifest.xml", sdk = 21)
public class FitbuddySQLiteOpenHelperTest {

    private Context context;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
    }

    @Test(expected = DatabaseResourceNotFoundException.class)
    public void newInstanceWithWrongResource_shouldCreateDatabase() throws Exception {
        FitbuddySQLiteOpenHelper openHelper = new FitbuddySQLiteOpenHelper("FitbuddySQLiteOpenHelperTest", 1, context, R.raw.fitbuddy_db) {
            protected void insertFromStream(InputStream inputStream, SQLiteDatabase database) throws IOException {
                throw new IOException();
            }
        };
        openHelper.getWritableDatabase();
    }

    @Test
    public void newInstance_shouldCreateDatabaseWithVersionOne() throws Exception {
        FitbuddySQLiteOpenHelper openHelper = new FitbuddySQLiteOpenHelper("FitbuddySQLiteOpenHelperTest", 1, context, R.raw.fitbuddy_db);
        SQLiteDatabase writableDatabase = openHelper.getWritableDatabase();

        assertThat(writableDatabase.getVersion(), equalTo(1));
    }

    @Test
    public void newInstanceAfterUpgrade_shouldCreateDatabaseWithVersionTwo() throws Exception {
        FitbuddySQLiteOpenHelper openHelper = new FitbuddySQLiteOpenHelper("FitbuddySQLiteOpenHelperTest", 1, context, R.raw.fitbuddy_db);
        SQLiteDatabase writableDatabase = openHelper.getWritableDatabase();
        writableDatabase.close();
        FitbuddySQLiteOpenHelper newOpenHelper = new FitbuddySQLiteOpenHelper("FitbuddySQLiteOpenHelperTest", 2, context, R.raw.fitbuddy_db);
        SQLiteDatabase newWritableDatabase = newOpenHelper.getWritableDatabase();

        assertThat(newWritableDatabase.getVersion(), equalTo(2));
    }
}