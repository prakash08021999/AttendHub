package com.example.prakash.copyprint.student;

/**
 * Created by user on 24-09-2018.
 */

public class StudentModel {
    String Name;
    int id;

    public StudentModel(String name, int id, String studentclass, String faceuri) {
        Name = name;
        this.id = id;
        Studentclass = studentclass;
        this.faceuri = faceuri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentclass() {
        return Studentclass;
    }

    public void setStudentclass(String studentclass) {
        Studentclass = studentclass;
    }

    public String getFaceuri() {
        return faceuri;
    }

    public void setFaceuri(String faceuri) {
        this.faceuri = faceuri;
    }

    String Studentclass,faceuri;


    public StudentModel(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
