package com.fanrir.habittracker;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Eisdrachl on 04.07.2016.
 */
public class HabitContract {
    public static final String CONTENT_AUTHORITY = "com.fanrir.habittracker.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.fanrir.habittracker.app/habit/ is a valid path for
    // looking at goals.
    public static final String PATH_HABIT = "habit";

    /* Inner class that defines the table contents of the goal table */
    public static final class HabitEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HABIT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HABIT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HABIT;

        // Table name
        public static final String TABLE_NAME = "habit";

        // The habit name string is what will define the habit
        public static final String COLUMN_NAME = "name";

        // The level shows where you stand in your habit
        public static final String COLUMN_LEVEL = "level";

        // The image will illustrate the habit
        public static final String COLUMN_IMAGE = "image";

        // The notes you make for your habit
        public static final String COLUMN_NOTES = "notes";

        public static Uri buildGoalUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildHabitUri() {
            return CONTENT_URI.buildUpon().build();
        }

        public static Uri buildHabitUri(String name) {
            return CONTENT_URI.buildUpon().appendPath(name).build();
        }

        public static String getNameFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }
}
