package com.fanrir.habittracker;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.fanrir.habittracker.HabitContract.HabitEntry;

/**
 * Created by Eisdrachl on 04.07.2016.
 */
public class HabitProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private HabitDbHelper mOpenHelper;

    static final int HABIT = 100;
    static final int HABIT_WITH_NAME = 101;

    private static final SQLiteQueryBuilder sGoalByNameQueryBuilder;

    static{
        sGoalByNameQueryBuilder = new SQLiteQueryBuilder();

        sGoalByNameQueryBuilder.setTables(
                HabitEntry.TABLE_NAME
        );
    }

    //goal.name = ?
    private static final String sGoalNameSelection =
            HabitEntry.TABLE_NAME+
                    "." + HabitEntry.COLUMN_NAME + " = ? ";

    private Cursor getGoalByName(Uri uri, String[] projection, String sortOrder) {
        String name = HabitEntry.getNameFromUri(uri);

        String[] selectionArgs = new String[]{name};
        String selection = sGoalNameSelection;

        return sGoalByNameQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = HabitContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, HabitContract.PATH_HABIT, HABIT);
        matcher.addURI(authority, HabitContract.PATH_HABIT + "/*", HABIT_WITH_NAME);

        return matcher;
    }

    /*
        We just create a new GoalDbHelper for later use here.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new HabitDbHelper(getContext());
        return true;
    }

    //Here's where you'll code the getType function that uses the UriMatcher.
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case HABIT:
                return HabitEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "goal"
            case HABIT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        HabitEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "goal/*"
            case HABIT_WITH_NAME: {
                retCursor = getGoalByName(uri, projection, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case HABIT: {
                long _id = db.insert(HabitEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = HabitEntry.buildGoalUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case HABIT:
                rowsDeleted = db.delete(
                        HabitEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case HABIT:
                rowsUpdated = db.update(HabitEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

}
