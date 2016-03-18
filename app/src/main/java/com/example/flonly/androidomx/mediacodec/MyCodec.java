package com.example.flonly.androidomx.mediacodec;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.util.Log;

import com.example.flonly.androidomx.CameraOpenGLES.CameraGLSurfaceView;

import java.io.IOException;

/**
 * Created by flonly on 3/15/16.
 */
public class MyCodec extends MediaCodec.Callback {
    private static final String TAG = "MyCodec";
    private String mCodecName = "OMX.qcom.video.encoder.avc";
    private MediaCodec mMediaCodec;
    private boolean isRecording;
    private MediaFormat mMediaFormat;
    private CameraGLSurfaceView mSurfaceView;

    public MyCodec(CameraGLSurfaceView glSurfaceView) {
        //mMediaCodec = new MediaCodec();
        try {
            mMediaCodec = MediaCodec.createByCodecName(mCodecName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        isRecording = false;
        mSurfaceView = glSurfaceView;
        initMediaFormat();
    }

    public void captureVideo(){
        //1. test list
        listSupportedCodecs();
        if(!isRecording){
            startCapture();
        }else{
            stopCapture();
        }

    }

    private void startCapture(){
        //init as a encoder
        mMediaCodec.configure(mMediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        //mMediaCodec.setInputSurface(mSurfaceView);
        isRecording = true;
    }

    private void stopCapture(){
        mMediaCodec.stop();
        mMediaCodec.release();
        isRecording = false;
    }

    private void initMediaFormat(){
        mMediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC,
                mSurfaceView.getWidth(), mSurfaceView.getHeight());
        mMediaFormat.setInteger(MediaFormat.KEY_BIT_RATE,125000);
        mMediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE,30);
        mMediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT,MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar);
        mMediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL,60);
    }

    private void listSupportedCodecs(){
        //MediaCodecList.getCodecCount();
        /*OMX.google.g711.alaw.decoder
        OMX.google.g711.mlaw.decoder
        OMX.google.vorbis.decoder
        OMX.google.opus.decoder
        OMX.google.raw.decoder
        OMX.google.aac.encoder
        OMX.google.amrnb.encoder
        OMX.google.amrwb.encoder
        OMX.google.flac.encoder
        OMX.google.gsm.decoder
        OMX.qcom.audio.encoder.evrc
        OMX.qcom.audio.encoder.qcelp13
        OMX.qcom.video.encoder.avc
        OMX.qcom.video.encoder.mpeg4
        OMX.qcom.video.encoder.h263
        OMX.qcom.video.encoder.vp8
        OMX.google.aac.decoder
        OMX.qcom.audio.decoder.Qcelp13
        OMX.qcom.audio.decoder.evrc
        OMX.qcom.video.decoder.avc
        OMX.qcom.video.decoder.mpeg2
        OMX.qcom.video.decoder.mpeg4
        OMX.qcom.video.decoder.h263
        OMX.qcom.video.decoder.vc1
        OMX.qcom.video.decoder.divx
        OMX.qcom.video.decoder.divx311
        OMX.qcom.video.decoder.divx4
        OMX.qcom.video.decoder.vp8
        OMX.qcom.video.decoder.hevc
        OMX.google.mpeg4.decoder
        OMX.google.h263.decoder
        OMX.google.h264.decoder
        OMX.google.hevc.decoder
        OMX.google.vp8.decoder
        OMX.google.vp9.decoder
        OMX.google.h263.encoder
        OMX.google.h264.encoder
        OMX.google.mpeg4.encoder
        OMX.google.vp8.encoder*/
        MediaCodecList codecList = new MediaCodecList(MediaCodecList.REGULAR_CODECS);
        MediaCodecInfo[] codecInfos = codecList.getCodecInfos();
        for(int i=0; i< codecInfos.length; i++){
            Log.d(TAG, "codec name :" + codecInfos[i].getName());
        }
    }

    @Override
    public void onInputBufferAvailable(MediaCodec codec, int index) {
        Log.d(TAG,"onInputBufferAvailable");
    }

    @Override
    public void onOutputBufferAvailable(MediaCodec codec, int index, MediaCodec.BufferInfo info) {
        Log.d(TAG,"onOutputBufferAvailable");
    }

    @Override
    public void onError(MediaCodec codec, MediaCodec.CodecException e) {
        Log.d(TAG,"onError");
    }

    @Override
    public void onOutputFormatChanged(MediaCodec codec, MediaFormat format) {
        Log.d(TAG,"onOutputFormatChanged");
    }
}
