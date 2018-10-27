package com.example.prakash.copyprint;

import android.os.Environment;

import java.io.File;
import java.io.*;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by PRAKASH on 07-10-2018.
 */

public class ExcelResult {
    public void create(){
    File sd= Environment.getExternalStorageDirectory();
    String csvFile = "myData.xls";
    File directory = new File(sd.getAbsolutePath()+"/AttReport");
    //create directory if not exist
        if (! directory.isDirectory()  ) {
        directory.mkdirs();
    }
        try {

            //file path
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("userList", 0);
            // column and row
            sheet.addCell(new Label(0, 0, "Name"));
            sheet.addCell(new Label(1, 0, "PhoneNumber"));


           /* if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex("user_name"));
                    String phoneNumber = cursor.getString(cursor.getColumnIndex("phone_number"));

                    int i = cursor.getPosition() + 1;
                    sheet.addCell(new Label(0, i, name));
                    sheet.addCell(new Label(1, i, phoneNumber));
                } while (cursor.moveToNext());
            }
            //closing cursor
            cursor.close();
            workbook.write();
            workbook.close();
            Toast.makeText(getApplication(),
                    "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();*/
        } catch(Exception e){
            e.printStackTrace();
        }

}
}
