package com.example.flonly.androidomx.CameraOpenGLES;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.example.flonly.androidomx.MyGLSurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by flonly on 3/11/16.
 */
public class MyCamera {
    private final static String LOG_TAG = "MyCamera";

    private Camera mCamera;
    private Camera.Parameters mCameraParams;
    private Boolean running = false;
    private CameraGLSurfaceView mSurfaceView;
    private Context mContext;
    private  static String TAG = "MyCamera";

    void start(CameraGLSurfaceView surfaceView, SurfaceTexture surface, Context context) {
        mSurfaceView = surfaceView;
        mContext = context;
        Log.v(LOG_TAG, "Starting Camera");

        mCamera = Camera.open(0);
        mCameraParams = mCamera.getParameters();
        Log.v(LOG_TAG, mCameraParams.getPreviewSize().width + " x " + mCameraParams.getPreviewSize().height);

        //mCamera.setDisplayOrientation(180);
        setUpCamera();
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
            running = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void stop() {
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

    private void setUpCamera() {
//        Camera.Parameters parameters = mCamera.getParameters();
//        Log.d(TAG,"camera preview size " + parameters.getPreviewSize());
//        return
        Camera.Parameters parameters = mCamera.getParameters();
//        List<Camera.Size> spSizes = parameters.getSupportedPreviewSizes();
//        for(int i = 0; i < spSizes.size(); i++ ){
//            Log.v(LOG_TAG, "supported size: " + spSizes.get(i).width + " x " + spSizes.get(i).height);
//        }
        WindowManager mWindowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
        Display mDisplay = mWindowManager.getDefaultDisplay();
        //int orientation = mContext.getResources().getConfiguration().getLayoutDirection();
        int orientation = mDisplay.getRotation();
        final int height = parameters.getPreviewSize().height;
        final int width = parameters.getPreviewSize().width;
        Log.d(MyCamera.class.toString(), "surface view width = " + width + " height=" + height + " orientation = " + orientation);
        if (orientation == Surface.ROTATION_0) {
            //parameters.setPreviewSize(height, width);
            //parameters.setRotation(90);
            mCamera.setDisplayOrientation(0);

            //mSurfaceView.setRotation(0);
        }

        if (orientation == Surface.ROTATION_90) {
            //.setPreviewSize(width, height);
            mCamera.setDisplayOrientation(270);
            //parameters.setRotation(0);
            //mSurfaceView.setRotation(90);
        }

        if (orientation == Surface.ROTATION_180) {
            //parameters.setPreviewSize(height, width);
            mCamera.setDisplayOrientation(180);
            //parameters.setRotation(270);
            //mSurfaceView.setRotation(180);
        }

        if (orientation == Surface.ROTATION_270) {
            //parameters.setPreviewSize(width, height);
            mCamera.setDisplayOrientation(90);
            //parameters.setRotation(180);
            //mSurfaceView.setRotation(270);
        }

        mSurfaceView.queueEvent(new Runnable() {
            @Override public void run() {
                mSurfaceView.setCameraPreviewSize(width, height);
            }
        });
        //mCamera.setParameters(parameters);
    }
}
