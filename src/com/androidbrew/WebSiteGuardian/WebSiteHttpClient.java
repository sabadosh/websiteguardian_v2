package com.androidbrew.WebSiteGuardian;


import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.sql.Timestamp;

public class WebSiteHttpClient {

    final String LOG_TAG = "WebSiteHttpClient";

    private String currentStatus;
    private Timestamp currentStatusTimeStamp;

    public void setCurrentStatusTimeStamp(Timestamp currentStatusTimeStamp) {
        this.currentStatusTimeStamp = currentStatusTimeStamp;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getCurrentStatus() {
        return this.currentStatus;
    }

    public Timestamp getCurrentStatusTimeStamp() {
        return this.currentStatusTimeStamp;
    }

    public void pingWebSite(String url) {
        Log.d(LOG_TAG, "trying to ping website " + url + "......");
        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
        HttpClient httpClient =  new DefaultHttpClient(httpParams);
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);
        } catch (ClientProtocolException e) {
//            e.printStackTrace();
            Log.d(LOG_TAG, "Got HTTP exception: " + e.toString());
        } catch (IOException e) {
            Log.d(LOG_TAG, "Got HTTP exception: " + e.toString());
        }
        int statusCode = 500;
        if (response != null) {
            statusCode = response.getStatusLine().getStatusCode();
        }
        Log.d(LOG_TAG, "got response code " + statusCode);
        this.setCurrentStatus(Integer.toString(statusCode));
        java.util.Date date = new java.util.Date();
        this.setCurrentStatusTimeStamp(new Timestamp(date.getTime()));
    }

}
