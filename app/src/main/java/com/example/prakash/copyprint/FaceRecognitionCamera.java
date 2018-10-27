package com.example.prakash.copyprint;

/**
 * Created by PRAKASH on 25-09-2018.
 */

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.support.design.widget.FloatingActionButton;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


import com.example.prakash.copyprint.all_dialogcollection.AddSubjectDialog;
import com.example.prakash.copyprint.database.Depart;
import com.example.prakash.copyprint.database.Lecture;
import com.example.prakash.copyprint.database.LectureC;
import com.example.prakash.copyprint.database.Student;
import com.example.prakash.copyprint.database.StudentCRUD;
import com.example.prakash.copyprint.database.Techer;
import com.example.prakash.copyprint.method.FaceDetection;
import com.example.prakash.copyprint.recognize.AttendenceList;
import com.example.prakash.copyprint.utils.FaceDectectionUtils;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;


import java.util.Vector;

/**
 * Created by PRAKASH on 20-09-2018.
 */

public class FaceRecognitionCamera extends AppCompatActivity implements View.OnClickListener
{
    //FaceRecognision code
    ProgressDialog pd;
    public static Vector<Student> students=new Vector<Student>();
    Mat faceMat;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    public static ArrayList<String> alName =new ArrayList<String>();
    public static  ArrayList<Bitmap> alImage = new ArrayList<Bitmap>();
    public static  ArrayList<Integer> alId=new ArrayList<Integer>();
    public  static AttendenceList mAdapter;
    StudentCRUD helper;
    FloatingActionButton submitAttendence;
    String cDate;
    String Subject;
    int totalLect;
    String cTime;
    String cClass;
    AlertDialog.Builder GlobalalertDialog;
    boolean b;



    private static SparseIntArray ORIENTATION1 =new SparseIntArray();
    static {
        ORIENTATION1.append(Surface.ROTATION_0,90);
        ORIENTATION1.append(Surface.ROTATION_90,0);
        ORIENTATION1.append(Surface.ROTATION_180,270);
        ORIENTATION1.append(Surface.ROTATION_270,180);

    }
    int totalRotation;
    FloatingActionButton capture;
    private static final int REQUEST_CAMERA_PERMISSION_RESULT=0;
    private String mCameraId;
    private HandlerThread mBackgroungHandlerThread;
    private Handler mBackgroundHandler;
    private Size mPreviewSize ;
    private Size mImageSize;
    private ImageReader mImageReader;
    private CameraCaptureSession mPreviewCaptureSession;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    public  static MyOnClickListener myOnClickListener;



