//
// Created by flonly on 2016/3/5.
//

#ifndef ANDROIDOMX_OMXMASTER_H
#define ANDROIDOMX_OMXMASTER_H

#include <OMX_Types.h>
#include <OMX_Core.h>

class OmxMaster {
public:
    virtual void list_component();

    /** The event_handler method is used to notify the application when an
       event of interest occurs.  Events are defined in the OMX_EVENTTYPE
       enumeration.  Please see that enumeration for details of what will
       be returned for each type of event. Callbacks should not return
       an error to the component, so if an error occurs, the application
       shall handle it internally.  This is a blocking call.

       The application should return from this call within 5 msec to avoid
       blocking the component for an excessively long period of time.

       @param hComponent
           handle of the component to access.  This is the component
           handle returned by the call to the GetHandle function.
       @param pAppData
           pointer to an application defined value that was provided in the
           pAppData parameter to the OMX_GetHandle method for the component.
           This application defined value is provided so that the application
           can have a component specific context when receiving the callback.
       @param eEvent
           Event that the component wants to notify the application about.
       @param nData1
           nData will be the OMX_ERRORTYPE for an error event and will be
           an OMX_COMMANDTYPE for a command complete event and OMX_INDEXTYPE for a OMX_PortSettingsChanged event.
        @param nData2
           nData2 will hold further information related to the event. Can be OMX_STATETYPE for
           a OMX_StateChange command or port index for a OMX_PortSettingsCHanged event.
           Default value is 0 if not used. )
       @param pEventData
           Pointer to additional event-specific data (see spec for meaning).
     */
//    virtual OMX_ERRORTYPE event_handler(
//            OMX_IN OMX_HANDLETYPE hComponent,
//            OMX_IN OMX_PTR pAppData,
//            OMX_IN OMX_EVENTTYPE eEvent,
//            OMX_IN OMX_U32 nData1,
//            OMX_IN OMX_U32 nData2,
//            OMX_IN OMX_PTR pEventData);

    /** The empty_buffer_done method is used to return emptied buffers from an
        input port back to the application for reuse.  This is a blocking call
        so the application should not attempt to refill the buffers during this
        call, but should queue them and refill them in another thread.  There
        is no error return, so the application shall handle any errors generated
        internally.

        The application should return from this call within 5 msec.

        @param hComponent
            handle of the component to access.  This is the component
            handle returned by the call to the GetHandle function.
        @param pAppData
            pointer to an application defined value that was provided in the
            pAppData parameter to the OMX_GetHandle method for the component.
            This application defined value is provided so that the application
            can have a component specific context when receiving the callback.
        @param pBuffer
            pointer to an OMX_BUFFERHEADERTYPE structure allocated with UseBuffer
            or AllocateBuffer indicating the buffer that was emptied.
     */
//    virtual OMX_ERRORTYPE empty_buffer_done(
//            OMX_IN OMX_HANDLETYPE hComponent,
//            OMX_IN OMX_PTR pAppData,
//            OMX_IN OMX_BUFFERHEADERTYPE* pBuffer);

    /** The fill_buffer_done method is used to return filled buffers from an
        output port back to the application for emptying and then reuse.
        This is a blocking call so the application should not attempt to
        empty the buffers during this call, but should queue the buffers
        and empty them in another thread.  There is no error return, so
        the application shall handle any errors generated internally.  The
        application shall also update the buffer header to indicate the
        number of bytes placed into the buffer.

        The application should return from this call within 5 msec.

        @param hComponent
            handle of the component to access.  This is the component
            handle returned by the call to the GetHandle function.
        @param pAppData
            pointer to an application defined value that was provided in the
            pAppData parameter to the OMX_GetHandle method for the component.
            This application defined value is provided so that the application
            can have a component specific context when receiving the callback.
        @param pBuffer
            pointer to an OMX_BUFFERHEADERTYPE structure allocated with UseBuffer
            or AllocateBuffer indicating the buffer that was filled.
     */
//    virtual OMX_ERRORTYPE fill_buffer_done(
//            OMX_OUT OMX_HANDLETYPE hComponent,
//            OMX_OUT OMX_PTR pAppData,
//            OMX_OUT OMX_BUFFERHEADERTYPE* pBuffer);

protected:
    OMX_ERRORTYPE load_componet(OMX_HANDLETYPE &pHandler, OMX_STRING cComponentName,
                                OMX_PTR pAppData);

private:

};


#endif //ANDROIDOMX_OMXMASTER_H
