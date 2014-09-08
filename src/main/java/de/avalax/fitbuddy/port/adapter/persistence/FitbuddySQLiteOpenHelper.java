package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FitbuddySQLiteOpenHelper extends SQLiteOpenHelper {
    private Context context;
    private int createRessourceId;

    public FitbuddySQLiteOpenHelper(String name, int version, Context context, int createRessourceId) {
        super(context, name, null, version);
        this.context = context;
        this.createRessourceId = createRessourceId;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        InputStream inputStream = context.getResources().openRawResource(createRessourceId);
        try {
            insertFromStream(inputStream, database);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }

    protected void insertFromStream(InputStream inputStream, SQLiteDatabase database) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while (bufferedReader.ready()) {
            String insertStmt = bufferedReader.readLine();
            database.execSQL(insertStmt);
        }
        bufferedReader.close();
    }
}