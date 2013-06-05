package com.androidbrew.WebSiteGuardian;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.androidbrew.WebSiteGuardian.DAO.DatabaseHelper;
import com.androidbrew.WebSiteGuardian.DAO.HttpClient;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

public class WebSiteGuardianService extends Service {
    final String LOG_TAG = "WebSiteGuardianServiceLogs";
    WebSiteHttpClient webSiteHttpClient;
    DatabaseHelper db;

    Timer timer;
    ServiceTask timerTask;
    String websiteUrl;

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
        db = new DatabaseHelper(this);
        webSiteHttpClient = new WebSiteHttpClient();
        timer = new Timer();
        timerTask = new ServiceTask();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        websiteUrl = intent.getStringExtra(MainActivity.WEBSITE_URL);
        timer.schedule(timerTask, 1000 ,MainActivity.TIMER_INTERVAL_SECONDS * 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy");
        timer.cancel();
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }

    private class ServiceTask extends TimerTask {
        @Override
        public void run() {
            Log.d(LOG_TAG, "insideServiceTask");
            Intent intentAllResponses = new Intent(AllResponsesActivity.BROADCAST_ACTION_ALL_RESPONSES);
            webSiteHttpClient.pingWebSite(websiteUrl);
            Timestamp ts = webSiteHttpClient.getCurrentStatusTimeStamp();
            String time = ts.toString();
            Log.d(LOG_TAG, "GOT such responce code:" + webSiteHttpClient.getCurrentStatus());
            Log.d(LOG_TAG, "Timestamp:" + time);
            Log.d(LOG_TAG, "Trying to save");
            HttpClient _http = new HttpClient(webSiteHttpClient.getCurrentStatus(), time);
            db.addHttpClient(_http);
//            intentAllResponses.putExtra("updated", true);
            sendBroadcast(intentAllResponses);
        }
    }

}
