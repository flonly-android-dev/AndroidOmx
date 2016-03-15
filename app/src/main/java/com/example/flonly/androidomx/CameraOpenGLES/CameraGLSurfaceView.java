package com.example.flonly.androidomx.CameraOpenGLES;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by flonly on 3/11/16.
 */
public class CameraGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "CameraGLSurfaceView";
    private Context mContext;
    private SurfaceTexture mSurface;
    private int mTextureID = -1;
    private DirectDrawer mDirectDrawer;
    private MyCamera mCamera;

    public CameraGLSurfaceView(Context context, MyCamera camera) {
        super(context);
        mContext = context;
        mCamera = new MyCamera();
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    public CameraGLSurfaceView(Context context, AttributeSet attrs) {
        super(context,attrs);
        mContext = context;
        mCamera = new MyCamera();
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

//    public void setCamere(MyCamera camera){
//        mCamera = camera;
//    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        //Log.i(TAG, "onFrameAvailable...");
        this.requestRender();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated...");
        mTextureID = createTextureID();
        mSurface = new SurfaceTexture(mTextureID);
        mSurface.setOnFrameAvailableListener(this);
        mDirectDrawer = new DirectDrawer(mTextureID);
        mCamera.start(this,mSurface,mContext);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d("GL", "GL_RENDERER = " + gl.glGetString(GL10.GL_RENDERER));
        Log.d("GL", "GL_VENDOR = " + gl.glGetString(GL10.GL_VENDOR));
        Log.d("GL", "GL_VERSION = " + gl.glGetString(GL10.GL_VERSION));
        Log.i("GL", "GL_EXTENSIONS = " + gl.glGetString(GL10.GL_EXTENSIONS));

        Log.i(TAG, "onSurfaceChanged..." + getRotation() + " width=" + width + " height=" + height
                + " layout Direction=" + getLayoutDirection());
        GLES20.glViewport(0, 0, width, height);

//        WindowManager mWindowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
//        Display mDisplay = mWindowManager.getDefaultDisplay();
//        //int orientation = mContext.getResources().getConfiguration().getLayoutDirection();
//        int orientation = mDisplay.getRotation();
        //setRotation(orientation*90);
//        if(!CameraInterface.getInstance().isPreviewing()){
//            CameraInterface.getInstance().doStartPreview(mSurface, 1.33f);
//        }
        mCamera.stop();
        mCamera.start(this,mSurface,mContext);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //Log.i(TAG, "onDrawFrame...");
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mSurface.updateTexImage();
        float[] mtx = new float[16];
        mSurface.getTransformMatrix(mtx);
        mDirectDrawer.draw(mtx);
    }

    @Override
    public void onPause() {
        super.onPause();
        //CameraInterface.getInstance().doStopCamera();
        //mCamera.stop();
        mCamera.stop();
    }

    private int createTextureID()
    {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }

    public SurfaceTexture getSurfaceTexture(){
        return mSurface;
    }

}
