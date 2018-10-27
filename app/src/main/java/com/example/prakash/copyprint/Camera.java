package com.example.prakash.copyprint;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.prakash.copyprint.database.StudentCRUD;
import com.example.prakash.copyprint.database.Techer;
import com.example.prakash.copyprint.method.FaceDetection;
import com.example.prakash.copyprint.utils.ImageUtils;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;

/**
 * Created by PRAKASH on 20-09-2018.
 */


public class Camera extends AppCompatActivity implements View.OnClickListener {
    ProgressDialog progressDoalog;
    Mat m;
    public static StudentCRUD studentCRUD;
    EditText name;
    Spinner classes;
    FloatingActionButton studentdetails, capturing, save, cancle;
    ImageView photo;
    private ProgressDialog progress;
    Bitmap btm;


    private HashMap<Integer, Mat> capturedMats = new HashMap<Integer, Mat>();
    Mat faceMat = null;
    Bitmap i1;

    int totalRotation;



    private static final int STATE_PREVIEW = 0;
    private String mCameraId;
    private HandlerThread mBackgroungHandlerThread;
    private Handler mBackgroundHandler;
    private Size mPreviewSize;
    private Size mImageSize;
    private ImageReader mImageReader;
    private CameraCaptureSession mPreviewCaptureSession;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private static SparseIntArray ORIENTATION = new SparseIntArray();

    static {
        ORIENTATION.append(Surface.ROTATION_0, 0);
        ORIENTATION.append(Surface.ROTATION_90, 90);
        ORIENTATION.append(Surface.ROTATION_180, 180);
        ORIENTATION.append(Surface.ROTATION_270, 270);

    }

    private static SparseIntArray ORIENTATION1 = new SparseIntArray();

    static {
        ORIENTATION1.append(Surface.ROTATION_0, 90);
        ORIENTATION1.append(Surface.ROTATION_90, 0);
        ORIENTATION1.append(Surface.ROTATION_180, 270);
        ORIENTATION1.append(Surface.ROTATION_270, 180);

    }

    @Override
    protected void onCreate(Bundle SaveInstanceState) {
        super.onCreate(SaveInstanceState);
        setContentView(R.layout.camera);
        init();
        setListeners();


        List<String> categories = new ArrayList<String>();
        categories.add("FY");
        categories.add("SY");
        categories.add("TY");
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, categories);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classes.setAdapter(spinnerArrayAdapter);


