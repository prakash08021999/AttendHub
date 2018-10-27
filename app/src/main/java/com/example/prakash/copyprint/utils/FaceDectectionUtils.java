package com.example.prakash.copyprint.utils;

import android.content.Context;

import com.example.prakash.copyprint.R;
import com.example.prakash.copyprint.method.FaceCrop;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by PRAKASH on 15-09-2018.
 */

public class FaceDectectionUtils {
    private static File mCascadeFile;
    private static File mCascadeLeftEyeFile;
    private static File mCascadeRightEyeFile;
    public static FaceCrop mNativeDetector;

    public static boolean cascadeFilesLoaded = false;
    private static Context mContext;


    public static FaceCrop  mNativeLeftEyeDetector;
    public static FaceCrop  mNativeRightEyeDetector;

    public static BaseLoaderCallback mLoaderCallback;

    public static void initionalize(Context context){
        mContext=context;
      //  faceDataSource=new FaceDataSource(context);
        mLoaderCallback =new BaseLoaderCallback(context) {
            @Override
            public void onManagerConnected(int status) {
                switch (status)
                    {
                        case LoaderCallbackInterface.SUCCESS:
                            {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        System.loadLibrary("native_camera_r2.2.0");
                                        System.loadLibrary("native_camera_r2.3.3");
                                        System.loadLibrary("native_camera_r3.0.1");
                                        System.loadLibrary("native_camera_r4.0.0");
                                        System.loadLibrary("native_camera_r4.0.3");
                                        System.loadLibrary("native_camera_r4.1.1");
                                        System.loadLibrary("native_camera_r4.2.0");
                                        System.loadLibrary("opencv_java");

                                        loadCascadeFiles();
                                        cascadeFilesLoaded = true;
                                    }
                                }).start();
                            } break;
                        default:
                        {
                            super.onManagerConnected(status);
                        } break;

                    }
            }

            @Override
            public void onPackageInstall(int operation, InstallCallbackInterface callback) {
                System.out.println("---------------------------------------- onPackageInstall");
                System.loadLibrary("opencv_java");
                loadCascadeFiles();
                cascadeFilesLoaded = true;
                System.out.println("---------------------------------------- ONNNNNNNNNNNNNNNNNNNNNNNNNNN");
            }
        };
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, context.getApplicationContext(), mLoaderCallback);
    }
    private static void loadCascadeFiles(){
        try{
        InputStream is=mContext.getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
        File mCascadeDir=mContext.getDir("cascade",Context.MODE_PRIVATE);
        mCascadeFile=new File(mCascadeDir,"haarcascade_frontalface_alt.xml");
        FileOutputStream os = new FileOutputStream(mCascadeFile);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead=is.read(buffer))!=-1){
            os.write(buffer, 0, bytesRead);
        }
            is.close();
            os.close();
           mNativeDetector = new FaceCrop(mCascadeFile.getAbsolutePath());
            mCascadeDir.delete();


            ////////////////////////////////////////////////////
            InputStream isLeftEye = mContext.getResources().openRawResource(R.raw.ojoleft);
            File cascadeDirLeftEye = mContext.getDir("cascadelefteye", Context.MODE_PRIVATE);
            mCascadeLeftEyeFile = new File(cascadeDirLeftEye, "haarcascade_mcs_lefteye.xml");
            FileOutputStream osLeftEye = new FileOutputStream(mCascadeLeftEyeFile);

            byte[] bufferLeftEye = new byte[4096];
            int bytesReadLeftEye;
            while ((bytesReadLeftEye = isLeftEye.read(bufferLeftEye)) != -1) {
                osLeftEye.write(bufferLeftEye, 0, bytesReadLeftEye);
            }
            isLeftEye.close();
            osLeftEye.close();

            mNativeLeftEyeDetector = new FaceCrop(mCascadeLeftEyeFile.getAbsolutePath());

            cascadeDirLeftEye.delete();
            /////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////
            InputStream isRightEye = mContext.getResources().openRawResource(R.raw.ojoright);
            File cascadeDirRightEye = mContext.getDir("cascaderighteye", Context.MODE_PRIVATE);
            mCascadeRightEyeFile = new File(cascadeDirRightEye, "haarcascade_mcs_righteye.xml");
            FileOutputStream osRightEye = new FileOutputStream(mCascadeRightEyeFile);

            byte[] bufferRightEye = new byte[4096];
            int bytesReadRightEye;
            while ((bytesReadRightEye = isRightEye.read(bufferRightEye)) != -1) {
                osRightEye.write(bufferRightEye, 0, bytesReadRightEye);
            }
            isRightEye.close();
            osRightEye.close();

            mNativeRightEyeDetector = new FaceCrop(mCascadeRightEyeFile.getAbsolutePath());

            cascadeDirRightEye.delete();
      }
    catch(IOException e){
        e.printStackTrace();
    }
    }
}


