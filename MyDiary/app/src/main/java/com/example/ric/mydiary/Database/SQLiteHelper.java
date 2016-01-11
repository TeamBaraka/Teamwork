package com.example.ric.mydiary.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyDiary.db";

    public static final String EVENTS_TABLE_NAME = "events";
    public static final String EVENTS_COLUMN_ID = "_id";
    public static final String EVENTS_COLUMN_TITLE = "title";
    public static final String EVENTS_COLUMN_DESCRIPTION = "description";
    public static final String EVENTS_COLUMN_CATEGORY = "category";
    public static final String EVENTS_COLUMN_DATETIME = "dateTime";
    public static final String EVENTS_COLUMN_PLACE = "place";
    public static final String EVENTS_COLUMN_IMAGE = "image";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table" + EVENTS_TABLE_NAME + "(" +
                        EVENTS_COLUMN_ID + "integer primary key autoincrement," +
                        EVENTS_COLUMN_TITLE + "text," +
                        EVENTS_COLUMN_DESCRIPTION + "text," +
                        EVENTS_COLUMN_CATEGORY + "text not null," +
                        EVENTS_COLUMN_DATETIME + "datetime," +
                        EVENTS_COLUMN_PLACE + "text," +
                        EVENTS_COLUMN_IMAGE + "text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EVENTS_TABLE_NAME);
        onCreate(db);
    }
}