        mTextureView = findViewById(R.id.textureView);
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        capturing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tackphoto();
            }
        });

    }

    // Set Listeners
    private void setListeners() {
        capturing.setOnClickListener(this);
        save.setOnClickListener(this);
        cancle.setOnClickListener(this);
        studentdetails.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.studentdetail: {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (name.getVisibility() == View.INVISIBLE) {
                            name.setVisibility(View.VISIBLE);
                            classes.setVisibility(View.VISIBLE);
                        } else if (name.getVisibility() == View.VISIBLE) {
                            name.setVisibility(View.INVISIBLE);
                            classes.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                break;
            }
            case R.id.Save: {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Pattern Reguname=Pattern.compile("^[A-Za-z\\s]+$");

                        String studentName = name.getText().toString();
                        String studentClass = classes.getSelectedItem().toString();
                        Matcher name1=Reguname.matcher(studentName);
                        if (studentName.equals("") && !name1.find() ){
                            AlertDialog alertDialog = new AlertDialog.Builder(Camera.this)
                                    //set icon
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    //set title
                                    .setTitle("Please Enter Name Correctly").show();
                            return;
                        }
                        studentCRUD = new StudentCRUD(getApplicationContext());
                        try {
                            studentCRUD.open();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        //for storing face folder with name as there id
                        long id = studentCRUD.lastData();
                        Log.d("total", String.valueOf(id));

                        // Find the SD Card path
                        File filepath = Environment.getExternalStorageDirectory();

                        // Create a new folder in SD Card
                        File dir = new File(filepath.getAbsolutePath()
                                + "/.SY_TY/" + (id+1));
                        dir.mkdirs();

                        //Propear Naming
                        final String nameToSave = studentName;
                        final String pathToSave = dir.getAbsolutePath();
                        studentCRUD.createPerson(studentName, studentClass, pathToSave, Techer.DepId);
                        studentCRUD.close();

                        for (int i = 0; i < capturedMats.size(); i++) {
                            if (capturedMats.containsKey(i)) {
                                Bitmap bitmap = Bitmap.createBitmap(capturedMats.get(i).width(), capturedMats.get(i).height(), Bitmap.Config.ARGB_8888);
                                OutputStream outputStream;
                                Utils.matToBitmap(capturedMats.get(i), bitmap);

                                //Create a name for the saved image
                                // Create a name for the saved image
                                File file = new File(dir, i + ".jpg");
                                Log.d("savePath", file.getAbsolutePath());
                                try {

                                    outputStream = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }
                        //This Code is use to set visiblity of the viwes
                        mTextureView.setVisibility(View.VISIBLE);
                        capturing.setVisibility(View.VISIBLE);
                        cancle.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.INVISIBLE);
                        name.setText("");
                        capturedMats.clear();
                    }
                });
                break;
            }
            case R.id.Cancle: {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        capturedMats.clear();
                        mTextureView.setVisibility(View.VISIBLE);
                        capturing.setVisibility(View.VISIBLE);
                        cancle.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.INVISIBLE);
                    }
                });
                break;
            }
        }

    }


    private TextureView mTextureView;
    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            setUpCamera(i, i1);
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

    private CameraDevice mCameraDevice;
    CameraDevice.StateCallback mCameraStateCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            startPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            mCameraDevice = null;
        }
    };

    private static class CompareSizeByArea implements Comparator<Size> {
        @Override
        public int compare(Size size, Size t1) {
            return Long.signum((long) size.getWidth() + size.getHeight() /
                    (long) t1.getWidth() + t1.getHeight());
        }
    }

    private void setUpCamera(int width, int height) {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) ==
                        CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }
                StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                int deviceOrientation = getWindowManager().getDefaultDisplay().getRotation();
                totalRotation = sensorToDeviceRotation(cameraCharacteristics, deviceOrientation);
                boolean swapRotation = totalRotation == 90 || totalRotation == 270;
                int rotatedWidth = width;
                int rotatedHeight = height;
                if (swapRotation) {
                    rotatedHeight = width;
                    rotatedWidth = height;
                }
                mPreviewSize = chooseOptionalSize(map.getOutputSizes(SurfaceTexture.class), rotatedWidth, rotatedHeight);
                mImageSize = chooseOptionalSize(map.getOutputSizes(ImageFormat.JPEG), rotatedWidth, rotatedHeight);
                mCameraId = cameraId;
                return;

            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    private void connectCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            cameraManager.openCamera(mCameraId, mCameraStateCallBack, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void startBackGroundThread(){
        mBackgroungHandlerThread=new HandlerThread("Camera2Api");
        mBackgroungHandlerThread.start();
        mBackgroundHandler=new Handler(mBackgroungHandlerThread.getLooper());
    }
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
    private  static int sensorToDeviceRotation(CameraCharacteristics cameraCharacteristics,int deviceOrientation){
        int sensorOrientation=cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        deviceOrientation =ORIENTATION.get(deviceOrientation);
        return (sensorOrientation + deviceOrientation + 360) % 360;
    }
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
    protected void tackphoto(){
        if (capturedMats.size()!=0){
            capturedMats.clear();
        }
        progressDoalog.setMessage("FaceDetection UnderProgress");
        progressDoalog.show();
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
                        btm = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        Toast.makeText(Camera.this,"Working ",Toast.LENGTH_SHORT).show();
                        Log.d("Camera","Working");


                        final Mat studentImage=new Mat(btm.getWidth(),btm.getHeight(),CvType.CV_8UC1);
                        Utils.bitmapToMat(btm,studentImage);
                        Toast.makeText(Camera.this,"Under Detection ",Toast.LENGTH_SHORT).show();
                        Log.d("Camera","Detection");
                        //Mat faceMat=null;
                        faceMat = FaceDetection.detectFaces(null,studentImage,true);
                        if (faceMat !=null){
                            capturedMats.put(0,studentImage);
                            for (int i=1;i<=4;i++){
                                capturedMats.put(i, faceMat);
                            }
                            Toast.makeText(getApplicationContext(),"Face Detected",Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Face Not Detected",Toast.LENGTH_SHORT).show();
                        }
                        progressDoalog.dismiss();
                       display();

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
    //initialization
    private void init(){
        name=findViewById(R.id.studentname);
        classes=findViewById(R.id.depclass);
        studentdetails =findViewById(R.id.studentdetail);
        capturing=findViewById(R.id.Capture);
        save=findViewById(R.id.Save);
        cancle=findViewById(R.id.Cancle);
        photo=findViewById(R.id.imageView);
        progressDoalog = new ProgressDialog(Camera.this);
    }
    public void display(){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (faceMat != null){
                    save.setVisibility(View.VISIBLE);
                    cancle.setVisibility(View.VISIBLE);
                    mTextureView.setVisibility(View.GONE);
                    i1=Bitmap.createBitmap(faceMat.width(),faceMat.height(),Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(faceMat,i1);
                    photo.setImageBitmap(i1);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Click Photo again",Toast.LENGTH_SHORT).show();
                    Log.i("Face Not Detected","Click Photo again");
                }

            }
        });
    }
}
