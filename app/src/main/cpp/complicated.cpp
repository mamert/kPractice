#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG    "jni_blargle"
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__))
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__))


jstring invokeSimpleCallback(
        JNIEnv *env,
        jobject callback,
        jstring message){ // more on types: http://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/types.html

//    jclass cls = env->FindClass("bloody/hell/kpractice/things/jni/JniMainFrag");
    jclass cls = env->GetObjectClass(callback);
    jmethodID methodid = env->GetMethodID(cls, "simpleJniCallback", "(Ljava/lang/String;)V"); // more on signature: http://www.rgagnon.com/javadetails/java-0286.html
    LOGI("methodid=%d",(methodid ? 1 : 0));
    if(!methodid) {
        return env->NewStringUTF("Method not found");
    }
    env->CallVoidMethod(callback, methodid, message); // more on method calls: http://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/functions.html

    std::string hello = "Success, maybe?";
    return env->NewStringUTF(hello.c_str());
}

jstring invokeSimpleCallback(
        JNIEnv *env,
        jobject callback,
        char* message){
    return invokeSimpleCallback(env, callback, env->NewStringUTF(message));
}


extern "C"
jstring
Java_bloody_hell_kpractice_things_jni_JniMainFrag_testCallback1(
        JNIEnv *env,
        jobject /* this */,
        jobject callback) {
    return invokeSimpleCallback(env, callback, "Yay callbacks via void methods!");
}



extern "C"
void
Java_bloody_hell_kpractice_things_jni_JniMainFrag_testCallback2(
        JNIEnv *env,
        jobject,
        jobject callback) {

    jclass cls = env->GetObjectClass(callback);
    jmethodID methodid = env->GetMethodID(cls, "anotherJniCallback", "(Ljava/lang/String;)Ljava/lang/String;");

    jstring strFromCallback = methodid ?
                              (jstring) env->CallObjectMethod(callback, methodid, env->NewStringUTF("ping:")):
                              env->NewStringUTF("Method not found");
    invokeSimpleCallback(env, callback, strFromCallback);
}