    private static SparseIntArray ORIENTATION =new SparseIntArray();
    static {
        ORIENTATION.append(Surface.ROTATION_0,0);
        ORIENTATION.append(Surface.ROTATION_90,90);
        ORIENTATION.append(Surface.ROTATION_180,180);
        ORIENTATION.append(Surface.ROTATION_270,270);

    }
    @Override
    protected void onCreate(Bundle SaveInstanceState){
        super.onCreate(SaveInstanceState);
        setContentView(R.layout.face_recognition_attendence);
        StudentCRUD studentCRUD=new StudentCRUD(getApplicationContext());
        try {
            studentCRUD.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(studentCRUD.getTotalStudent()==0){
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Please Insert Student Details..")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(FaceRecognitionCamera.this,MainActivity.class));
                        }
                    }).show();
        }
        studentCRUD.close();
        init();
        this.Subject= Lecture.subjects;
        this.totalLect=Lecture.totalLectuer;
        this.cTime=Lecture.time;
        this.cClass=Lecture.studentClass;
        b=checkDetail(cClass);
        if (!b)
        {
            GlobalalertDialog = new AlertDialog.Builder(FaceRecognitionCamera.this)
                    .setTitle("Alert Box")
                    .setMessage("There is no Student belong to this class")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            GlobalalertDialog.show();
        }
        myOnClickListener = new MyOnClickListener(this);
        mTextureView =findViewById(R.id.textureView2);
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        submitAttendence=findViewById(R.id.submitAttendence);
        submitAttendence.setOnClickListener(this);
        recyclerView=findViewById(R.id.attendence);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        helper =new StudentCRUD(getApplicationContext());
        capture=findViewById(R.id.recognize);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tackphoto();
            }
        });

    }

    public boolean checkDetail(String sClass){
        helper=new StudentCRUD(getApplicationContext());
        try {
            helper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (cClass!=null){
            students = helper.getAllStudentCondition(cClass);
        }

        helper.close();
        if (students.size()==0){
            return false;
        }
        return true;
    }

    public void init(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        cDate = df.format(c);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.submitAttendence:
                if(alName==null){
                    AlertDialog alertDialog = new AlertDialog.Builder(FaceRecognitionCamera.this)
                            //set icon
                            .setIcon(android.R.drawable.ic_dialog_alert)

                            //set title
                            .setTitle("Attendence Error")
                            .setMessage("Please atlease take attendence").show();
                    return;

                }
                for (String name: alName){
                    Log.d("Attendence",name);
                }
                LectureC lectureC=new LectureC(this);
                try {
                    lectureC.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                int lecid=lectureC.getId(Subject);
                lectureC.createLecture(cDate,cTime,totalLect,lecid);
                int Lecid =lectureC.lecId();
                lectureC.close();
                for(int ids:alId){
                    LectureC AttendenceLec =new LectureC(this);
                    try {
                        AttendenceLec.open();
                        AttendenceLec.createAttendence(Lecid,id);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    finally {
                        AttendenceLec.close();
                    }
                }
                Log.d("Attendence:","attendence is done");
                recyclerView.setAdapter(null);
                alId.clear();
                alImage.clear();
                alName.clear();
                break;
        }
    }

    public static String getPersonFolder(int id)
    {
        return students.get(id).getFaceUri();
    }

    public static long getPersonID(int index)
    {
        return students.get(index).getStudentId();
    }


    private  void recognize(final int result){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<students.size();i++){
                    final int id = students.get(i).getStudentId();
                    if (result==id){
                        final int index = i;
                        if(!alId.contains(id)){
                            alId.add(result);
                            FaceRecognitionCamera.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Mat m = Highgui.imread(students.get(index).getFaceUri()+"/0.jpg");
                                    final Bitmap bmp = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.RGB_565);
                                    Utils.matToBitmap(m, bmp);
                                    alName.add( students.get(index).getStudentName());
                                    alImage.add(bmp);
                                    mAdapter = new AttendenceList(getApplicationContext(), alName, alImage);
                                    recyclerView.setAdapter(mAdapter);
                                }
                            });
                        }
                    }
                }
            }}).start();

    }


    //This function takephoto on click of button
    protected void tackphoto(){
        if (mCameraDevice==null){
            Log.e("Camera","CameraDevice not Connected");
            return;
        }
        CameraManager manager =(CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics=manager.getCameraCharacteristics(mCameraId);
            Size[] jpegSize=null;
            if (characteristics !=null){
                jpegSize=characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width =640;
            int height =480;
            if (jpegSize !=null && jpegSize.length > 0){
                width=jpegSize[0].getWidth();
                height=jpegSize[0].getHeight();
                mImageReader =ImageReader.newInstance(width,height,ImageFormat.JPEG,1);
                List<Surface> outputSurface =new ArrayList<Surface>(2);
                outputSurface.add(mImageReader.getSurface());
                outputSurface.add(new Surface(mTextureView.getSurfaceTexture()));
                final CaptureRequest.Builder mCaptureRequestBuilder=mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                mCaptureRequestBuilder.addTarget(mImageReader.getSurface());
                mCaptureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

                //Orientation
                int rotation =getWindowManager().getDefaultDisplay().getRotation();
                mCaptureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION,ORIENTATION1.get(rotation));
                ImageReader.OnImageAvailableListener readerListener=new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader imageReader) {
                        Image image =null;
                        image=mImageReader.acquireLatestImage();
                        ByteBuffer buffer=image.getPlanes()[0].getBuffer();
                        byte[] bytes=new byte[buffer.capacity()];
                        buffer.get(bytes);
                        Bitmap btm= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        Mat capturedMat=new Mat(btm.getWidth(),btm.getHeight(), CvType.CV_8UC1);
                        Utils.bitmapToMat(btm,capturedMat);
                        faceMat = FaceDetection.detectFaces(null,capturedMat,true);
                      /* if (faceMat== null){
                            Log.d("Face Not Detected","Take Photo again");
                            Toast.makeText(getApplicationContext(),"Take Photo again",Toast.LENGTH_SHORT).show();


                        }else {
                            if(students.size()>0){
                               // progressDoalog.setMessage("FaceRecognition UnderProgress");
                               // progressDoalog.show();
                                int result = faceRecognition(faceMat.getNativeObjAddr(), students.size());
                                Log.d("confidencess",String.valueOf(result)+"  length "+students.size());
                                Toast.makeText(getApplicationContext(),"confidence is"+ result,Toast.LENGTH_SHORT).show();
                                recognize(result);
                                //progressDoalog.dismiss();
                            }
                            else{
                                Toast.makeText(getApplicationContext()," There Are no student ",Toast.LENGTH_SHORT).show();

                            }


                        }*/
                      manyStudent(capturedMat);

                    }

                };
                mImageReader.setOnImageAvailableListener(readerListener,mBackgroundHandler);
                final CameraCaptureSession.CaptureCallback captureCallback=new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                        super.onCaptureCompleted(session, request, result);
                        startPreview();
                    }
                };
                mCameraDevice.createCaptureSession(outputSurface, new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                        try {
                            cameraCaptureSession.capture(mCaptureRequestBuilder.build(),captureCallback,mBackgroundHandler);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                    }
                },mBackgroundHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    public void manyStudent(Mat capturedMat)
    {
        List<Mat> face = FaceDetection.MultiDetection(capturedMat);
        if (face== null){
            Log.d("Face Not Detected","Take Photo again");
            Toast.makeText(getApplicationContext(),"Take Photo again",Toast.LENGTH_SHORT).show();
        }else {

            if(students.size()>0){
                for (int i=0;i<face.size();i++){
                int result = faceRecognition(face.get(i).getNativeObjAddr(), students.size());
                Log.d("confidencess",String.valueOf(result)+"  length "+students.size());
                Toast.makeText(getApplicationContext(),"confidence is"+ result,Toast.LENGTH_SHORT).show();
                recognize(result);
                }
            }
            else{
                Toast.makeText(getApplicationContext()," There Are No Student ",Toast.LENGTH_SHORT).show();
            }



        }


    }





    /////////////////////////////////////


    private TextureView mTextureView;
    private TextureView.SurfaceTextureListener  mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            setUpCamera(i,i1);
            connectCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };


