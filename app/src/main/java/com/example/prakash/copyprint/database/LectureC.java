package com.example.prakash.copyprint.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by PRAKASH on 04-10-2018.
 */

public class LectureC {
    private SQLiteDatabase database;
    private FaceRecognizerDBHelper dbHelper;

    public LectureC(Context context)
    {
        dbHelper = new FaceRecognizerDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public  void createLecture(String date ,String time,int total_lec,int subject)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FaceRecognizerDBHelper.DATE,date);
        contentValues.put(FaceRecognizerDBHelper.TIME,time);
        contentValues.put(FaceRecognizerDBHelper.TOTAL_LEC,total_lec);
        contentValues.put(FaceRecognizerDBHelper.SUBJECT_ID,subject);
        long result = database.insert(FaceRecognizerDBHelper.LECTURE_TABLE,null ,contentValues);
    }

    public  Cursor getSubject(int id){

       Cursor c= database.rawQuery("SELECT * from " + FaceRecognizerDBHelper.SUBJECT_TABLE
                + "  WHERE " + FaceRecognizerDBHelper.TEACH_ID +" = "+id , null);
        return c;
    }
    public int getId(String name){
        Cursor c = database.rawQuery("select sub_id ,sub_nm from " + FaceRecognizerDBHelper.SUBJECT_TABLE +
                " where " + FaceRecognizerDBHelper.SUBJECT_NAME + " =? ", new String[] {name});
        int id=0;
        while (c.moveToNext()){
            Log.d("sub_nm",c.getString(1));
            id=Integer.parseInt(c.getString(0));
        }
        return id;
    }
    public Cursor getAttendenceTable(int stuid,String date){
        final String MY_QUERY = "SELECT a.sub_id , a.total_LEC , a.date , a.lec_time ,b.status FROM lecture a INNER JOIN attendence b ON a.lec_id=b.lec_id WHERE b.stud_id=? AND a.date=? ";
        Cursor  data =database.rawQuery(MY_QUERY, new String[]{String.valueOf(stuid),date});
        return  data;
    }
    public Cursor getAttendenceBetweenTable(int stuid,String startdate,String enddate){
        final String MY_QUERY = "SELECT a.sub_id , a.total_LEC , a.date , a.lec_time ,b.status FROM lecture a INNER JOIN attendence b ON a.lec_id=b.lec_id WHERE b.stud_id=? AND a.date BETWEEN ? AND ?  ";
        Cursor  data = database.rawQuery(MY_QUERY, new String[]{String.valueOf(stuid),startdate,enddate});
        return  data;
    }
    public void createAttendence(int LecId,int stuId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FaceRecognizerDBHelper.LEC_ID,LecId);
        contentValues.put(FaceRecognizerDBHelper.STUD_ID,stuId);
        contentValues.put(FaceRecognizerDBHelper.STATUS,"p");
        long result = database.insert(FaceRecognizerDBHelper.ATTENDENCE_TABLE,null ,contentValues);
    }
    public int lecId(){
        Cursor c= database.rawQuery("SELECT * from " + FaceRecognizerDBHelper.LECTURE_TABLE, null);
        c.moveToLast();
        return Integer.parseInt(c.getString(0));
    }
}


