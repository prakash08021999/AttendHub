//
// Created by PRAKASH on 16-09-2018.
//

#include "opencv2/core/core.hpp"
#include "opencv2/contrib/contrib.hpp"
#include "opencv2/highgui/highgui.hpp"

#include <iostream>
#include <fstream>
#include <sstream>
#include <android/log.h>

using namespace cv;
using namespace std;

#include "lbph.h"
int lbpRecognize(vector<Mat> images, vector<int> labels, Mat testSample) {

    // These vectors hold the images and corresponding labels.

    // Read in the data. This can fail if no valid
    // input filename is given.

    if(images.size() == 0){
        return -1;
    }
    int height = ((Mat)images[0]).rows;
    Ptr<FaceRecognizer> model = createLBPHFaceRecognizer();
    model->train(images, labels);
    // The following line predicts the label of a given
    // test image:
    int predictedLabel = -1;
    double confidence = 0.0;
    model->predict(testSample, predictedLabel, confidence);
    LOGD("--- confidence: %f", confidence);
    if ( confidence<80)
    {
        return predictedLabel;
    }
    return -1;

}