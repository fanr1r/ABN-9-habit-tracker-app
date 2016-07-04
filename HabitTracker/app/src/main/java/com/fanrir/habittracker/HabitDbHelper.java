package com.fanrir.habittracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fanrir.habittracker.HabitContract.HabitEntry;

/**
 * Created by Eisdrachl on 04.07.2016.
 */
public class HabitDbHelper extends SQLiteOpenHelper {

    // Database Version
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    // Database Name
    static final String DATABASE_NAME = "habit_tracker.db";

    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table create statement
        // Create a table to hold habits. A habit consists of a name as string, an image,
        // a state as int ordinal for the object and the amount of time spend to reach the goal
        final String SQL_CREATE_GOAL_TABLE = "CREATE TABLE " + HabitEntry.TABLE_NAME + " (" +
                HabitEntry._ID + " INTEGER PRIMARY KEY," +
                HabitEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                HabitEntry.COLUMN_IMAGE + " BLOB, " +
                HabitEntry.COLUMN_LEVEL + " INTEGER NOT NULL, " +
                HabitEntry.COLUMN_NOTES + " TEXT " +
                " );";

        // creating table
        db.execSQL(SQL_CREATE_GOAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // updates the schema without wiping data
        db.execSQL("DROP TABLE IF EXISTS " + HabitEntry.TABLE_NAME);
        onCreate(db);
    }

    public void deleteDatabase() {
        this.deleteDatabase();
    }
}
