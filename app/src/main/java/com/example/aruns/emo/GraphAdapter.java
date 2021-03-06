package com.example.aruns.emo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.icu.text.IDNA;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityRecord;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GraphAdapter extends RecyclerView.Adapter {

    private Context context;
    public DefaultLabelFormatter formatter;
    ValueDependentColor<DataPoint> colorer;

    public GraphAdapter(Context context){
        this.context = context;

        colorer = new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/6, 100, 100);
            }
        };

        formatter =  new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x value
                    if (value < 5)
                        return Information.getEnumString(Emotion.values[(int)value]);
                    else
                        return ((int)value)+"";
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_layout, null);
        GraphHolder holder = new GraphHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        if(Information.information == null) {
            Information.information = new Information();
            Information.information.createInfoFromMemory(context);
        }
        ArrayList<Pair> pairs =  Information.information.data.get((new ArrayList(Information.information.data.keySet())).get(position));

        int[] sums = new int[5];

        for(Pair pair: pairs) {
            sums[pair.value.ordinal()]++;
        }

        DataPoint[] points = new DataPoint[5];
        for (int i = 0; i < 5; i++) {
            points[i] = new DataPoint(i, sums[i]);
        }

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);

        ((GraphHolder) h).graphView.getGridLabelRenderer().setLabelFormatter(formatter);

        ((GraphHolder) h).graphView.setTitle((new ArrayList<>(Information.information.data.keySet())).get(position) );

        series.setValueDependentColor(colorer);
        series.setSpacing(5);
        series.setDataWidth(1);

        ((GraphHolder) h).graphView.removeAllSeries();
        ((GraphHolder) h).graphView.addSeries(series);
    }

    @Override
    public int getItemCount() {
        // 6:05 AM app crashes here
        return Information.information.data.keySet().size();
    }

    public class GraphHolder extends RecyclerView.ViewHolder {
        GraphView graphView;

        public GraphHolder(View itemView) {
            super(itemView);

            graphView = itemView.findViewById(R.id.graphView);

            FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 400);
            p.leftMargin = 1;
            p.topMargin = 1;
            p.bottomMargin = 4;
            ((View)graphView).setLayoutParams(p);
            //textView = itemView.findViewById(R.id.textView);
        }
    }
}
