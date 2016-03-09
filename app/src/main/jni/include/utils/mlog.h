//
// Created by flonly on 2016/3/5.
//

#ifndef ANDROIDOMX_LOG_H_H
#define ANDROIDOMX_LOG_H_H
#include <android/log.h>

#define LOG_TAG   "libplasma"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#endif //ANDROIDOMX_LOG_H_H
