package com.example.prakash.copyprint.all_dialogcollection;


import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.prakash.copyprint.FaceRecognitionCamera;
import com.example.prakash.copyprint.MainActivity;
import com.example.prakash.copyprint.R;
import com.example.prakash.copyprint.database.Lecture;
import com.example.prakash.copyprint.database.LectureC;
import com.example.prakash.copyprint.database.Techer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by PRAKASH on 03-10-2018.
 */

public class AddSubjectDialog extends Activity {
    private EditText totalLecture,lect_time;
    private Spinner lsubject,sClass;
    public int teacherId;
    Button submit;
    List<String> tySubject;
    List<String> sySubject;
    List<String> fySubject;


    @Override
    public void onCreate(Bundle SaveInstanceState){
        super.onCreate(SaveInstanceState);
        setContentView(R.layout.add_subject_dialog);
        totalLecture =findViewById(R.id.lectuers);
        lsubject=findViewById(R.id.Subject);
        lect_time=findViewById(R.id.time);
        sClass=findViewById(R.id.sclass);
        submit=findViewById(R.id.submit);
        tySubject=new ArrayList<>();
        sySubject=new ArrayList<>();;
        fySubject=new ArrayList<>();;



        teacherId= Techer.getTeacherId();
        LectureC lec=new LectureC(AddSubjectDialog.this);
        String[] subjectList = null;
        try {
            lec.open();
            Cursor subject= lec.getSubject(teacherId);
            subjectList=new String[subject.getCount()];
            int i=0;
            while (subject.moveToNext()){
                subjectList[i]=subject.getString(1);
                Log.d("class",subject.getString(3));
                if (subject.getString(3).equals("TY"))
                {
                    Log.d("ty",subject.getString(3));
                    tySubject.add(subject.getString(1));
                }
               else if (subject.getString(3).equals("FY"))
                {
                    fySubject.add(subject.getString(1));
                }
                else
                {
                    sySubject.add(subject.getString(1));
                }
                i++;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        finally {
            lec.close();
        }
        sClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sClass.getSelectedItem()=="FY")
                {
                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddSubjectDialog.this, android.R.layout.simple_spinner_item,fySubject);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lsubject.setAdapter(spinnerArrayAdapter);

                }
                else if (sClass.getSelectedItem()=="SY")
                {
                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddSubjectDialog.this, android.R.layout.simple_spinner_item,sySubject);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lsubject.setAdapter(spinnerArrayAdapter);
                }
                else
                {
                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddSubjectDialog.this, android.R.layout.simple_spinner_item,tySubject);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lsubject.setAdapter(spinnerArrayAdapter);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lect_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddSubjectDialog.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        lect_time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        // Initializing an ArrayAdapter


        final String[] tclass={"TY","SY","FY"};
        final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(AddSubjectDialog.this, android.R.layout.simple_spinner_item,tclass);
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sClass.setAdapter(spinnerArrayAdapter1);

       submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (totalLecture.getText().length()==0 || totalLecture.getText().toString() == "" ||  lect_time.getText().toString() == "" ||  lect_time.getText().length()==0 || lsubject.getSelectedItem()=="" ){
                   Toast.makeText(AddSubjectDialog.this,"Enter All Details",Toast.LENGTH_SHORT).show();
               }
               else {
                   String subjec=lsubject.getSelectedItem().toString();
                   String sClasss=sClass.getSelectedItem().toString();
                   String times=lect_time.getText().toString();
                   int totalLect=Integer.parseInt(totalLecture.getText().toString());
                   Intent intent=new Intent(AddSubjectDialog.this,FaceRecognitionCamera.class);
                   Log.d("subject",subjec);
                   new Lecture(sClasss,totalLect,subjec,times);
                   startActivity(intent);

               }
           }
       });
    }
}
