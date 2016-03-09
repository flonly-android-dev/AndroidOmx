//
// Created by Flonly on 3/9/16.
//

#include <include/frameworks/av/media/stagefright/MetaData.h>
#include <include/frameworks/av/media/stagefright/MediaDefs.h>
#include <media/openmax/OMX_IVCommon.h>
#include <utils/mlog.h>
#include "AVSource.h"

AVSource::AVSource() {
    mVideoInfo.width = 640;
    mVideoInfo.height = 480;
    mVideoInfo.fps = 30;
    mVideoInfo.bitrate = 800 * 1024;
    mVideoInfo.iframe_interval = 3;
    mVideoInfo.color_format = OMX_COLOR_FormatYUV422Planar;

    mFormat = new MetaData;
    //mFormat->setCString(kKeyMIMEType, MEDIA_MIMETYPE_VIDEO_AVC);
    mFormat->setCString(kKeyMIMEType, MEDIA_MIMETYPE_VIDEO_AVC);
    mFormat->setInt32(kKeyWidth, mVideoInfo.width);
    mFormat->setInt32(kKeyHeight, mVideoInfo.height);
    mFormat->setInt32(kKeyFrameRate, mVideoInfo.fps);
    mFormat->setInt32(kKeyBitRate, mVideoInfo.bitrate);
    mFormat->setInt32(kKeyIFramesInterval, mVideoInfo.iframe_interval);
    mFormat->setInt32(kKeyStride, mVideoInfo.width);
    mFormat->setInt32(kKeySliceHeight, mVideoInfo.height);
    //mFormat->setInt32(kKeyColorFormat, OMX_COLOR_FormatYUV420Planar);
    //mFormat->setInt32(kKeyColorFormat, OMX_COLOR_FormatYUV420PackedPlanar);
    mFormat->setInt32(kKeyColorFormat, OMX_COLOR_FormatYUV420SemiPlanar);

    mYuvSize = mVideoInfo.width * mVideoInfo.height * 3 / 2 + 1;
    mYuv420 = new char[mYuvSize];
    mFrameCount = 0;
}

AVSource::~AVSource() {
    delete mYuv420;
}

status_t AVSource::read(MediaBuffer **buffer, const MediaSource::ReadOptions *options) {
    updateSouce();

    memcpy((*buffer)->data(), mYuv420, mYuvSize);
    (*buffer)->set_range(0, mYuvSize);
    (*buffer)->meta_data()->clear();
    //(*buffer)->meta_data()->setInt32(kKeyIsSyncFrame, packet.flags & AV_PKT_FLAG_KEY);
    (*buffer)->meta_data()->setInt64(kKeyTime, mFrameCount * 1000000 / mVideoInfo.fps);

}

void AVSource::updateSouce() {
    //TODO update yuvdata;
    static struct timeval tv, ctv;
    if (mFrameCount == 0) {
        gettimeofday(&tv, NULL);
    } else {
        gettimeofday(&ctv, NULL);
        uint64_t time_wait = (ctv.tv_sec * 1000000 + ctv.tv_usec) - (tv.tv_sec * 1000000 + tv.tv_usec) -
                             (1000000 / mVideoInfo.fps);
        if (time_wait > 0) {
            usleep(time_wait);
        } else {
            LOGE("encode cost too much time with more %dus",time_wait);
        }
        tv = ctv;
    }
    LOGD("============ updateSouce ============ ");
    mFrameCount++;
}

