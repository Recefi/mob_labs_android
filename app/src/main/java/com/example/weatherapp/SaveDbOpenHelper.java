package com.example.weatherapp;

import static java.sql.Types.INTEGER;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SaveDbOpenHelper extends SQLiteOpenHelper {
    public static final String dbName = "save.db";
    private static final int dbVersion = 1;

    public SaveDbOpenHelper(Context context) {
        super(context, dbName,null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("create table save ( cur_weather INTEGER,"
//                + "cur_temp" + " REAL, " + "cur_app_temp" + " REAL, "
//                + "is_day INTEGER, curCload INTEGER, curSpeed REAL, curHum INTEGER, curSeaPres REAL, curSurf REAL);");
        db.execSQL("create table save (str_msg TEXT);");
        ContentValues contentValue = new ContentValues();
        contentValue.put("str_msg", "");
        db.insert("save", null, contentValue);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
