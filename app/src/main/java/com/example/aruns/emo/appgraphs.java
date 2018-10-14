package com.example.aruns.emo;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.HashMap;

public class appgraphs extends Activity {

    String app;
    private LinearLayout.LayoutParams params;
    GraphView[] graphs = new GraphView[5];
    TextView appName;
    private LinearLayout lin;
    public DefaultLabelFormatter formatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appgraphs);
        lin = findViewById(R.id.appgraphslin);
        params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,400);
        params.leftMargin = 5;
        params.leftMargin = 5;
        params.topMargin = 5;
        params.bottomMargin = 5;

        formatter =  new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return "";
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        };
        appName = new TextView(this);
        lin.addView(appName);
        for (int i =0; i < 5; i++) {
            graphs[i] = new GraphView(this);
            graphs[i].getGridLabelRenderer().setLabelFormatter(formatter);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<> (new DataPoint[] { new DataPoint(0,0)} );
            series.setDrawDataPoints(true);
            series.setThickness(5);
            graphs[i].addSeries(series);
            graphs[i].setTitle(Information.getEnumString(Emotion.values[i]));
            lin.addView(graphs[i], params);
        }
        app = getIntent().getExtras().getString("APP_NAME");
        appName.setText(app);
        appName.setAllCaps(true);
        appName.setGravity(Gravity.CENTER_HORIZONTAL);
        appName.setTextSize(20);
        graphApp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        super.onResume();
        app = getIntent().getExtras().getString("APP_NAME");
        graphApp();
    }

    public void graphApp(){

        ArrayList<Pair> appData = Information.information.data.get(app);

        HashMap<Emotion, ArrayList<DataPoint>> allPoints = new HashMap<>();

        for ( int i =0; i<5; i++) {
            allPoints.put(Emotion.values[i], new ArrayList<DataPoint>());
        }

        int[] counts = new int[5];

        for (Pair pair : appData) {
            ArrayList<DataPoint> cur = allPoints.get(pair.value);
            cur.add(new DataPoint( pair.time, counts[pair.value.ordinal()]++ ) );
        }

        for ( int i =0; i<5; i++) {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(allPoints.get(Emotion.values[i]).toArray(new DataPoint[0]));
            series.setDrawDataPoints(true);
            series.setThickness(2);
            graphs[i].removeAllSeries();
            graphs[i].addSeries(series);

        }
    }

}
