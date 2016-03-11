package com.example.flonly.androidomx.CameraOpenGLES;

import android.app.Activity;
import android.os.Bundle;

import com.example.flonly.androidomx.R;

/**
 * Created by flonly on 3/11/16.
 */
public class MyCameraActivity extends Activity{

    private CameraGLSurfaceView glSurfaceView;
    private MyCamera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///setContentView(R.layout.camera_gl);
        mCamera = new MyCamera();

        glSurfaceView = new CameraGLSurfaceView(this, mCamera);

        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mCamera.stop();
    }
}
