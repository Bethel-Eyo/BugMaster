package com.google.developer.bugmaster.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.developer.bugmaster.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Database helper class to facilitate creating and updating
 * the database from the chosen schema.
 */
public class BugsDbHelper extends SQLiteOpenHelper {
    private static final String TAG = BugsDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "insects.db";
    private static final int DATABASE_VERSION = 5;

    //Used to read data from res/ and assets/
    private Resources mResources;

    public BugsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mResources = context.getResources();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO: Create and fill the database
        final String SQL_CREATE_BUG_TABLE = "CREATE TABLE " + DatabaseManager.Contract.TABLE_NAME
                + " (" + DatabaseManager.Contract.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DatabaseManager.Contract.Columns.FRIENDLY_NAME + " TEXT NOT NULL, "
                + DatabaseManager.Contract.Columns.SCIENTIFIC_NAME + " TEXT NOT NULL, "
                + DatabaseManager.Contract.Columns.CLASSIFICATION + " TEXT NOT NULL, "
                + DatabaseManager.Contract.Columns.IMAGE_ASSET + " TEXT NOT NULL, "
                + DatabaseManager.Contract.Columns.DANGER_LEVEL + " INTEGER NOT NULL " + "); ";
        db.execSQL(SQL_CREATE_BUG_TABLE);
        addDemoBugs(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: Handle database version upgrades
        /* drop table if the database version is changed
         * Note: table isn't always dropped for production app,
         * it is altered instead, to prevent deletion of data*/
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManager.Contract.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Streams the JSON data from insect.json, parses it, and inserts it into the
     * provided {@link SQLiteDatabase}.
     *
     * @param db Database where objects should be inserted.
     * @throws IOException
     * @throws JSONException
     */
    private void readInsectsFromResources(SQLiteDatabase db) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.insects);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        //Parse resource into key/values
        final String rawJson = builder.toString();
        //TODO: Parse JSON data and insert into the provided database instance
        JSONObject root = new JSONObject(rawJson);
        // get json array
        JSONArray jsonArray = root.getJSONArray("insects");

        List<ContentValues> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            ContentValues values = new ContentValues();
            JSONObject bugObject = jsonArray.getJSONObject(i);
            String friendlyName = bugObject.getString("friendlyName");
            String scientificName = bugObject.getString("scientificName");
            String classification = bugObject.getString("classification");
            String imageAsset = bugObject.getString("imageAsset");
            int dangerLevel = bugObject.getInt("dangerLevel");
            values.put(DatabaseManager.Contract.Columns.FRIENDLY_NAME, friendlyName);
            values.put(DatabaseManager.Contract.Columns.SCIENTIFIC_NAME, scientificName);
            values.put(DatabaseManager.Contract.Columns.CLASSIFICATION, classification);
            values.put(DatabaseManager.Contract.Columns.IMAGE_ASSET, imageAsset);
            values.put(DatabaseManager.Contract.Columns.DANGER_LEVEL, dangerLevel);
            Log.w("Bethelllll",i + " " + friendlyName);
            db.insert(DatabaseManager.Contract.TABLE_NAME, null, values);
        }

    }

    public void addDemoBugs (SQLiteDatabase db){
        try {
            db.beginTransaction();
            try{
                readInsectsFromResources(db);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } catch (IOException | JSONException e){
            Log.e(TAG, "Unable to pre-fill database", e);
        }
    }
}
