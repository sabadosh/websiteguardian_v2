package com.androidbrew.WebSiteGuardian;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.androidbrew.WebSiteGuardian.DAO.DatabaseHelper;

public class AllResponsesActivity extends Activity {

    final String LOG_TAG = "WebSiteGuardian";
    public static final String BROADCAST_ACTION_ALL_RESPONSES = "com.androidbrew.WebSiteGuardian.WebSiteGuardianService";
    private DatabaseHelper db;
    BroadcastReceiver br;
    ListView listView;
    Boolean isActive;

    public final String LIST_VIEW_VALUES_LIMIT = "10";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_responses);
        db = new DatabaseHelper(this);
        br = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (isActive) {
                    Log.d(LOG_TAG, "got updated message from service");
                    displayListView();
                }
            }
        };
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION_ALL_RESPONSES);
        registerReceiver(br, intFilt);
        listView = (ListView) findViewById(R.id.lvAllResponses);
        listView.setEmptyView(findViewById(R.layout.each_response));

    }

    protected void onResume() {
        super.onResume();
        isActive = true;
        displayListView();
    }

    protected void onPause() {
        isActive = false;
        super.onPause();
    }

    private void displayListView() {
        Cursor cursor = db.getAllHttpClients(LIST_VIEW_VALUES_LIMIT);
        String[] columns = new String[] {
                DatabaseHelper.KEY_RESPONSE,
                DatabaseHelper.KEY_TIMESTAMP
        };
        int[] to = new int[] {
                R.id.tvResponseText,
                R.id.tvTimestampText,
        };
        if(cursor!=null && cursor.getCount()>0) {
            SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(
                    this, R.layout.each_response,
                    cursor,
                    columns,
                    to,
                    0);
            listView.setAdapter(dataAdapter);
        }
    }
}
