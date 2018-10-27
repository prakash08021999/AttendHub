//
// Created by PRAKASH on 16-09-2018.
//

#ifndef COPYPRINT_LBPH_H
#define COPYPRINT_LBPH_H

#include "opencv2/core/core.hpp"
using namespace std;
using namespace cv;
int lbpRecognize(vector<Mat> images, vector<int> labels, Mat testSample);

#define LOG_TAG "DetectionAndRecognition"
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))
#endif //COPYPRINT_LBPH_H
