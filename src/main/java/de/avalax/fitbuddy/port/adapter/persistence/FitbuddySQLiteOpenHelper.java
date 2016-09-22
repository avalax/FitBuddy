package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.avalax.fitbuddy.port.adapter.persistence.exception.DatabaseResourceNotFoundException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FitbuddySQLiteOpenHelper extends SQLiteOpenHelper {
    private Context context;
    private int resourceId;

    public FitbuddySQLiteOpenHelper(String name, int version, Context context, int resourceId) {
        super(context, name, null, version);
        this.context = context;
        this.resourceId = resourceId;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        try {
            insertFromStream(inputStream, database);
        } catch (IOException e) {
            throw new DatabaseResourceNotFoundException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // No upgrade script at this time
    }

    protected void insertFromStream(InputStream inputStream, SQLiteDatabase database)
            throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        while (bufferedReader.ready()) {
            String insertStmt = bufferedReader.readLine();
            database.execSQL(insertStmt);
        }
        bufferedReader.close();
    }
}