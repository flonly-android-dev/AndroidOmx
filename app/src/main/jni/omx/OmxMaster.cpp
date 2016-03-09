//
// Created by flonly on 2016/3/5.
//

#include "OmxMaster.h"
#include <OMX_Core.h>
#include <OMX_Types.h>
#include <utils/mlog.h>
#include <stddef.h>


/*list component:
 * 03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 0: OMX.qcom.video.decoder.avc
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 1: OMX.qcom.video.decoder.avc.secure
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 2: OMX.qcom.video.decoder.divx4
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 3: OMX.qcom.video.decoder.divx
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 4: OMX.qcom.video.decoder.divx311
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 5: OMX.qcom.video.decoder.mpeg4
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 6: OMX.qcom.video.decoder.mpeg2
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 7: OMX.qcom.video.decoder.mpeg2.secure
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 8: OMX.qcom.video.decoder.vc1
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 9: OMX.qcom.video.decoder.wmv
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 10: OMX.qcom.video.decoder.h263
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 11: OMX.qcom.video.decoder.hevcswvdec
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 12: OMX.qcom.video.decoder.hevc
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 13: OMX.qcom.video.decoder.hevc.secure
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 14: OMX.qcom.video.decoder.vp8
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 15: OMX.qcom.video.encoder.mpeg4
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 16: OMX.qcom.video.encoder.h263
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 17: OMX.qcom.video.encoder.avc
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 18: OMX.qcom.video.encoder.avc.secure
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 19: OMX.qcom.video.encoder.vp8
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 20: OMX.qcom.audio.decoder.Qcelp13
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 21: OMX.qcom.audio.decoder.evrc
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 22: OMX.qcom.audio.decoder.wma
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 23: OMX.qcom.audio.decoder.amrwbplus
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 24: OMX.qcom.audio.decoder.wma10Pro
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 25: OMX.qcom.audio.decoder.wmaLossLess
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 26: OMX.qcom.audio.decoder.aac
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 27: OMX.qcom.audio.encoder.aac
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 28: OMX.qcom.audio.encoder.qcelp13
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 29: OMX.qcom.audio.encoder.evrc
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 30: OMX.qcom.audio.encoder.amrnb
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 31: OMX.qcom.audio.decoder.aac
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 32: OMX.qcom.audio.decoder.multiaac
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 33: AIV.play
03-08 09:46:44.011 26234-26234/com.example.flonly.androidomx D/libplasma: Component 34: OMX.qcom.file.muxer
 */

void OmxMaster::list_component() {
    /* print a list of all components */
    char szCompName[256]={0};
    OMX_ERRORTYPE eError = OMX_ErrorNone;
    for (int i=0; OMX_ErrorNoMore != eError; i++)
    {
        eError = OMX_ComponentNameEnum(szCompName, 256, i);
        if (OMX_ErrorNone == eError)
            LOGD("Component %i: %s\n",i, szCompName);
    }
}

OMX_ERRORTYPE event_handler(
        OMX_IN OMX_HANDLETYPE hComponent,
        OMX_IN OMX_PTR pAppData,
        OMX_IN OMX_EVENTTYPE eEvent,
        OMX_IN OMX_U32 nData1,
        OMX_IN OMX_U32 nData2,
        OMX_IN OMX_PTR pEventData){

    LOGD("enter func: %s",__FUNCTION__);
}

OMX_ERRORTYPE empty_buffer_done(
        OMX_IN OMX_HANDLETYPE hComponent,
        OMX_IN OMX_PTR pAppData,
        OMX_IN OMX_BUFFERHEADERTYPE* pBuffer){
    LOGD("enter func: %s",__FUNCTION__);
}

OMX_ERRORTYPE fill_buffer_done(
        OMX_OUT OMX_HANDLETYPE hComponent,
        OMX_OUT OMX_PTR pAppData,
        OMX_OUT OMX_BUFFERHEADERTYPE* pBuffer){

    LOGD("enter func: %s",__FUNCTION__);

}

OMX_ERRORTYPE OmxMaster::load_componet(OMX_HANDLETYPE &pHandler, OMX_STRING cComponentName,
                                       OMX_PTR pAppData){

    OMX_CALLBACKTYPE pCallBacks = {event_handler, empty_buffer_done, fill_buffer_done};
    OMX_ERRORTYPE eError = OMX_ErrorNone;


    eError = OMX_GetHandle(&pHandler, cComponentName, pAppData, NULL);

    if(eError == OMX_ErrorNone){
        LOGI("load component %s success",cComponentName);
    }else{
        LOGE("load component %s fail, error=0x%08x",cComponentName, eError);
    }

    return  eError;
}