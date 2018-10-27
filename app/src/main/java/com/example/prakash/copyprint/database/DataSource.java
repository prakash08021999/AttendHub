package com.example.prakash.copyprint.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by PRAKASH on 26-09-2018.
 */

public class DataSource {
    private SQLiteDatabase database;
    private FaceRecognizerDBHelper dbHelper;
    public DataSource(Context context) {
        dbHelper = new FaceRecognizerDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }



}
