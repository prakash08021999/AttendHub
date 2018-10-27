package com.example.prakash.copyprint.method;

import android.content.Context;
import org.opencv.core.Size;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.net.ContentHandler;

/**
 * Created by PRAKASH on 24-09-2018.
 */

public class FaceCrop {
    Context mContext;
    int absoluteFaceSize=0;
    float mRelativeFaceSize=0.2f;
    static MatOfRect faces = new MatOfRect();
    CascadeClassifier cascadeClassifier;
    public FaceCrop(String mCasCadeFile){
        cascadeClassifier=new CascadeClassifier(mCasCadeFile);
    }
    public MatOfRect detectFaces(Mat image){
        if (absoluteFaceSize == 0) {
            int height = image.height();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                absoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }
        // Use the classifier to detect faces
        Mat grayscaleImage=new Mat(image.rows(),image.rows(),image.type());
        Imgproc.cvtColor(image,grayscaleImage,Imgproc.COLOR_BGR2GRAY);

        Mat claheMat=new Mat(grayscaleImage.rows(),grayscaleImage.cols(),grayscaleImage.type());
        createClahe(grayscaleImage.getNativeObjAddr(),claheMat.getNativeObjAddr());

        if (cascadeClassifier !=null){
            cascadeClassifier.detectMultiScale(claheMat, faces, 1.2, 5, 2,
                    new  Size(absoluteFaceSize,absoluteFaceSize),new Size());
        }
       return faces;

    }
    public MatOfRect eyes(Mat image){
        MatOfRect eye=new MatOfRect();
        if (cascadeClassifier !=null){
            cascadeClassifier.detectMultiScale(image,eye,1.1,2,2,new Size(30,30),new Size());
        }
        return eye;
    }
    public static native void createClahe(long image,long result);

}
