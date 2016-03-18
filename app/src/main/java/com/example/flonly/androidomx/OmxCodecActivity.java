package com.example.flonly.androidomx;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by flonly on 3/18/16.
 */
public class OmxCodecActivity extends Activity {

    public native void testomx();
    static {
        System.loadLibrary("mediacore");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.omx_codec);
        testomx();
    }

}
