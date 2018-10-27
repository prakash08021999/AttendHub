package com.example.prakash.copyprint.database;

/**
 * Created by PRAKASH on 19-09-2018.
 */

public class Student {
    int StudentId;
    String StudentName;
    String StudentClass;
    String FaceUri;
    int DepId;

    public Student() {
    }

    public int getDepId() {
        return DepId;
    }

    public void setDepId(int depId) {
        DepId = depId;
    }

    public Student(int studentId,  int depId ,String studentName, String studentClass, String faceUri ) {
        StudentId = studentId;
        StudentName = studentName;
        StudentClass = studentClass;
        FaceUri = faceUri;
        DepId = depId;
    }

    public Student( int depId,String studentName, String studentClass, String faceUri) {
        StudentName = studentName;
        StudentClass = studentClass;
        FaceUri = faceUri;
        DepId = depId;
    }

    public int getStudentId() {
        return StudentId;
    }

    public void setStudentId(int studentId) {
        StudentId = studentId;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getStudentClass() {
        return StudentClass;
    }

    public void setStudentClass(String studentClass) {
        StudentClass = studentClass;
    }

    public String getFaceUri() {
        return FaceUri;
    }

    public void setFaceUri(String faceUri) {
        FaceUri = faceUri;
    }


}
