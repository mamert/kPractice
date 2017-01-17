#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG    "jni_blargle"
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__))

extern "C"
jstring
Java_bloody_hell_kpractice_things_jni_JniMainFrag_testCallback1(
        JNIEnv *env,
        jobject /* this */,
        jobject callback) {

//    jclass cls = env->FindClass("bloody/hell/kpractice/things/jni/JniMainFrag");
    jclass cls = env->GetObjectClass(callback);
    jmethodID methodid = env->GetMethodID(cls, "simpleJniCallback", "(Ljava/lang/String;)V"); // more on signature: http://www.rgagnon.com/javadetails/java-0286.html
    LOGI("methodid=%d",(methodid ? 1 : 0));
    if(!methodid) {
        return env->NewStringUTF("Method not found");
    }
    jstring jstr = env->NewStringUTF("Yay callbacks via void methods!");
    env->CallVoidMethod(callback, methodid, jstr);

    std::string hello = "Success, maybe?";
    return env->NewStringUTF(hello.c_str());
}
