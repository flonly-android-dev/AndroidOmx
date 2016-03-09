package com.example.flonly.androidomx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.Surface;
import android.view.SurfaceHolder;

public class MainActivity extends AppCompatActivity {

    public Surface mSurface;
    public SurfaceHolder mSurfaceHolder;

    public native void testomx();
    static {
        System.loadLibrary("mediacore");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPlayerView();

        testomx();
    }


    public void setDisplay(SurfaceHolder sh) {
        mSurfaceHolder = sh;
        if (sh != null) {
            mSurface = sh.getSurface();
        }
    }

    private void initPlayerView(){


    }

}
