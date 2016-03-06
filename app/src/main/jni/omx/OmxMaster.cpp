//
// Created by flonly on 2016/3/5.
//

#include "OmxMaster.h"
#include <OMX_Core.h>
#include <OMX_Types.h>
#include "utils/Log.h"

#endif

void OmxMaster::list_component() {
    OMX_ERRORTYPE errortype = Omx_Init();
    LOGD("Omx_Init result %x", errortype);
}