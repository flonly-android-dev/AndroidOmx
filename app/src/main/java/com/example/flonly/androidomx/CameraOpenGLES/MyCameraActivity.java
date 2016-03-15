package com.example.flonly.androidomx.CameraOpenGLES;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.example.flonly.androidomx.R;

/**
 * Created by flonly on 3/11/16.
 */
public class MyCameraActivity extends Activity{

    private static final String TAG = "MyCameraActivity";
    private CameraGLSurfaceView glSurfaceView;
    //private GLSurfaceView glSurfaceView;
    //private MyCamera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glsurface);
        //mCamera = new MyCamera();
        //glSurfaceView = new CameraGLSurfaceView(this, mCamera);

//        int sysAutoRotate = 0;
//        try {
//            sysAutoRotate = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
//        } catch (Settings.SettingNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        Log.d(TAG,"Auto-rotate Screen from Device Settings:" + sysAutoRotate);
//
//        WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//        Display mDisplay = mWindowManager.getDefaultDisplay();
//        Log.d(TAG, "on create display orientation=" + mDisplay.getRotation());

        glSurfaceView = (CameraGLSurfaceView)findViewById(R.id.surfaceviewclass);
        //setContentView(glSurfaceView);
        //
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        glSurfaceView = (CameraGLSurfaceView)findViewById(R.id.surfaceviewclass);
        glSurfaceView.onPause();
//        mCamera.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView = (CameraGLSurfaceView)findViewById(R.id.surfaceviewclass);
        glSurfaceView.onResume();
    }

    public void startRecord(View v){
        Log.d(TAG,"start recoding ~~~");
    }
}
