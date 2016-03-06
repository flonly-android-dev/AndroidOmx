LOCAL_PATH := $(call my-dir)
LIBPATH := $(LOCAL_PATH)/lib
INCLUDE_PATH := $(LOCAL_PATH)/include $(LOCAL_PATH)/include/omx $(LOCAL_PATH)

include $(CLEAR_VARS)
LOCAL_MODULE := libOmxCore
LOCAL_SRC_FILES := $(LIBPATH)/libOmxCore.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_C_INCLUDES :=$(LOCAL_PATH)
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/omx
LOCAL_MODULE    := mediacore
LOCAL_SHARED_LIBRARIES := libOmxCore
LOCAL_SRC_FILES := omx/OmxMaster.cpp \
     java_interface/api.cpp
LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)