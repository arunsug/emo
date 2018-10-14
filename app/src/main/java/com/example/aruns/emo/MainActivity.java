package com.example.aruns.emo;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class MainActivity extends AppCompatActivity {


    public DefaultLabelFormatter formatter;
    ValueDependentColor<DataPoint> colorer;
    LinearLayout lin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lin = (LinearLayout)findViewById(R.id.mainLinear);

        Log.d(this.getLocalClassName(),"Service Created");
        handlePermissions();
        startService(new Intent(this, CameraService.class));

        formatter =  new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x value
                    if (value < 6)
                        return CloudVisionTask.getEnumString(CloudVisionTask.Emotion.values[(int)value-1]);
                    else
                        return ((int)value)+"";
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        };

        colorer = new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/6, 100, 100);
            }
        };

        graph();

    }

    protected void onResume()
    {
        super.onResume();
    }

    protected void graph() {


        //LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,400);
        params.leftMargin = 1;
        params.leftMargin = 1;
        params.topMargin = 1;
        params.bottomMargin = 4;


        Set<String> keys = CloudVisionTask.data.keySet();

        for(String key : keys) {

            ArrayList<CloudVisionTask.Pair> cur = CloudVisionTask.data.get(key);
            int[] sums = new int[5];

            for(CloudVisionTask.Pair pair: cur) {
                sums[pair.value.ordinal()]++;
            }

            DataPoint[] points = new DataPoint[5];
            for (int i = 0; i < 5; i++) {
                points[i] = new DataPoint(i+1, sums[i]);
            }

            GraphView graph = new GraphView(this);
            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);

            graph.getGridLabelRenderer().setLabelFormatter(formatter);
            series.setValueDependentColor(colorer);
            series.setSpacing(5);
            series.setDataWidth(1);
            graph.addSeries(series);

            //final View rowView = inflater.inflate(R.layout.activity_main, null);

            lin.addView((View)graph, params);

        }
    }



    public void handlePermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        }
    }
}
