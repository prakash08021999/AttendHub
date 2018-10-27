//
// Created by PRAKASH on 03-10-2018.
//
//
// Created by PRAKASH on 15-09-2018.
//

#include <jni.h>
#include <string>
#include <opencv2/contrib/detection_based_tracker.hpp>
#include <android/log.h>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include "DetectionAndRecognition.h"
#include "lbph.h"

using namespace std;
using namespace cv;
#define LOG_TAG "DetectionAndRecognition"
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))


void saveImage(Mat image, const char * path){
    bool data = imwrite(path, image);
    LOGD("------------------------------------------- saved: %d written: %d",data);
}

JNIEXPORT jint JNICALL Java_com_example_prakash_copyprint_FaceRecognitionCamera_faceRecognition
        (JNIEnv *jenv, jclass clazz, jlong sample, jint size ){
    try
    {
        jclass clazz = jenv->FindClass("com/example/prakash/copyprint/FaceRecognitionCamera");
        jmethodID getFaceFolder = jenv->GetStaticMethodID(clazz, "getPersonFolder","(I)Ljava/lang/String;");
        jmethodID getPersonID = jenv->GetStaticMethodID(clazz, "getPersonID","(I)J");

        vector<Mat> images;
        vector<int> labels;
        Ptr<CLAHE> clahe=createCLAHE();
        clahe->setClipLimit(4);
        for(int i = 0; i<size; i++){
            jstring folderName = (jstring)jenv->CallStaticObjectMethod(clazz, getFaceFolder, (jint)i);
            jboolean isCopy;
            const char *savePath=jenv->GetStringUTFChars(folderName, &isCopy);
            jlong id = jenv->CallStaticLongMethod(clazz, getPersonID, (jint)i);
            std::ostringstream stringstream;
            for(int j = 1; j < 4; j++) {
                stringstream << savePath;
                stringstream << "/";
                stringstream << j;
                stringstream << ".jpg";
                Mat greymat, colormat;
                colormat = imread(stringstream.str().c_str());
                if(colormat.data) {
                    LOGD("---------------------------------------------- image Loaded");
                    cvtColor(colormat, greymat, CV_BGR2GRAY);
                    clahe->apply(greymat,greymat);
                    images.push_back(greymat);
                    labels.push_back((int)id);
                }
                stringstream.clear();

            }
        }
        vector<Mat> histograms;
        Mat greymat;
        cvtColor(*((Mat*)sample), greymat, CV_BGR2GRAY);
        clahe->apply(greymat,greymat);
        jint lbpResult = lbpRecognize(images, labels,greymat);
        return lbpResult;
    }
    catch(cv::Exception& e)
    {
        LOGD("nativeCreateObject caught cv::Exception: %s", e.what());
        jclass je = jenv->FindClass("org/opencv/core/CvException");
        if(!je)
            je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, e.what());
        return -1;
    }
    catch (...)
    {
        LOGD("nativeDetect caught unknown exception");
        jclass je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, "Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
        return - 1;
    }
    LOGD("Java_com_yaylas_sytech_facerecognizer_DetectionBasedTracker_nativeDetect exit");
    return - 1;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_prakash_copyprint_method_FaceCrop_createClahe(JNIEnv *env, jclass type,
                                                               jlong image, jlong result) {

    Mat img=*(Mat*)image;
    Mat dest=*(Mat*)result;

    Ptr<CLAHE> clahe=createCLAHE();
    clahe->setClipLimit(4);

    clahe->apply(img,dest);



}extern "C"
JNIEXPORT void JNICALL
Java_com_example_prakash_copyprint_utils_ImageUtils_saveImageAsPGM(JNIEnv *jenv, jclass type,
                                                                   jstring path,
                                                                   jlong imageReference) {

    jboolean isCopy;
    const char *savePath = jenv->GetStringUTFChars(path, &isCopy);
    Mat image = *((Mat*)imageReference);
    saveImage(image, savePath);

}
