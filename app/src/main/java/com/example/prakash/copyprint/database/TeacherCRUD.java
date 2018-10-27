package com.example.prakash.copyprint.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PRAKASH on 26-09-2018.
 */

public class TeacherCRUD {
    private SQLiteDatabase database;
    private FaceRecognizerDBHelper dbHelper;

    public TeacherCRUD(Context context)
    {
        dbHelper = new FaceRecognizerDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean createTeacher(int DptId,String name,String userName,String email,String Password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FaceRecognizerDBHelper.DEP_ID,DptId);
        contentValues.put(FaceRecognizerDBHelper.TEACH_NAME,name);
        contentValues.put(FaceRecognizerDBHelper.FACE_URI,"");
        contentValues.put(FaceRecognizerDBHelper.USER_NAME,userName);
        contentValues.put(FaceRecognizerDBHelper.Email_id,email);
        contentValues.put(FaceRecognizerDBHelper.PASSWORD,Password);
        long result = database.insert(FaceRecognizerDBHelper.TEACHER_TABLE,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public  boolean createSubject(String subjName,int teacherId,String subClass){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FaceRecognizerDBHelper.SUBJECT_NAME,subjName);
        contentValues.put(FaceRecognizerDBHelper.TEACH_ID,teacherId);
        contentValues.put(FaceRecognizerDBHelper.CLASS,subClass);
        database.insert(FaceRecognizerDBHelper.SUBJECT_TABLE,null,contentValues);
        return true;
    }
    public  boolean createDipartment(String dipName){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FaceRecognizerDBHelper.DEP_Name,dipName);
        database.insert(FaceRecognizerDBHelper.DEP_TABLE,null,contentValues);
        Cursor data= database.rawQuery("SELECT * FROM " + FaceRecognizerDBHelper.DEP_TABLE + " WHERE " + FaceRecognizerDBHelper.DEP_Name  + " = ? " ,  new String[] {dipName});
        while (data.moveToNext()){
            new Depart(Integer.parseInt(data.getString(0)));
            Log.d("dep",data.getString(0));
        }
        return true;
    }
    public boolean cheakDepartme(){
        Cursor data= database.rawQuery("SELECT * FROM " + FaceRecognizerDBHelper.DEP_TABLE + " WHERE " + FaceRecognizerDBHelper.DEP_Name  + " = ? " ,  new String[] {Depart.getDepartmentName().toLowerCase()});

        if (data.getCount()>=1 ){
            while (data.moveToNext()){
                new Depart(Integer.parseInt(data.getString(0)));
                Log.d("dep",data.getString(0));
            }
         return false;
     }
     else {
         return  true;
     }
    }
    public  int getTeacherId(String TeachName){
        Cursor c = database.rawQuery("select teach_id from " + FaceRecognizerDBHelper.TEACHER_TABLE +
                " where " + FaceRecognizerDBHelper.TEACH_NAME + " = ? ", new String[] { TeachName});
        int id=0;
        while (c.moveToNext()){
            id=Integer.parseInt(c.getString(0));
        }
        return id;
    }
    public Cursor getLoginAuthentication(String userName ,String Password){
        Cursor c = database.rawQuery("select * from " +
                        FaceRecognizerDBHelper.TEACHER_TABLE + " where " + FaceRecognizerDBHelper.USER_NAME + " = ? AND " + FaceRecognizerDBHelper.PASSWORD +
                        " = ? ", new String[] {userName,Password});
        return c;
    }

    public int getUserNameCheck(String userName){
        Cursor c = database.rawQuery("select * from " +
                        FaceRecognizerDBHelper.TEACHER_TABLE + " where " + FaceRecognizerDBHelper.USER_NAME + " = ? ",
                new String[] {userName});
        return c.getCount();
    }
    public List attendences(String date, int stu_ids,String sclass)
    {
        Cursor c=database.rawQuery("select sub_id , sub_nm , class from " +
                        FaceRecognizerDBHelper.SUBJECT_TABLE + " where " + FaceRecognizerDBHelper.TEACH_ID + " = (select teach_id from "+
                        FaceRecognizerDBHelper.TEACHER_TABLE +" where "+ FaceRecognizerDBHelper.DEP_ID +" = "+ Techer.getDepId() +")",
                null);

        List<String[]> result=new ArrayList<String[]>();
        while (c.moveToNext()){
            if (c.getString(2).equals(sclass))
            {
            int subid=Integer.parseInt(c.getString(0));
                Cursor c1=database.rawQuery("select * from " + FaceRecognizerDBHelper.LECTURE_TABLE , null);
                while (c1.moveToNext()){
                    if (c1.getString(1).equals(date)){
                    Cursor statuslec=database.rawQuery("Select "+FaceRecognizerDBHelper.STATUS+ " from "+
                            FaceRecognizerDBHelper.ATTENDENCE_TABLE +" where "+ FaceRecognizerDBHelper.LEC_ID +" = "+
                            c1.getString(0)+ " AND "+ FaceRecognizerDBHelper.STUD_ID +" = "+stu_ids,null);
                    String[] record=new String[5];
                    if (statuslec!= null){
                        record= new String[]{c.getString(1), c1.getString(3), date, c1.getString(2), "P"};
                    }
                    else {
                        Log.i("status","A");
                        record=new String[]{c.getString(1),c1.getString(3),date,c1.getString(2),"P"};
                    }
                    result.add(record);}
                }

        }
        }
        return result;
    }
    public  List<String> getUsername(String email)
    {
        List<String> personal=new ArrayList<>();
        Cursor data= database.rawQuery("SELECT * FROM " + FaceRecognizerDBHelper.TEACHER_TABLE + " WHERE " + FaceRecognizerDBHelper.Email_id  + " = ? " ,  new String[] {email});
        while (data.moveToNext())
        {
            String username=data.getString(4);
            String passwOrde=data.getString(6);
            personal.add(username);
            personal.add(passwOrde);
        }
        return personal;
    }

    public boolean updateTechName() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FaceRecognizerDBHelper.DEP_ID,Techer.DepId);
        contentValues.put(FaceRecognizerDBHelper.TEACH_NAME,Techer.getTeacherNmae());
        contentValues.put(FaceRecognizerDBHelper.FACE_URI,Techer.getFaceUri());
        contentValues.put(FaceRecognizerDBHelper.USER_NAME,Techer.getUserName());
        contentValues.put(FaceRecognizerDBHelper.Email_id,Techer.getEmailId());
        contentValues.put(FaceRecognizerDBHelper.PASSWORD,Techer.getPassword());
        database.update(FaceRecognizerDBHelper.TEACHER_TABLE, contentValues, "ID = "+ Techer.getTeacherId(),null);
        return true;
    }


}
