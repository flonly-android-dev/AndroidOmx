//
// Created by Flonly on 3/9/16.
//

#include "utils/mlog.h"
#include "VideoCodec.h"

VideoCodec::VideoCodec() {
    mVideoSource = new AVSource();
}

VideoCodec::~VideoCodec() {
    //mVideoSource->clear();
    mVideoEncoder->stop();
    delete mVideoSource;
    mClient.disconnect();
}

int VideoCodec::init() {
    status_t status;
    status = mClient.connect();
    LOGD("mClient.connect() status[0x%08x]", status);

    bool createEncoder = true;
    mVideoEncoder = OMXCodec::Create(mClient.interface(), mVideoSource->getFormat(), createEncoder,
                                     mVideoSource, NULL, 0, NULL);
    if(mVideoEncoder == NULL){
        LOGE("create mVideoEncoder failed");
    }else{
        LOGI("create mVideoEncoder success");
    }
    mVideoEncoder->start();
}

void VideoCodec::test() {
    vep.module = this;
    vep.type = THREAD_TYPE_VIDEO_ENCODE;
    _thread_encode_video.Start("video_encoder");
    _thread_encode_video.AddTask(thread_main, &vep);
}

void* VideoCodec::thread_main(void* param){
    ThreadParam* pParam = (ThreadParam*)param;
    switch (pParam->type) {
        case THREAD_TYPE_VIDEO_ENCODE:
            pParam->module->encode_video();
            break;
        default:
            break;
    }
    LOGE("VideoCodec::Run thread type = %d\n", pParam->type);
    return NULL;
}

void VideoCodec::encode_video() {
    uint32_t framecount = 0;
    for (; ;) {
        MediaBuffer *mVideoBuffer;
        MediaSource::ReadOptions options;
        status_t err = mVideoEncoder->read(&mVideoBuffer, &options);
        if (err == OK) {
            if (mVideoBuffer->range_length() > 0) {
                // If video frame availabe, render it to mNativeWindow
//                sp<MetaData> metaData = mVideoBuffer->meta_data();
//                int64_t timeUs = 0;
//                metaData->findInt64(kKeyTime, &timeUs)
//                native_window_set_buffers_timestamp(mNativeWindow.get(), timeUs * 1000);
//                err = mNativeWindow->queueBuffer(mNativeWindow.get(),
//                                                 mVideoBuffer->graphicBuffer().get());
//                if (err == 0) {
//                    metaData->setInt32(kKeyRendered, 1);
//                }
                framecount++;
                LOGD("encode frame success, framecount=%d",framecount);
            }
            mVideoBuffer->release();
        }
    }
}