package com.example.aruns.emo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startActivity( new Intent(StartupActivity.this, MainActivity.class));
        return true;
    }
}
