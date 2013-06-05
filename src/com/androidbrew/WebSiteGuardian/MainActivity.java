package com.androidbrew.WebSiteGuardian;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends TabActivity {

    final String LOG_TAG = "WebSiteGuardian";
    public static final int TIMER_INTERVAL_SECONDS = 20;
    public static final String BROADCAST_ACTION = "com.androidbrew.WebSiteGuardian.WebSiteGuardianService";
    public static final String PARAM_RESPONSE = "responsecode";
    public static final String PARAM_TIME = "timestamp";
    public static final String WEBSITE_URL = "website_url";

    ProgressBar progressBar;
    TextView responseView;
    EditText websiteUrl;
    BroadcastReceiver br;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        responseView = (TextView) findViewById(R.id.responseView);
        websiteUrl = (EditText) findViewById(R.id.websiteUrl);

        TabHost tabHost = getTabHost();
        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tagAll");
        tabSpec.setIndicator("All Responses");
        tabSpec.setContent(new Intent(this, AllResponsesActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tagFailed");
        tabSpec.setIndicator("Failed Responses");
        tabSpec.setContent(new Intent(this, FailedResponsesActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tagChart");
        tabSpec.setIndicator("Pie Chart");
        tabSpec.setContent(new Intent(this, PieChartActivity.class));
        tabHost.addTab(tabSpec);
    }

    public void onClickStart(View v) {
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, WebSiteGuardianService.class);
        intent.putExtra(WEBSITE_URL, websiteUrl.getText().toString());
        startService(intent);
    }

    public void onClickStop(View v) {
        progressBar.setVisibility(View.INVISIBLE);
        stopService(new Intent(this, WebSiteGuardianService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
