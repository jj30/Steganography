#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_stega_jj_bldg5_steganography_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
