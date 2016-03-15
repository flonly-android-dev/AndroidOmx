package com.example.flonly.androidomx.CameraOpenGLES;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.io.IOException;

/**
 * Created by flonly on 3/11/16.
 */
public class MyCamera {
    private final static String LOG_TAG = "MyCamera";

    private Camera mCamera;
    private Camera.Parameters mCameraParams;
    private Boolean running = false;

    void start(SurfaceTexture surface, Camera.Parameters cp, int angle)
    {
        Log.v(LOG_TAG, "Starting Camera");

        mCamera = Camera.open(0);
        mCameraParams = mCamera.getParameters();
        Log.v(LOG_TAG, mCameraParams.getPreviewSize().width + " x " + mCameraParams.getPreviewSize().height);
        mCamera.setDisplayOrientation(angle);

        if(cp != null){
            mCamera.setParameters(cp);
        }
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
            running = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    void stop()
    {
        if (running) {
            Log.v(LOG_TAG, "Stopping Camera");
            mCamera.stopPreview();
            mCamera.release();
            running = false;
        }
    }

    public Camera.Parameters getParameters() {
        return mCamera.getParameters();
    }

    public void setDisplayOrientation(int i) {
        mCamera.setDisplayOrientation(i);
    }
}
