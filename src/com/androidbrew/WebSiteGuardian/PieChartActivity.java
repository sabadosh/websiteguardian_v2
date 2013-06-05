package com.androidbrew.WebSiteGuardian;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import com.androidbrew.WebSiteGuardian.DAO.DatabaseHelper;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class PieChartActivity extends Activity {

    private DatabaseHelper db;
    final String LOG_TAG = "WebSiteGuardian";
    private static final String FAILED_SERIES_NAME = "Failed Requests";
    private static final String PASSED_SERIES_NAME = "Passed Requests";
    public static final String BROADCAST_ACTION_ALL_RESPONSES = "com.androidbrew.WebSiteGuardian.WebSiteGuardianService";

    private long failedResponsesCount;
    private long passedResponsesCount;

    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;

    BroadcastReceiver br;
    Boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);
        db = new DatabaseHelper(this);

        mRenderer.setStartAngle(180);
        mRenderer.setDisplayValues(true);
        mRenderer.setLabelsColor(Color.WHITE);
        mRenderer.setLabelsTextSize(15);
        mRenderer.setShowLegend(false);

        mSeries.add(FAILED_SERIES_NAME, failedResponsesCount);
        SimpleSeriesRenderer rendererFailed = new SimpleSeriesRenderer();
        rendererFailed.setColor(Color.RED);
        mRenderer.addSeriesRenderer(rendererFailed);

        mSeries.add(PASSED_SERIES_NAME, passedResponsesCount);
        SimpleSeriesRenderer rendererOK = new SimpleSeriesRenderer();
        rendererOK.setColor(Color.GREEN);
        mRenderer.addSeriesRenderer(rendererOK);

        br = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (isActive) {
                    Log.d(LOG_TAG, "got updated message from service");
                    updateValues();
                    mChartView.repaint();
                }
            }
        };
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION_ALL_RESPONSES);
        registerReceiver(br, intFilt);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        updateValues();
        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
            mRenderer.setClickEnabled(true);
            layout.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        } else {
            mChartView.repaint();
        }
    }

    @Override
    protected void onPause() {
        isActive = false;
        super.onPause();
    }

    private void updateValues() {
        failedResponsesCount = db.getFailedRequestsCount();
        passedResponsesCount = db.getPassedRequestsCount();
        mSeries.clear();
        mSeries.add(FAILED_SERIES_NAME, failedResponsesCount);
        mSeries.add(PASSED_SERIES_NAME, passedResponsesCount);
    }
}
