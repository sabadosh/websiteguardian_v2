package com.androidbrew.WebSiteGuardian.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    final String LOG_TAG = "WebSiteGuardianDAO";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "websiteguardian";

    // Contacts table name
    private static final String TABLE_RESPONSES = "responses";

    // Contacts Table Columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_RESPONSE = "response_code";
    public static final String KEY_TIMESTAMP = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(LOG_TAG, "inside constructor");
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "inside oncreate");
        String CREATE_RESPONSES_TABLE = "CREATE TABLE " + TABLE_RESPONSES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_RESPONSE + " TEXT,"
                + KEY_TIMESTAMP + " TEXT" + ")";
        db.execSQL(CREATE_RESPONSES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "inside onupgrade");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPONSES);
        // Create tables again
        onCreate(db);
    }

    public void addHttpClient(HttpClient httpClient) {
        Log.d(LOG_TAG, "inside adding data");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RESPONSE, httpClient.getResponse());
        values.put(KEY_TIMESTAMP, httpClient.getTimestamp());
        Log.d(LOG_TAG, "Saving:" + values.get(KEY_RESPONSE) + " " + values.get(KEY_TIMESTAMP));
        db.insert(TABLE_RESPONSES, null, values);
        Log.d(LOG_TAG, "closing connection");
        db.close();
    }

    public Cursor getAllHttpClients() {
        Log.d(LOG_TAG, "trying to get all httpclients");
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_RESPONSES, null, null, null, null, null, KEY_ID);
    }

    public Cursor getAllHttpClients(String limit) {
        Log.d(LOG_TAG, "executing query to get httpclients limited by: " + limit);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_RESPONSES, null, null, null, null, null, KEY_ID + " DESC", limit);
    }

    public Cursor getFailedHttpClients(String limit) {
        Log.d(LOG_TAG, "executing query to get all failed httpclients limited by: " + limit);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_RESPONSES, null, KEY_RESPONSE + " <> ?", new String[] {"200"}, null, null, KEY_ID + " DESC", limit);
    }

    public Long getFailedRequestsCount() {
        Log.d(LOG_TAG, "executing query to count failed requests");
        SQLiteDatabase db = this.getWritableDatabase();
        long i = DatabaseUtils.queryNumEntries(db, TABLE_RESPONSES, KEY_RESPONSE + "<>?", new String[]{"200"});
        return i;
    }

    public Long getPassedRequestsCount() {
        Log.d(LOG_TAG, "executing query to count passed requests");
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_RESPONSES, KEY_RESPONSE + "=?", new String[]{"200"});
    }
}
