package com.example.prakash.copyprint;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prakash.copyprint.all_dialogcollection.AddSubjectDialog;
import com.example.prakash.copyprint.database.StudentCRUD;
import com.example.prakash.copyprint.loginactivity.SignUpActivity;
import com.example.prakash.copyprint.method.FaceDetection;
import com.example.prakash.copyprint.student.StudentList;
import com.example.prakash.copyprint.utils.FaceDectectionUtils;
import com.example.prakash.copyprint.utils.ImageUtils;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.sql.SQLException;
import java.util.Currency;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
   static {
        System.loadLibrary("native-lib");
    }
    FaceDectectionUtils mUtils;

    private static int screenHeight;
    private static int screenWidth;
    private final String TAG="MainActivity";
    StudentCRUD student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int Permission_All =1;
        String[] Permission ={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
        if (!hasPermission(this,Permission))
        {
            ActivityCompat.requestPermissions(this,Permission,Permission_All);
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( dm );
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        new LoadDataTask().execute(0);


        CardView teacher=findViewById(R.id.profile);
        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent i=new Intent(MainActivity.this,TeacherProfile.class);
             startActivity(i);
            }
        });
        CardView rStudent=findViewById(R.id.student);
        rStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,Camera.class);
                startActivity(i);
            }
        });

        CardView Attendence=findViewById(R.id.attendence);
        Attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, AddSubjectDialog.class);
                startActivity(i);
            }
        });
        CardView sProfile=findViewById(R.id.student_profile);
        sProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, StudentList.class);
                startActivity(i);
            }
        });


    }
    public static int getScreenHeight(){
        return screenHeight;
    }

    public static int getScreenWidth(){
        return screenWidth;
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    //public native String nativeCreateObject();
    // The params are dummy and not used
    class LoadDataTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
            FaceDectectionUtils.initionalize(MainActivity.this);
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
    public static boolean hasPermission(Context context , String... permissions){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context != null && permissions != null ){
            for (String permission :permissions){
                if (ActivityCompat.checkSelfPermission(context,permission)!=PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

}



