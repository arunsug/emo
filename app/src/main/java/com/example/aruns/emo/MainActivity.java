package com.example.aruns.emo;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

<<<<<<< HEAD
    GraphAdapter graphAdapter;
=======
    public DefaultLabelFormatter formatter;
    ValueDependentColor<DataPoint> colorer;
    private LinearLayout lin;
    private HashMap<String, GraphView> graphs;
    private LinearLayout.LayoutParams params;

>>>>>>> 6bad119df04e418e97ae7f99a9559ead7292394d
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        graphAdapter = new GraphAdapter(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(graphAdapter);

        Log.d(this.getLocalClassName(),"Service Created");
        handlePermissions();
        startService(new Intent(this, CameraService.class));
    }

    public void showAppData(String app){
        Intent AppData = new Intent(this, appgraphs.class);
        AppData.putExtra("APP_NAME", app);
        startActivity(AppData);
    }

    protected void onResume()
    {
        super.onResume();
        Log.v("MainActivity", "datachange");
        graphAdapter.notifyDataSetChanged();
    }

    public void handlePermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        }
    }
}
