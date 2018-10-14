package com.example.aruns.emo;

import android.os.Bundle;
import android.app.Activity;

import java.util.ArrayList;

public class appgraphs extends Activity {

    String app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appgraphs);
    }

    public void graphApp(){

        ArrayList<Pair> appData = Information.information.data.get(app);

        for ( int i = 0; i< appData.size(); i++) {

        }

        for ( int i =0; i<5; i++) {

            

        }
    }

}
