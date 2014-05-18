package de.avalax.fitbuddy.datalayer.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WorkoutSQLiteOpenHelper extends SQLiteOpenHelper {
    private Context context;
    private int createRessourceId;

    public WorkoutSQLiteOpenHelper(String name, int version, Context context, int createRessourceId) {
        super(context, name, null, version);
        this.context = context;
        this.createRessourceId = createRessourceId;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d(getDatabaseName(),"Create new database");
        insertFromFile(database,context,createRessourceId);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.d(getDatabaseName(),"Upgrade from v"+ oldVersion + " to v" + newVersion);
    }

    public void insertFromFile(SQLiteDatabase database,Context context, int resourceId) {
        try {
            InputStream insertsStream = context.getResources().openRawResource(resourceId);
            BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));
            while (insertReader.ready()) {
                String insertStmt = insertReader.readLine();
                database.execSQL(insertStmt);
            }
            insertReader.close();
        } catch (IOException e) {
            Log.e(getDatabaseName(),e.getMessage(),e);
        }
    }
}