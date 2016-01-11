package com.example.ric.mydiary.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;

public class EventsDataSource {
    private SQLiteDatabase db;
    private SQLiteHelper dbHelper;
    Cursor cursor = db.rawQuery("SELECT * FROM " + dbHelper.EVENTS_TABLE_NAME + " LIMIT 1", null);
    private String[] allColumns = cursor.getColumnNames();

    public EventsDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public long addEvent(String title, String description, Category category, Timestamp dateTime, String place, String image) {
        this.db = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(this.dbHelper.EVENTS_COLUMN_TITLE, title);
        values.put(this.dbHelper.EVENTS_COLUMN_DESCRIPTION, description);
        values.put(this.dbHelper.EVENTS_COLUMN_CATEGORY, String.valueOf(category));
        values.put(this.dbHelper.EVENTS_COLUMN_DATETIME, String.valueOf(dateTime));
        values.put(this.dbHelper.EVENTS_COLUMN_PLACE, place);
        values.put(this.dbHelper.EVENTS_COLUMN_IMAGE, image);

        long newEventsId;
        newEventsId = db.insert(this.dbHelper.EVENTS_TABLE_NAME, null, values);
        db.close();
        return newEventsId;
    }

    public boolean updateEvent(Integer id, String title, String description, Category category, Timestamp dateTime, String place, String image) {
        this.db = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(this.dbHelper.EVENTS_COLUMN_TITLE, title);
        values.put(this.dbHelper.EVENTS_COLUMN_DESCRIPTION, description);
        values.put(this.dbHelper.EVENTS_COLUMN_CATEGORY, String.valueOf(category));
        values.put(this.dbHelper.EVENTS_COLUMN_DATETIME, String.valueOf(dateTime));
        values.put(this.dbHelper.EVENTS_COLUMN_PLACE, place);
        values.put(this.dbHelper.EVENTS_COLUMN_IMAGE, image);

        db.update(this.dbHelper.EVENTS_TABLE_NAME,
                values,
                this.dbHelper.EVENTS_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(id)});
        db.close();
        return true;
    }

    public Integer deleteEvent(Integer id) {
        this.db = this.dbHelper.getWritableDatabase();
        db.delete(this.dbHelper.EVENTS_TABLE_NAME,
                this.dbHelper.EVENTS_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(id)});
        db.close();
        return id;
    }

    public ArrayList<Event> getEventsByDate(Timestamp dateTime) {
        return getEvents(this.dbHelper.EVENTS_COLUMN_DATETIME + " = ?", new String[]{dateTime.toString()});
    }

    public ArrayList<Event> getEventsByCategory(Category category) {
        return getEvents(this.dbHelper.EVENTS_COLUMN_CATEGORY + " = ?", new String[]{category.toString()});
    }


    public ArrayList<Event> getEvents(String selection, String[] selectionArgs) {
        ArrayList<Event> events = new ArrayList<Event>();

        this.db = this.dbHelper.getReadableDatabase();
        Cursor cursor = db.query(this.dbHelper.EVENTS_TABLE_NAME, allColumns, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Event event = cursorToEvent(this.cursor);
            events.add(event);
            cursor.moveToNext();
        }
        cursor.close();
        return events;
    }

    private Event cursorToEvent(Cursor cursor) {
        Event event = new Event();
        event.setId(cursor.getLong(0));
        event.setTitle(cursor.getString(1));
        event.setDescription(cursor.getString(2));
        event.setCategory(cursor.getString(3));
        //TODO dont know if this works
        event.setDateTime(new Timestamp(cursor.getLong(4)));
        event.setPlace(cursor.getString(5));
        event.setImage(cursor.getString(6));
        return event;
    }
}
