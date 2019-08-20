package com.google.developer.bugmaster.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Singleton that controls access to the SQLiteDatabase instance
 * for this application.
 */
public class DatabaseManager {
    private static DatabaseManager sInstance;
    private SQLiteDatabase mDb;

    public static synchronized DatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseManager(context.getApplicationContext());
        }
        return sInstance;
    }

    private BugsDbHelper mBugsDbHelper;

    public DatabaseManager(Context context) {
        mBugsDbHelper = new BugsDbHelper(context);
    }

    /**
     * Return a {@link Cursor} that contains every insect in the database.
     *
     * @param sortOrder Optional sort order string for the query, can be null
     * @return {@link Cursor} containing all insect results.
     */
    public Cursor queryAllInsects(String sortOrder) {
        //TODO: Implement the query
        mDb = mBugsDbHelper.getReadableDatabase();

        return mDb.query(
                DatabaseManager.Contract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    /**
     * Return a {@link Cursor} that contains a single insect for the given unique id.
     *
     * @param id Unique identifier for the insect record.
     * @return {@link Cursor} containing the insect result.
     */
    public Cursor queryInsectsById(int id) {
        //TODO: Implement the query
        return null;
    }

    // Database contract
    public static class Contract {
        public static final String TABLE_NAME = "bugs";

        public static final class Columns implements BaseColumns {
            public static final String FRIENDLY_NAME = "bug_name";
            public static final String SCIENTIFIC_NAME = "bug_sci_name";
            public static final String CLASSIFICATION = "bug_class";
            public static final String IMAGE_ASSET = "image_url";
            public static final String DANGER_LEVEL = "wild";
        }
    }
}
