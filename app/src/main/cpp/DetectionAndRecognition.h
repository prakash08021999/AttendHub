//
// Created by PRAKASH on 15-09-2018.
//

#include <jni.h>

#ifndef COPYPRINT_DETECTIONANDRECOGNITION_H
#define COPYPRINT_DETECTIONANDRECOGNITION_H

extern "C" {

/*
 * Class:     org_opencv_samples_fd_DetectionBasedTracker
 * Method:    nativeCreateObject
 * Signature: (Ljava/lang/String;F)J
 */
JNIEXPORT jint JNICALL Java_com_example_prakash_copyprint_FaceRecognitionCamera_faceRecognition
        (JNIEnv *, jclass, jlong , jint );

JNIEXPORT jint JNICALL Java_com_example_prakash_copyprint_DetectionBasedTracker_dector
        (JNIEnv *, jclass,jstring ,jlong,jlong);

//CLAHE algorithum to readuce contrast
JNIEXPORT void JNICALL
Java_com_example_prakash_copyprint_method_FaceCrop_createClahe(JNIEnv *, jclass ,
                                                               jlong , jlong );
#ifdef __cplusplus
}
#endif
#endif

