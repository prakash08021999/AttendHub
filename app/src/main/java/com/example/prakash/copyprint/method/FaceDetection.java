package com.example.prakash.copyprint.method;

import android.util.Log;

import com.example.prakash.copyprint.utils.FaceDectectionUtils;
import com.example.prakash.copyprint.utils.ImageUtils;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PRAKASH on 15-09-2018.
 */

public class FaceDetection {
 public static  Mat detectFaces(Mat cameraFrame, Mat image, boolean captureImage){
     MatOfRect faces = null;
     if (FaceDectectionUtils.mNativeDetector != null)
         faces=FaceDectectionUtils.mNativeDetector.detectFaces(image);
     if(faces==null){
         return null;
     }

     Rect[] faceArray=faces.toArray();
     if(faceArray.length == 0){
         return null;
     }
     if (faceArray.length>1){
         return null;
     }
    Log.d("Detect", String.valueOf(faceArray.length));
     Rect rect_Crop=null;
     for (Rect rect : faceArray) {
         rect_Crop = new Rect(rect.x, rect.y, rect.width, rect.height);
     }
     Mat image_roi = new Mat(image,rect_Crop);
     Mat resize_image =new Mat();
     Imgproc.resize(image_roi,resize_image,new Size(100,100));
     if (resize_image !=null){
         return  resize_image;
     }
     return image_roi;
    }
    public static List<Mat> MultiDetection(Mat image){
        List<Mat> face=new ArrayList<Mat>();
        MatOfRect faces = null;
        if (FaceDectectionUtils.mNativeDetector != null)
            faces=FaceDectectionUtils.mNativeDetector.detectFaces(image);
        if(faces==null){
            return null;
        }
        Rect[] facesArray = faces.toArray();
        double maxArea  = 0;
        int maxId = 0;
        for (int i = 0; i < facesArray.length; i++) {
            if(facesArray[i].area()>maxArea)
            {
                maxArea = facesArray[i].area();
            }
            Mat rgba=ImageUtils.getCroppedFace(image, facesArray[i]);
            face.add(rgba);
        }

        return face;
    }
}
