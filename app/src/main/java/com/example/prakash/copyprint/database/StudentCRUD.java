package com.example.prakash.copyprint.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

/**
 * Created by PRAKASH on 27-09-2018.
 */

public class StudentCRUD {
    private SQLiteDatabase database;
    private FaceRecognizerDBHelper dbHelper;

    private String[] allColumns = { FaceRecognizerDBHelper.STUD_ID,
            FaceRecognizerDBHelper.DEP_ID,FaceRecognizerDBHelper.STUD_NAME,FaceRecognizerDBHelper.CLASS,FaceRecognizerDBHelper.FACE_URI };

    public StudentCRUD(Context context) {
        dbHelper = new FaceRecognizerDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createPerson(String name, String StudentClass ,String facePath,int dptId ) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FaceRecognizerDBHelper.DEP_ID,dptId);
        contentValues.put(FaceRecognizerDBHelper.STUD_NAME,name);
        contentValues.put(FaceRecognizerDBHelper.CLASS,StudentClass);
        contentValues.put(FaceRecognizerDBHelper.FACE_URI,facePath);
        database.insert(FaceRecognizerDBHelper.STUDENT_TABLE,null ,contentValues);
    }

    public Vector<Student> getAllStudent(){
        Vector<Student> students =new Vector<Student>();
        Cursor cursor =database.query(FaceRecognizerDBHelper.STUDENT_TABLE,allColumns, null, null, null, null, null);
        Log.i("Face",String.valueOf(cursor.getCount()));
        while (cursor.moveToNext()){
            Student student=cursorToStudent(cursor);
            students.add(student);
            }
            return students;
    }

    public Vector<Student> getAllStudentCondition(String sClass){
        Vector<Student> students =new Vector<Student>();
        Cursor c= database.rawQuery("SELECT * from " + FaceRecognizerDBHelper.STUDENT_TABLE
                + "  WHERE " +FaceRecognizerDBHelper.DEP_ID +" = "+Techer.getDepId() ,null);
        while (c.moveToNext()){
            Log.i("class",sClass+" cursor:-"+c.getString(3));
            if (c.getString(3).equals(sClass)){
                Log.i("class",sClass);
                Student student=cursorToStudent(c);
                students.add(student);
            }
        }
        return students;
    }

    public Student cursorToStudent(Cursor cursor){
        Student student=new Student();
        student.setStudentId(cursor.getInt(0));
        student.setDepId(cursor.getInt(1));
        student.setStudentName(cursor.getString(2));
        student.setStudentClass(cursor.getString(3));
        student.setFaceUri(cursor.getString(4));
        return  student;
    }


    public int getTotalStudent(){
        Cursor data= database.rawQuery("SELECT count(*) FROM " + FaceRecognizerDBHelper.STUDENT_TABLE , null);
        if (data == null){
            return  0;
        }
        else {
            return  data.getCount();
        }
    }
    public Cursor getAllData() {
        Cursor res = database.rawQuery("select * from " + FaceRecognizerDBHelper.STUDENT_TABLE,null);
        return res;
    }
    public int lastData(){
        Cursor id=getAllData();
        if(id.getCount() == 0) {
            // show message
            return 0;
        }
        else {
            id.moveToLast();
            return Integer.parseInt(id.getString(0));
        }

    }

    public void deleteStudent(int sid,String PATH) {
        int id = sid;
        database.delete(FaceRecognizerDBHelper.STUDENT_TABLE, FaceRecognizerDBHelper.STUD_ID
                + " = " + id, null);
        deleteFacesFolder(PATH);
    }

    public void deleteFacesFolder(String path){
        File f = new File(path);
        if(f.exists()){
            deleteFolder(f);
        }
    }

    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    //For Excel




}
