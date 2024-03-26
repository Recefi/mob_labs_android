package com.example.weatherapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CitiesDbOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "CitiesDbOpenHelper";
    public static final String dbName = "cities.db";
    private static final int dbVersion = 1;
    private final Context context;
    private boolean createDb = false;

    public CitiesDbOpenHelper(Context context){
        super(context, dbName, null, dbVersion);
        this.context = context;
    }

    private void copyDbFromAssets(SQLiteDatabase db) {
        Log.i(TAG, "copyDatabase");
        try(InputStream inputStream = context.getAssets().open("cities.db");
                        OutputStream outputStream = new FileOutputStream(db.getPath())) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new Error(TAG + " Error copying database");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDb = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        if (createDb) {
            createDb = false;
            copyDbFromAssets(db);
        }
    }
}
