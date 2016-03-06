package com.example.flonly.androidomx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testomx();
    }
    public native void  testomx();
    static {
        System.loadLibrary("mediacore");
    }
}
