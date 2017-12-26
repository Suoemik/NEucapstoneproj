package com.example.tortuga.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.macroyau.thingspeakandroid.ThingSpeakChannel;
import com.macroyau.thingspeakandroid.ThingSpeakLineChart;
import com.macroyau.thingspeakandroid.model.ChannelFeed;
import com.macroyau.thingspeakandroid.model.Feed;

import java.util.Calendar;
import java.util.Date;

import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends ActionBarActivity {
    JSONParser jsonParser = new JSONParser();
    private ThingSpeakChannel tsChannel;
    private ThingSpeakLineChart tsChart, tsChart2;
    private LineChartView chartView, chartView2;
    private TextView Moist, Water;
    private EditText Plant;
    private SharedPreferences saved;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private void makeTag(String tag){
        String newString = saved.getString(tag, null);
        SharedPreferences.Editor savedEdit = saved.edit();
        savedEdit.putString("tag", tag); //change this line to this
        savedEdit.commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        //final TextView rndNum = (TextView) findViewById(R.id.rndNum);

        tsChannel = new ThingSpeakChannel(92143);

        Plant = (EditText) findViewById(R.id.textView6);
        Water = (TextView) findViewById(R.id.textView2);
        Moist = (TextView) findViewById(R.id.textView3);

        tsChannel.setChannelFeedUpdateListener(new ThingSpeakChannel.ChannelFeedUpdateListener() {
            @Override
            public void onChannelFeedUpdated(long channelId, String channelName, ChannelFeed channelFeed) {

                // Notify last update time of the Channel feed through a Toast message
                //Plant.setText(channelName);
                Date lastUpdate = channelFeed.getChannel().getUpdatedAt();
                getSupportActionBar().setSubtitle("Last Updated: " + lastUpdate.toString());
            }
        });
        // Fetch the specific Channel feed
        tsChannel.loadChannelFeed();

        saved = getSharedPreferences("notes", MODE_PRIVATE);
        Plant.setText(saved.getString("tag", "Name"));


        tsChannel.setFeedUpdateListener(new ThingSpeakChannel.FeedEntryUpdateListener() {
            @Override
            public void onFeedUpdated(long channelId, long entryId, Feed feed) {
                feed.getEntryId();

                int moistureLvl = Integer.parseInt(feed.getField1());
                int waterLvl = Integer.parseInt(feed.getField2());
                if (moistureLvl < 300) {
                    Moist.setText("Dry Soil");
                }
                if (moistureLvl >= 300 && moistureLvl < 700) {
                    Moist.setText("Humid Soil");
                }
                if (moistureLvl >= 700) {
                    Moist.setText("Maximum");
                }
                if (waterLvl >= 700) {
                    Water.setText("Full Tank");
                }
                if (waterLvl < 700 && waterLvl >= 500) {
                    Water.setText("Good");
                }
                if (waterLvl < 500 && waterLvl > 480) {
                    Water.setText("Almost Empty");
                }
                if (waterLvl <= 480) {
                    Water.setText("REFILL!");
                }

            }

        });
        tsChannel.loadLastEntryInChannelFeed();

        tsChannel.getService();

        // Create a Calendar object dated 5 minutes ago
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -5);

        // Configure LineChartView
        chartView = (LineChartView) findViewById(R.id.chart);
        chartView2 = (LineChartView) findViewById(R.id.chart2);
        chartView.setZoomEnabled(false);
        chartView.setValueSelectionEnabled(true);
        chartView2.setZoomEnabled(false);
        chartView2.setValueSelectionEnabled(true);


        // Create a line chart from Field2 of ThinkSpeak Channel
        tsChart = new ThingSpeakLineChart(92143, 1);
        tsChart2 = new ThingSpeakLineChart(92143, 2);
        // Get 200 entries at maximum
        tsChart.setNumberOfEntries(5);
        tsChart2.setNumberOfEntries(5);
        // Set value axis labels on 10-unit interval
        tsChart.setValueAxisLabelInterval(50);
        tsChart2.setValueAxisLabelInterval(50);
        // Set date axis labels on 5-minute interval
        tsChart.setDateAxisLabelInterval(1);
        tsChart2.setDateAxisLabelInterval(1);
        // Show the line as a cubic spline
        tsChart.useSpline(true);
        tsChart2.useSpline(true);
        // Set the line color
        tsChart.setLineColor(Color.parseColor("#D32F2F"));
        tsChart2.setLineColor(Color.parseColor("#D32F2F"));
        // Set the axis color
        tsChart.setAxisColor(Color.parseColor("#455a64"));
        tsChart2.setAxisColor(Color.parseColor("#455a64"));
        // Set the starting date (5 minutes ago) for the default viewport of the chart
        tsChart.setChartStartDate(calendar.getTime());
        tsChart2.setChartStartDate(calendar.getTime());
        tsChart.setXAxisName("Time");
        tsChart2.setXAxisName("Time");

        // Set listener for chart data update
        tsChart.setListener(new ThingSpeakLineChart.ChartDataUpdateListener() {
            @Override
            public void onChartDataUpdated(long channelId, int fieldId, String title, LineChartData lineChartData, Viewport maxViewport, Viewport initialViewport) {
                // Set chart data to the LineChartView
                chartView.setLineChartData(lineChartData);
                // Set scrolling bounds of the chart
                chartView.setMaximumViewport(maxViewport);
                // Set the initial chart bounds
                //chartView.setMinimumHeight(700);
                chartView.setCurrentViewport(initialViewport);
            }
        });
        // Load chart data asynchronously
        tsChart.loadChartData();

        tsChart2.setListener(new ThingSpeakLineChart.ChartDataUpdateListener() {
            @Override
            public void onChartDataUpdated(long channelId, int fieldId, String title, LineChartData lineChartData, Viewport maxViewport, Viewport initialViewport) {
                // Set chart data to the LineChartView
                chartView2.setLineChartData(lineChartData);
                // Set scrolling bounds of the chart
                chartView2.setMaximumViewport(maxViewport);
                // Set the initial chart bounds
                chartView2.setCurrentViewport(initialViewport);
            }
        });
        // Load chart data asynchronously
        tsChart2.loadChartData();

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                Log.d("Swipe", "Refreshing");
                (new Handler()).postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);

                        tsChart.loadChartData();
                        tsChart2.loadChartData();
                        tsChannel.loadChannelFeed();
                    }

                }, 3000);

            }
            ;
            ;
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStop() {

        makeTag(Plant.getText().toString());

        super.onStop();
        setContentView(R.layout.activity_main);

        tsChannel = new ThingSpeakChannel(92143);

        tsChannel.setChannelFeedUpdateListener(new ThingSpeakChannel.ChannelFeedUpdateListener() {
            @Override
            public void onChannelFeedUpdated(long channelId, String channelName, ChannelFeed channelFeed) {

                // Notify last update time of the Channel feed through a Toast message
                Date lastUpdate = channelFeed.getChannel().getUpdatedAt();
                getSupportActionBar().setSubtitle("Last Updated: " + lastUpdate.toString());
            }
        });
        // Fetch the specific Channel feed
        tsChannel.loadChannelFeed();


        tsChannel.setFeedUpdateListener(new ThingSpeakChannel.FeedEntryUpdateListener() {
            @Override
            public void onFeedUpdated(long channelId, long entryId, Feed feed) {
                feed.getEntryId();
                int moistureLvl = Integer.parseInt(feed.getField1());
                int waterLvl = Integer.parseInt(feed.getField2());
                if (moistureLvl < 300) {
                    Moist.setText("Dry Soil");
                }
                if (moistureLvl >= 300 && moistureLvl < 700) {
                    Moist.setText("Humid Soil");
                }
                if (moistureLvl >= 700) {
                    Moist.setText("Maximum");
                }
                if (waterLvl >= 700) {
                    Water.setText("Full Tank");
                }
                if (waterLvl < 700 && waterLvl >= 500) {
                    Water.setText("Good");
                }
                if (waterLvl < 500 && waterLvl > 480) {
                    Water.setText("Almost Empty");
                }
                if (waterLvl <= 480) {
                    Water.setText("REFILL!");
                }
            }

        });
        tsChannel.loadLastEntryInChannelFeed();

        tsChannel.getService();

        // Create a Calendar object dated 5 minutes ago
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -5);

        // Configure LineChartView
        chartView = (LineChartView) findViewById(R.id.chart);
        chartView2 = (LineChartView) findViewById(R.id.chart2);
        chartView.setZoomEnabled(false);
        chartView.setValueSelectionEnabled(true);
        chartView2.setZoomEnabled(false);
        chartView2.setValueSelectionEnabled(true);


        // Create a line chart from Field2 of ThinkSpeak Channel
        tsChart = new ThingSpeakLineChart(92143, 1);
        tsChart2 = new ThingSpeakLineChart(92143, 2);
        // Get 200 entries at maximum
        tsChart.setNumberOfEntries(5);
        tsChart2.setNumberOfEntries(5);
        // Set value axis labels on 10-unit interval
        tsChart.setValueAxisLabelInterval(50);
        tsChart2.setValueAxisLabelInterval(50);
        // Set date axis labels on 5-minute interval
        tsChart.setDateAxisLabelInterval(1);
        tsChart2.setDateAxisLabelInterval(1);
        // Show the line as a cubic spline
        tsChart.useSpline(true);
        tsChart2.useSpline(true);
        // Set the line color
        tsChart.setLineColor(Color.parseColor("#D32F2F"));
        tsChart2.setLineColor(Color.parseColor("#D32F2F"));
        // Set the axis color
        tsChart.setAxisColor(Color.parseColor("#455a64"));
        tsChart2.setAxisColor(Color.parseColor("#455a64"));
        // Set the starting date (5 minutes ago) for the default viewport of the chart
        tsChart.setChartStartDate(calendar.getTime());
        tsChart2.setChartStartDate(calendar.getTime());
        tsChart.setXAxisName("Time");
        tsChart2.setXAxisName("Time");

        // Set listener for chart data update
        tsChart.setListener(new ThingSpeakLineChart.ChartDataUpdateListener() {
            @Override
            public void onChartDataUpdated(long channelId, int fieldId, String title, LineChartData lineChartData, Viewport maxViewport, Viewport initialViewport) {
                // Set chart data to the LineChartView
                chartView.setLineChartData(lineChartData);
                // Set scrolling bounds of the chart
                chartView.setMaximumViewport(maxViewport);
                // Set the initial chart bounds
                chartView.setCurrentViewport(initialViewport);
            }
        });
        // Load chart data asynchronously
        tsChart.loadChartData();

        tsChart2.setListener(new ThingSpeakLineChart.ChartDataUpdateListener() {
            @Override
            public void onChartDataUpdated(long channelId, int fieldId, String title, LineChartData lineChartData, Viewport maxViewport, Viewport initialViewport) {
                // Set chart data to the LineChartView
                chartView2.setLineChartData(lineChartData);
                // Set scrolling bounds of the chart
                chartView2.setMaximumViewport(maxViewport);
                // Set the initial chart bounds
                chartView2.setCurrentViewport(initialViewport);
            }
        });
        // Load chart data asynchronously
        tsChart2.loadChartData();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onResume(){
        Plant = (EditText) findViewById(R.id.textView6);
        Water = (TextView) findViewById(R.id.textView2);
        Moist = (TextView) findViewById(R.id.textView3);

        saved = getSharedPreferences("notes", MODE_PRIVATE);
        Plant.setText(saved.getString("tag", "Name"));

        super.onResume();
    }

    @Override
    public void onBackPressed(){

        makeTag(Plant.getText().toString());
        super.onBackPressed();
    }

    public void showNotification() {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        //Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("WASP POT")
                .setContentText("Refill your water tank")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pi)
                .setAutoCancel(true).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification);

    }
}
