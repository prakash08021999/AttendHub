package com.example.prakash.copyprint.database;

/**
 * Created by PRAKASH on 21-09-2018.
 */

public class Attendence {
    public Attendence() {
    }

    public Attendence(String status) {
        Status = status;
    }

    String Status;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
