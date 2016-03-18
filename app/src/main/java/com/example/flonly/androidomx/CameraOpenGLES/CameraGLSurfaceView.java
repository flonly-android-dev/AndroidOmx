package com.example.flonly.androidomx.CameraOpenGLES;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.EGL14;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.example.flonly.androidomx.MainActivity;
import com.example.flonly.androidomx.mediacodec.TextureMovieEncoder;

import java.io.File;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by flonly on 3/11/16.
 */
public class CameraGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "CameraGLSurfaceView";

    private static final int RECORDING_OFF = 0;
    private static final int RECORDING_ON = 1;
    private static final int RECORDING_RESUMED = 2;

    private Context mContext;
    private SurfaceTexture mSurface;
    private int mTextureID = -1;
    private DirectDrawer mDirectDrawer;
    private MyCamera mCamera;

    private TextureMovieEncoder mVideoEncoder;
    private File mOutputFile;
    //private int mTextureId;
    private boolean mRecordingEnabled;
    private int mRecordingStatus;
    private int mFrameCount;
    // width/height of the incoming camera preview frames
    private boolean mIncomingSizeUpdated;
    private int mIncomingWidth;
    private int mIncomingHeight;

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

        mVideoEncoder = new TextureMovieEncoder();
        mOutputFile = MainActivity.getOutputMediaFile(MainActivity.MEDIA_TYPE_VIDEO);

        mTextureID = -1;

        mRecordingStatus = -1;
        mRecordingEnabled = false;
        mFrameCount = -1;

        mIncomingSizeUpdated = false;
        mIncomingWidth = mIncomingHeight = -1;

        Log.d(TAG, "constructor called");
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
        //mDirectDrawer = new DirectDrawer(mTextureID);
        mDirectDrawer = new DirectDrawer();
        mCamera.start(this, mSurface, mContext);
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

        mRecordingEnabled = mVideoEncoder.isRecording();
        if (mRecordingEnabled) {
            mRecordingStatus = RECORDING_RESUMED;
        } else {
            mRecordingStatus = RECORDING_OFF;
        }

//        WindowManager mWindowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
//        Display mDisplay = mWindowManager.getDefaultDisplay();
//        //int orientation = mContext.getResources().getConfiguration().getLayoutDirection();
//        int orientation = mDisplay.getRotation();
        //setRotation(orientation*90);
//        if(!CameraInterface.getInstance().isPreviewing()){
//            CameraInterface.getInstance().doStartPreview(mSurface, 1.33f);
//        }
        mCamera.stop();
        mCamera.start(this, mSurface, mContext);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //Log.i(TAG, "onDrawFrame...");
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mSurface.updateTexImage();


        // If the recording state is changing, take care of it here.  Ideally we wouldn't
        // be doing all this in onDrawFrame(), but the EGLContext sharing with GLSurfaceView
        // makes it hard to do elsewhere.
        if (mRecordingEnabled) {
            switch (mRecordingStatus) {
                case RECORDING_OFF:
                    Log.d(TAG, "START recording, output file : " + mOutputFile.toString());
                    // start recording
                    mVideoEncoder.startRecording(new TextureMovieEncoder.EncoderConfig(
                            mOutputFile, 720, 1134, 1000000, EGL14.eglGetCurrentContext()));
//                    mVideoEncoder.startRecording(new TextureMovieEncoder.EncoderConfig(
//                            mOutputFile, mIncomingWidth, mIncomingHeight, 1000000, EGL14.eglGetCurrentContext()));
                    mRecordingStatus = RECORDING_ON;
                    break;
                case RECORDING_RESUMED:
                    Log.d(TAG, "RESUME recording, output file : " + mOutputFile.toString());
                    mVideoEncoder.updateSharedContext(EGL14.eglGetCurrentContext());
                    mRecordingStatus = RECORDING_ON;
                    break;
                case RECORDING_ON:
                    // yay
                    break;
                default:
                    throw new RuntimeException("unknown status " + mRecordingStatus);
            }
        } else {
            switch (mRecordingStatus) {
                case RECORDING_ON:
                case RECORDING_RESUMED:
                    // stop recording
                    Log.d(TAG, "STOP recording");
                    mVideoEncoder.stopRecording();
                    mRecordingStatus = RECORDING_OFF;
                    break;
                case RECORDING_OFF:
                    // yay
                    break;
                default:
                    throw new RuntimeException("unknown status " + mRecordingStatus);
            }
        }

        // Set the video encoder's texture name.  We only need to do this once, but in the
        // current implementation it has to happen after the video encoder is started, so
        // we just do it here.
        //
        // TODO: be less lame.
        mVideoEncoder.setTextureId(mTextureID);

        // Tell the video encoder thread that a new frame is available.
        // This will be ignored if we're not actually recording.
        mVideoEncoder.frameAvailable(mSurface);

        //TODO ??
        if (mIncomingSizeUpdated) {
            mDirectDrawer.setTexSize(mIncomingWidth, mIncomingHeight);
            mIncomingSizeUpdated = false;
        }

        float[] mtx = new float[16];
        mSurface.getTransformMatrix(mtx);

        //Log.d(TAG, "onDrawFrame: mTextureID = " + mTextureID);
//        mDirectDrawer.draw(mtx, mTextureID);
//        long t1=System.currentTimeMillis();
//        ByteBuffer PixelBuffer = ByteBuffer.allocateDirect(640*480*3);
//        PixelBuffer.order(ByteOrder.nativeOrder());
//        PixelBuffer.position(0);
//        for(int i=0; i<100;i++){
//            GLES20.glReadPixels(0, 0, 640, 480, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, PixelBuffer);
//        }
//        GLES20.glReadPixels(0, 0, 640, 480, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, PixelBuffer);
//        GLES20.glReadPixels(0,0,640,480,GLES20.GL_RGB,GLES20.GL_UNSIGNED_BYTE,PixelBuffer);
//        GLES20.glReadPixels(0,0,640,480,GLES20.GL_RGB,GLES20.GL_UNSIGNED_BYTE,PixelBuffer);
//        long t2=System.currentTimeMillis();
        //Log.d(TAG,"time cost for read rgb from gl : " + (t2-t1));

    }


    /**
     * Records the size of the incoming camera preview frames.
     * <p>
     * It's not clear whether this is guaranteed to execute before or after onSurfaceCreated(),
     * so we assume it could go either way.  (Fortunately they both run on the same thread,
     * so we at least know that they won't execute concurrently.)
     */
    public void setCameraPreviewSize(int width, int height) {
        Log.d(TAG, "setCameraPreviewSize");
        mIncomingWidth = width;
        mIncomingHeight = height;
        mIncomingSizeUpdated = true;
    }

    /**
     * Notifies the renderer that we want to stop or start recording.
     */
    public void changeRecordingState(boolean isRecording) {
        Log.d(TAG, "changeRecordingState: was " + mRecordingEnabled + " now " + isRecording);
        mRecordingEnabled = isRecording;
    }

    @Override
    public void onResume() {
        super.onResume();
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
