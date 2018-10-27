package com.example.prakash.copyprint.database;

/**
 * Created by PRAKASH on 26-09-2018.
 */

public class Depart {

    public Depart(int departmentId, String departmentName) {
        DepartmentId = departmentId;
        DepartmentName = departmentName;
    }

    public Depart(int departmentId) {
        DepartmentId = departmentId;
    }
    public Depart(String departmentName)
    {
        DepartmentName = departmentName;
    }

    public  static  int DepartmentId;
    public static String DepartmentName;


    public int getDepartmentId() {
        return DepartmentId;
    }

    public void setDepartmentId(int departmentId) {
        DepartmentId = departmentId;
    }

    public static String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }


}
