package com.example.prakash.copyprint.database;

/**
 * Created by PRAKASH on 21-09-2018.
 */

public class Subject {
    int SubjectId;

    public Subject(String sbjectName) {
        SbjectName = sbjectName;
    }

    public Subject(int subjectId, String sbjectName) {
        SubjectId = subjectId;
        SbjectName = sbjectName;
    }

    public int getSubjectId() {
        return SubjectId;
    }

    public void setSubjectId(int subjectId) {
        SubjectId = subjectId;
    }

    public  static  String getSbjectName() {
        return SbjectName;
    }

    public void setSbjectName(String sbjectName) {
        SbjectName = sbjectName;
    }

    public static String SbjectName;

}
