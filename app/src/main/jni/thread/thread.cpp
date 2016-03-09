#include "thread.h"
#include <memory.h>
#include <sys/prctl.h>
#include <sys/types.h>
#include <unistd.h>
#include "utils/mlog.h"
//#include "jni_mimic.h"
#include <jni.h>
extern JavaVM* gJavaVM;

CPThread::CPThread()
{
    _user = 0;
    _worker = 0;
    _run = false;
    _name = NULL;
    memset(&_thread, 0, sizeof(_thread));
}

CPThread::~CPThread()
{
}

int CPThread::Start(const char* thread_name)
{
	if(thread_name != NULL)
	{
		_name = (char*)thread_name;
	}

    pthread_attr_t attr;
    pthread_attr_init(&attr);
    pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_JOINABLE);
    _run = true;
    int ret = pthread_create(&_thread, &attr, Run, this);
    if (ret != 0) {
        _run = false;
        Stop();
        return -1;
    }
    prctl(PR_SET_NAME, thread_name);

    return 0;
}

void CPThread::Stop()
{
	if(!_run) {
		return;
	}
    _run = false;
    _sem.Post();
    void *status = NULL;
    pthread_join(_thread, &status);
    LOGE("function=CPThread::%s, line=%d, threadname=%s", __FUNCTION__, __LINE__,_name);
}

void* CPThread::Run(void* param)
{
    CPThread* pthread = (CPThread*)param;
    pthread->Process();

    return 0;
}

void CPThread::AddTask(CPThreadWorkProc pWorker, void* pJob)
{
    _worker = pWorker;
    _user = pJob;
    _sem.Post();
}

void CPThread::Process()
{
	LOGE("thread=%s,tid=%d,function=%s, line = %d", _name, gettid(), __FUNCTION__, __LINE__);
	JNIEnv* env;
	jint ret = gJavaVM->AttachCurrentThread(&env, NULL);
	if (ret || env == NULL) {
		LOGE("function=%s, line = %d", __FUNCTION__, __LINE__);
		return;
	}

    while (_run)
    {
        _sem.Pend();
        if (!_run) {
            break;
        }
        if (_worker)
        {
            _worker(_user);
        }
    }

	gJavaVM->DetachCurrentThread();
	LOGE("thread=%s,function=%s, line = %d", _name, __FUNCTION__, __LINE__);
}
