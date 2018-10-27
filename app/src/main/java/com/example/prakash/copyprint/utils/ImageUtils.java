package com.example.prakash.copyprint.utils;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Point;

/**
 * Created by PRAKASH on 15-09-2018.
 */

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageUtils {

   public  Mat detectEdges(Mat image){

       Mat edges=new Mat(image.size(),CvType.CV_8UC1);
       Imgproc.Canny(image,edges,80,100);
       /*
        Mat image – 8-bit input image
        Mat edges – output edge map; single channels 8-bit image, which has the same size as image
        double treshold1– first threshold for the hysteresis procedure
        dobule treshold2– second threshold for the hysteresis procedure*/
       return  edges;
   }
   public static Mat bitToMat(Bitmap bitmap){
       Mat rgba=new Mat();
       Utils.bitmapToMat(bitmap,rgba);
       return rgba;
   }
   public static Mat cropFace(){
      return null;
   }
    public static native void saveImageAsPGM(String savingPath, long image);

    private static Size mFaceSize  = new Size(200,200);
    public static Mat getCroppedFace(Mat img, Rect rect)
    {
        Mat croppedimage = img.submat(rect);
        Mat resizedimage = new Mat();
        Imgproc.resize(croppedimage, resizedimage, mFaceSize);
        return resizedimage;
    }

}