//Cmaera device to open camera
    private CameraDevice mCameraDevice;
    CameraDevice.StateCallback mCameraStateCallBack =new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraDevice=cameraDevice;
            startPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
            mCameraDevice=null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            mCameraDevice=null;
        }
    };


    //This code compare size
    private static class CompareSizeByArea implements Comparator<Size> {
        @Override
        public int compare(Size size, Size t1) {
            return Long.signum((long)size.getWidth()+size.getHeight()/
                    (long)t1.getWidth()+t1.getHeight());
        }
    }


    //This code to get proper camera
    private void setUpCamera(int width ,int height){
        CameraManager cameraManager=(CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try {
            for(String cameraId : cameraManager.getCameraIdList()){
                CameraCharacteristics cameraCharacteristics=cameraManager.getCameraCharacteristics(cameraId);
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)==
                        CameraCharacteristics.LENS_FACING_FRONT){
                    continue;
                }
                StreamConfigurationMap map=cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                int deviceOrientation =getWindowManager().getDefaultDisplay().getRotation();
                totalRotation=sensorToDeviceRotation(cameraCharacteristics,deviceOrientation);
                boolean swapRotation= totalRotation==90 || totalRotation==270;
                int rotatedWidth=width;
                int rotatedHeight=height;
                if (swapRotation){
                    rotatedHeight=width;
                    rotatedWidth=height;
                }
                mPreviewSize=chooseOptionalSize(map.getOutputSizes(SurfaceTexture.class),rotatedWidth,rotatedHeight);
                mImageSize=chooseOptionalSize(map.getOutputSizes(ImageFormat.JPEG),rotatedWidth,rotatedHeight);
                mCameraId=cameraId;
                return;

            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }


    //This code connect the camera
    private  void connectCamera(){
        CameraManager cameraManager=(CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try {
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                    cameraManager.openCamera(mCameraId,mCameraStateCallBack,mBackgroundHandler);
                }
                else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                        Toast.makeText(getApplicationContext(),"App required access to camera",Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA_PERMISSION_RESULT);
                }
            }
            else {
                cameraManager.openCamera(mCameraId,mCameraStateCallBack,mBackgroundHandler);
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    //This code start background thread
    private void startBackGroundThread(){
        mBackgroungHandlerThread=new HandlerThread("Camera2Api");
        mBackgroungHandlerThread.start();
        mBackgroundHandler=new Handler(mBackgroungHandlerThread.getLooper());
    }


    //This code stop background thead
    private void stopBackGroundThread(){
        mBackgroungHandlerThread.quitSafely();
        try {
            mBackgroungHandlerThread.join();
            mBackgroungHandlerThread=null;
            mBackgroundHandler=null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //This code give sensor take priview rotation
    private  static int sensorToDeviceRotation(CameraCharacteristics cameraCharacteristics,int deviceOrientation){
        int sensorOrientation=cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        deviceOrientation =ORIENTATION.get(deviceOrientation);
        return (sensorOrientation + deviceOrientation + 360) % 360;
    }

    //This give the sixe of screen
    private static Size chooseOptionalSize(Size[] choose,int width,int height){
        List<Size> bigEnough =new ArrayList<Size>();
        for(Size option: choose){
            if(option.getHeight()==option.getWidth() * height /width &&
                    option.getWidth() >= width && option.getHeight() >= height){
                bigEnough.add(option);
            }
        }
        if(bigEnough.size()>0){
            return Collections.min(bigEnough,new CompareSizeByArea());
        }
        else{
            return choose[0];
        }
    }



    //this code disconnect the camera device and Image reader for no futher connection and image reading
    private void closeCamera(){
        if(mCameraDevice !=null){
            mCameraDevice.close();
            mCameraDevice=null;
        }
        if(mImageReader != null){
            mImageReader.close();
            mImageReader=null;
        }
    }


    //This function gives preview
    private  void startPreview(){
        SurfaceTexture surfaceTexture=mTextureView.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(),mPreviewSize.getHeight());
        Surface mPreviewSurface=new Surface(surfaceTexture);
        try {
            mCaptureRequestBuilder=mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mCaptureRequestBuilder.addTarget(mPreviewSurface);
            mCameraDevice.createCaptureSession(Arrays.asList(mPreviewSurface),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            mPreviewCaptureSession =cameraCaptureSession;

                            try {
                                mPreviewCaptureSession.setRepeatingRequest(mCaptureRequestBuilder.build(),null,mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                            Toast.makeText(getApplicationContext(),"Unable to setup capture preview",Toast.LENGTH_SHORT).show();
                        }
                    },null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    //this function reconnect the camera and restart the background thread
    @Override
    protected void onResume(){
        super.onResume();
        startBackGroundThread();
        if (mTextureView.isAvailable()){
            setUpCamera(mTextureView.getWidth(),mTextureView.getHeight());
            connectCamera();
        }
        else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }


    //Close The Camera Connection and stopbackground thread
    @Override
    protected void onPause(){
        closeCamera();
        stopBackGroundThread();
        super.onPause();
    }



    //native function calling
    private static native int faceRecognition(long inputImage, int personCount);


    //onClick listener for recyclear view
    private static class MyOnClickListener implements View.OnClickListener{

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            removeItem(view);
        }
        private void removeItem(View v) {
            int selectedItemPosition = recyclerView.getChildPosition(v);
            alId.remove(selectedItemPosition);
            alImage.remove(selectedItemPosition);
            alName.remove(selectedItemPosition);
            mAdapter.notifyItemRemoved(selectedItemPosition);
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            recyclerView.removeAllViews();
            alId.clear();
            alImage.clear();
            alName.clear();
            finish();
            startActivity(new Intent(FaceRecognitionCamera.this,MainActivity.class));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}

