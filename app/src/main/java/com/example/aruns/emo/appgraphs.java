package com.example.aruns.emo;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

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
    private LinearLayout lin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appgraphs);
        lin = findViewById(R.id.appgraphslin);
        params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,400);
        params.leftMargin = 1;
        params.leftMargin = 1;
        params.topMargin = 1;
        params.bottomMargin = 4;

        for (int i =0; i < 5; i++) {
            graphs[i] = new GraphView(this);
            lin.addView(graphs[i], params);
        }
        app = getIntent().getExtras().getString("APP_NAME");
        graphApp();
    }

    @Override
    protected void onResume() {
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
            graphs[i].addSeries(series);

        }
    }

}
