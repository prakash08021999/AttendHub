package com.example.prakash.copyprint.database;

import java.sql.Date;

/**
 * Created by PRAKASH on 21-09-2018.
 */

public class Lecture {
    int LectureId;
    Date date;

    //Lecture details
    public static String studentClass;
    public  static int totalLectuer;
    public static String subjects;
    public  static String time;
    public Lecture(String  sClasss,int totalLect,String subjec,String times) {
        studentClass=sClasss;
        totalLectuer=totalLect;
        subjects=subjec;
        time=times;
    }

    int TotalStud;

    public Lecture(int lectureId, Date date, int totalStud) {
        LectureId = lectureId;
        this.date = date;
        TotalStud = totalStud;
    }

    public int getLectureId() {
        return LectureId;
    }

    public void setLectureId(int lectureId) {
        LectureId = lectureId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTotalStud() {
        return TotalStud;
    }

    public void setTotalStud(int totalStud) {
        TotalStud = totalStud;
    }
}
