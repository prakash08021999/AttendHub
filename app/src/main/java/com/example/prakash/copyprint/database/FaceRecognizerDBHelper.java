package com.example.prakash.copyprint.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by PRAKASH on 17-09-2018.
 */

public class FaceRecognizerDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Attendencedb";
    private static int VERSION_INT = 1;
    private Context context;

    //Tables
    public static final String STUDENT_TABLE = "student";
    public static final String LECTURE_TABLE = "lecture";
    public static final String ATTENDENCE_TABLE ="attendence";
    public static final String SUBJECT_TABLE ="subject";
    public static final String TEACHER_TABLE ="teacher";
    public static final String DEP_TABLE ="Department";



    //Subject_table
    public static final String SUBJECT_ID = "sub_id";
    public static final String SUBJECT_NAME = "sub_nm";

    //Lecture Table
    public static final String LEC_ID="lec_id";
    public static final String DATE ="date";
    public static final String TOTAL_LEC="total_LEC";
    public static final String TIME="lec_time";

    //Student Table
    public static final String STUD_ID="stud_id";
    public static final String STUD_NAME="stud_nm";
    public static final String CLASS  ="class";
    public static final String FACE_URI="face_uri";

    //Attendence Table
    public static final String STATUS="status";

    //Teacher Table
    public static final  String TEACH_ID="teach_id";
    public static  final  String TEACH_NAME="teach_nm";
    public static  final String USER_NAME="user_nm";
    public static final String Email_id ="email_id";
    public static  final String PASSWORD ="password";

    //Department Table
    public static final String DEP_ID="dep_id";
    public static final String DEP_Name="dep_nm";



     public static final String CREATE_DEPARTMENT_TABLE ="CREATE TABLE "
             + DEP_TABLE + "("+ DEP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
             + DEP_Name + " TEXT )";

     public static final String CREATE_TEACHER_TABLE="CREATE TABLE "
             + TEACHER_TABLE + "("+ TEACH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
             +  DEP_ID + " INTEGER, " + TEACH_NAME + " TEXT, " + FACE_URI + " TEXT, " + USER_NAME +" TEXT, "
             + Email_id +" TEXT, "  + PASSWORD + " TEXT, " + "FOREIGN KEY(" + DEP_ID + ") REFERENCES "
             + DEP_TABLE + " ( " + DEP_ID + ")" + ")";

     public static  final String CREATE_SUBJECT_TABLE = "CREATE TABLE "
             + SUBJECT_TABLE + "("+ SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
             + SUBJECT_NAME + " TEXT, " + TEACH_ID +" INTEGER ," + CLASS+ " TEXT ,"
             + "FOREIGN KEY(" + TEACH_ID + ") REFERENCES " + TEACHER_TABLE + " ( " + TEACH_ID +")" + ")";

     public static  final String CREATE_STUDENT_TABLE = "CREATE TABLE "
             + STUDENT_TABLE + " ( "+ STUD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + DEP_ID + " INTEGER ,"
             + STUD_NAME + " TEXT , " + CLASS + " TEXT , " + FACE_URI + " TEXT "
             +  ");";

     public static  final  String CREATE_LECTURE_TABLE = "CREATE TABLE "
             + LECTURE_TABLE + "("+ LEC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
             + DATE + " TEXT, " + TIME +" TEXT, " + TOTAL_LEC + " INTEGER, " + SUBJECT_ID + " INTEGER, "
             +  "FOREIGN KEY(" + SUBJECT_ID + ") REFERENCES "
             + SUBJECT_TABLE + " ( " + SUBJECT_ID + ")" + ")";

     public static final String CREATE_ATTENDENCE_TABLE = " CREATE TABLE "
             + ATTENDENCE_TABLE + "(" + LEC_ID + " INTEGER , " + STUD_ID + " INTEGER , " + STATUS + " TEXT , "
             + " PRIMARY KEY ( " + LEC_ID + " , " +  STUD_ID  + " ) , "
             + "FOREIGN KEY(" + LEC_ID + ") REFERENCES " + LECTURE_TABLE + " ( " + LEC_ID + "),"
             + "FOREIGN KEY(" + STUD_ID + ") REFERENCES " + STUDENT_TABLE + " ( " + STUD_ID + ") )" ;




    private static FaceRecognizerDBHelper instance;

    public static synchronized FaceRecognizerDBHelper getHelper(Context context) {
        if (instance == null)
            instance = new FaceRecognizerDBHelper(context);
        return instance;
    }
    public FaceRecognizerDBHelper(Context context) {
        super(context,DATABASE_NAME, null , VERSION_INT);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DEPARTMENT_TABLE);
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_TEACHER_TABLE);
        db.execSQL(CREATE_SUBJECT_TABLE);
        db.execSQL(CREATE_LECTURE_TABLE);
        db.execSQL(CREATE_ATTENDENCE_TABLE);
        Log.i("Create "," The Table was created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+STUDENT_TABLE);
        onCreate(sqLiteDatabase);

    }
}
