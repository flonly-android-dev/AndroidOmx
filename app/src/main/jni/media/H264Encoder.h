//
// Created by Flonly on 3/8/16.
//

#ifndef ANDROIDOMX_H264ENCODER_H
#define ANDROIDOMX_H264ENCODER_H
#include <OMX_Core.h>
#include <OMX_Types.h>
#include <OMX_Video.h>
#include <media/IMediaPlayerService.h>
#include <binder/IServiceManager.h>
#include <media/IOMX.h>
#include "omx/OmxMaster.h"
using namespace android;

class H264Encoder : public OmxMaster, public BnOMXObserver{
public:
    H264Encoder();
    ~H264Encoder();
    int init();

    //test iomx
    int test();
    void initBuffer();
    void configComponent();

    //override
    virtual void list_component();

    //override BnOMXObserver
    virtual void onMessage(const omx_message &msg);
    virtual void OnEvent(const omx_message &msg);
    virtual void OnEmptyBufferDone(const omx_message &msg);
    virtual void OnFillBufferDone(const omx_message &msg);

public:
    static const char OMX_COMPONETNAME[];
    OMX_HANDLETYPE  _pomx_handle;
private:
    OMX_HANDLETYPE pomx_handle_avc;

    android::sp<android::IServiceManager> serviceManager ;
    android::sp<android::IBinder> binder;
    android::sp<android::IMediaPlayerService> mediaPlayerService;
    android::sp<android::IOMX> mOMX;

    IOMX::node_id mNode;
    String8 mCompName;
    uint32_t mInPortIndex;
    uint32_t mOutPortIndex;

    uint32_t vidWidth;
    uint32_t vidHeight;
    uint32_t numInBuf;
    uint32_t numOutBuf;
};
#endif //ANDROIDOMX_H264ENCODER_H
