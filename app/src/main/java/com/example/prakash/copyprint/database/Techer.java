package com.example.prakash.copyprint.database;

/**
 * Created by PRAKASH on 21-09-2018.
 */

public class  Techer {
    public static int TeacherId;
    public static String TeacherNmae;
    public static String FaceUri;
    public static String UserName;
    public static String Password;

    public static int getDepId() {
        return DepId;
    }

    public static void setDepId(int depId) {
        DepId = depId;
    }

    public static int DepId;
    public static String getEmailId() {
        return EmailId;
    }

    public static void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public static String EmailId;

    public Techer(int getTeacherId,int getDepId ,String getTeacherName,String  getFaceUri,String  getUserName,String  getEmaiId,String  getPassWord) {
    TeacherId=getTeacherId;
    DepId=getDepId;
    TeacherNmae=getTeacherName;
    FaceUri=getFaceUri;
    UserName=getUserName;
    EmailId=getEmaiId;
    Password=getPassWord;
    }

    public Techer(String teacherNmae, String faceUri, String userName, String emailId,String password) {
        TeacherNmae = teacherNmae;
        FaceUri = faceUri;
        UserName = userName;
        EmailId =emailId;
        Password = password;
    }

    public Techer(int teacherId, String teacherNmae, String faceUri, String userName, String password) {
        TeacherId = teacherId;
        TeacherNmae = teacherNmae;
        FaceUri = faceUri;
        UserName = userName;
        Password = password;
    }

    public static int getTeacherId() {
        return TeacherId;
    }

    public void setTeacherId(int teacherId) {
        TeacherId = teacherId;
    }

    public static  String getTeacherNmae() {
        return TeacherNmae;
    }

    public static  void setTeacherNmae(String teacherNmae) {
        TeacherNmae = teacherNmae;
    }

    public static String getFaceUri() {
        return FaceUri;
    }

    public static  void setFaceUri(String faceUri) {
        FaceUri = faceUri;
    }

    public static String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public static String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
