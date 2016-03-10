//
// Created by Flonly on 3/9/16.
//

#ifndef ANDROIDOMX_AVSOURCE_H
#define ANDROIDOMX_AVSOURCE_H

#include <media/stagefright/MediaSource.h>
#include <media/stagefright/MediaBufferGroup.h>

using namespace android;
struct VideoInfo{
    int width;
    int height;
    int fps;
    int bitrate; //bps
    int iframe_interval;  //seconds
    int color_format;
};

class AVSource : public MediaSource {
public:
    AVSource(struct VideoInfo);

    virtual void updateSouce(char *dst);
    virtual status_t read(MediaBuffer **buffer, const MediaSource::ReadOptions *options);
    virtual sp<MetaData> getFormat() { return mFormat; }
    virtual status_t start(MetaData *params) { return OK; }
    virtual status_t stop() { return OK; }
    virtual ~AVSource();
private:
    void createYuvData();

private:
    ///AVFormatContext *mDataSource;
    //AVCodecContext *mVideoTrack;
    //AVBitStreamFilterContext *mConverter;
    MediaBufferGroup mGroup;
    sp<MetaData> mFormat;
    int mVideoIndex;
    struct VideoInfo mVideoInfo;
    char *mYuv420Blue;
    char *mYuv420Red;
    int mYuvSize;
    uint32_t mFrameCount;
};
#endif //ANDROIDOMX_AVSOURCE_H
