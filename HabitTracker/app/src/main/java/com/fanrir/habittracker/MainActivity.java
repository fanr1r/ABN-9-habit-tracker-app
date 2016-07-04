package com.fanrir.habittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fanrir.habittracker.HabitContract.HabitEntry;

public class MainActivity extends AppCompatActivity {
    private HabitDbHelper mDbHelper = new HabitDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        long id = insertHabit("walk the dog", 2);
        Log.v("MainActivity", Long.toString(id));

        id = insertHabit("play games", 5);
        Log.v("MainActivity", Long.toString(id));

        Cursor c = readHabit();

        updateHabit(1, "go home");

        deleteHabit();
        Log.v("MainActivity", "deleted all entries");

    }

    private void updateHabit(long rowId, String updatedTitle) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(HabitEntry._ID, rowId);
        updatedValues.put(HabitEntry.COLUMN_NAME, updatedTitle);

        getContentResolver().update(
                HabitEntry.CONTENT_URI, updatedValues, HabitEntry._ID + "= ?",
                new String[] { Long.toString(rowId)});

    }

    private void deleteHabit() {
        getContentResolver().delete(
                HabitEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = getContentResolver().query(
                HabitEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        cursor.close();
    }

    private Cursor readHabit() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.query(
                HabitEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );
        return cursor;
    }

    private long insertHabit(String name, int level) {
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)
        ContentValues testValues = createHabitValues(name, level);

        // Third Step: Insert ContentValues into database and get a row ID back
        long locationRowId;
        locationRowId = db.insert(HabitEntry.TABLE_NAME, null, testValues);

        // Data's inserted.
        db.close();
        return locationRowId;
    }

    private ContentValues createHabitValues(String name, int level) {
        ContentValues habitValues = new ContentValues();

        habitValues.put(HabitEntry.COLUMN_NAME, name);
        habitValues.put(HabitEntry.COLUMN_LEVEL, level);

        return habitValues;
    }
}
