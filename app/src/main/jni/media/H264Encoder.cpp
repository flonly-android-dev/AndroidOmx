//
// Created by Flonly on 3/8/16.
//

#include <stddef.h>
#include <media/IOMX.h>
#include <binder/MemoryDealer.h>
#include <OMX_Component.h>
#include "H264Encoder.h"
#include "utils/mlog.h"

//OMX.qcom.video.decoder.avc
const char H264Encoder::OMX_COMPONETNAME[] = "OMX.qcom.video.encoder.avc";
using namespace android;

H264Encoder::H264Encoder() :pomx_handle_avc(NULL){

}

H264Encoder::~H264Encoder() {
}

int H264Encoder::init() {
//    OMX_ERRORTYPE errortype = OMX_Init();
//    if(errortype != OMX_ErrorNone){
//        LOGD("Omx_Init success");
//    }else{
//        LOGE("Omx_Init result %x", errortype);
//    }

    serviceManager = android::defaultServiceManager();
    binder = serviceManager->getService(android::String16("media.player"));
    mediaPlayerService = android::interface_cast<android::IMediaPlayerService>(binder);
    mOMX = mediaPlayerService->getOMX();
}

int H264Encoder::test() {
    OMX_ERRORTYPE eError = OMX_ErrorNone;
    //1.list
    //list_component();
    //TODO check h264(avc) encoder() component exists
    //2.load h264(avc) encoder() component
    //eError = load_componet(pomx_handle_avc, (char*)OMX_COMPONETNAME, NULL);

    //=============test iomx ======

    list_component();
    initBuffer();
    configComponent();
}

void H264Encoder::list_component() {
    String8 ComponentRoleAvcDecoder = String8("video_decoder.avc");
    String8 ComponentRoleAvcEncoder = String8("video_encoder.avc");

    List<IOMX::ComponentInfo> componentInfos;
    status_t err = mOMX->listNodes(&componentInfos);

    List<IOMX::ComponentInfo>::iterator it = componentInfos.begin();
    const IOMX::ComponentInfo &info = *it;
    const char *componentName = info.mName.string();
    bool found = false;
    for (List<IOMX::ComponentInfo>::iterator it = componentInfos.begin();
         it != componentInfos.end(); ++it)
    {

        const IOMX::ComponentInfo &info = *it;
        const char *componentName = info.mName.string();

        for (List<String8>::const_iterator role_it = info.mRoles.begin();
             role_it != info.mRoles.end(); ++role_it)
        {
            const char *componentRole = (*role_it).string();
           LOGD("componentName: %s, componentRole: %s\n", componentName, componentRole);

            if((*role_it) == ComponentRoleAvcEncoder && !found){
                found = true;
                mCompName = info.mName;
                mOMX->allocateNode(mCompName.string(), this, &mNode);
                LOGD("find ComponentRoleAvcEncoder componentName: %s, componentRole: %s\n",
                     componentName, componentRole);
            }
        }
    }
}

void H264Encoder::initBuffer(){

    mInPortIndex = 0;
    mOutPortIndex = 1;
    int inBufLen = 1024*1024;
    numInBuf = 8;
    numOutBuf = 8;

//    sp<MemoryDealer> mDealer = new MemoryDealer(16 * 1024 * 1024, "OmxH264Eec");
//    sp<IMemory> mMemory = mDealer->allocate(inBufLen);
//    IOMX::buffer_id mID;
//
//
//
//    int i = 0;
//    for(i = 0; i < numInBuf; i++)
//    {
//        mOMX->emptyBuffer()
//        mOMX->useBuffer(mNode, mInPortIndex, mMemory, &mID);
//        //mBufferHandler->registerInBuf(mMemory, mID);
//    }
//
//    for(i = 0; i < numOutBuf; i++)
//    {
//        mOMX->useBuffer(mNode, mOutPortIndex, mMemory, &mID);
//        //mBufferHandler->registerOutBuf(mMemory, mID);
//    }
}

void H264Encoder::configComponent() {

    vidWidth    = 480;
    vidHeight   = 320;

    status_t  status = 0;

    OMX_PARAM_PORTDEFINITIONTYPE portDefn;
    portDefn.nPortIndex = mInPortIndex;

    status=mOMX->getParameter(mNode, OMX_IndexParamPortDefinition, &portDefn, sizeof(portDefn));
    LOGD("getParameter  for port %d, status[0x%08x]",portDefn.nPortIndex, status );
    LOGD("getParameter  for port %d, status[0x%08x],portDefn.format.video.nFrameWidth=%d ",
         portDefn.nPortIndex, status, portDefn.format.video.nFrameWidth);

    portDefn.nBufferCountActual = numInBuf;        // set some suitable value here or don’t update to
                                                    // use default value
    portDefn.format.video.nFrameWidth = vidWidth;   // width of the video to be played
    portDefn.format.video.nFrameHeight = vidHeight; // height of video to be played
    portDefn.format.video.nStride = vidWidth;
    portDefn.format.video.nSliceHeight = vidHeight;

    status = mOMX->setParameter(mNode, OMX_IndexParamPortDefinition, &portDefn, sizeof(portDefn));
    LOGD("setParameter  for port %d, status[0x%08x]",portDefn.nPortIndex, status );
    portDefn.format.video.nFrameWidth = 555;
    status=mOMX->getParameter(mNode, OMX_IndexParamPortDefinition, &portDefn, sizeof(portDefn));
    LOGD("getParameter  for port %d, status[0x%08x],portDefn.format.video.nFrameWidth=%d ",
         portDefn.nPortIndex, status, portDefn.format.video.nFrameWidth);


    portDefn.nPortIndex = mOutPortIndex;

    status = mOMX->getParameter(mNode, OMX_IndexParamPortDefinition, &portDefn, sizeof(portDefn));
    LOGD("getParameter  for port %d, status[0x%08x]",portDefn.nPortIndex, status );

    portDefn.nBufferCountActual = numOutBuf; // set suitable value or leave to default.
    portDefn.nBufferSize = (vidWidth * vidHeight * 3) / 2;
    portDefn.format.video.nFrameWidth = vidWidth;
    portDefn.format.video.nFrameHeight = vidHeight;
    portDefn.format.video.nStride = vidWidth;
    portDefn.format.video.nSliceHeight = vidHeight;
    status = mOMX->setParameter(mNode, OMX_IndexParamPortDefinition, &portDefn, sizeof(portDefn));
    LOGD("setParameter  for port %d, status[0x%08x]",portDefn.nPortIndex, status );
}

void H264Encoder::onMessage(const omx_message &msg)
{
    switch(msg.type)
    {
        case omx_message::EVENT:
            OnEvent(msg);
            break;

        case omx_message::EMPTY_BUFFER_DONE:
            OnEmptyBufferDone(msg);
            break;

        case omx_message::FILL_BUFFER_DONE:
            OnFillBufferDone(msg);
            break;
    }
}
void H264Encoder::OnEvent(const omx_message &msg){
    LOGD("message in %s", __FUNCTION__);
}

void H264Encoder::OnEmptyBufferDone(const omx_message &msg){
    //mOMX->emptyBuffer()
    LOGD("message in %s", __FUNCTION__);
}

void H264Encoder::OnFillBufferDone(const omx_message &msg){
    LOGD("message in %s", __FUNCTION__);
}
