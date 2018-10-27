package com.example.prakash.copyprint.student;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.prakash.copyprint.R;
import com.example.prakash.copyprint.all_dialogcollection.AddSubjectDialog;
import com.example.prakash.copyprint.database.TeacherCRUD;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * Created by user on 24-09-2018.
 */


public class StudentTable extends AppCompatActivity {
    int id;
    String[][] table;

    public StudentTable() {
    }

    EditText lec_date;
    String sclass;
    Button show;

    static String[] spaceProbeHeaders={"Subject","Lecture","Date","Time","Status"};
    TableView<String[]> tableView;
    TableColumnDpWidthModel columnModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendence_table);
        lec_date=findViewById(R.id.startDate);
        show=findViewById(R.id.show);
        tableView = (TableView<String[]>) findViewById(R.id.tableView);
        //SET PROP
        Bundle bundle = getIntent().getExtras();
        id = Integer.parseInt(bundle.getString("id"));
        sclass = bundle.getString("class");
        columnModel = new TableColumnDpWidthModel(getApplicationContext(), 4, 200);
        lec_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int date = mcurrentTime.get(Calendar.DATE);
                int month = mcurrentTime.get(Calendar.MONTH);
                int year =mcurrentTime.get(Calendar.YEAR);
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(StudentTable.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker DatePicker, int Date, int Month,int Year) {
                        lec_date.setText( Year + "-" + Month+"-"+Date);
                    }
                }, date, month, year);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeacherCRUD teacherCRUD=new TeacherCRUD(getApplicationContext());
                try {
                    teacherCRUD.open();
                    String date=lec_date.getText().toString();
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    Date date1=null;
                    try {
                       date1 = format.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String dateTime = format.format(date1);
                        List<String[]> result= teacherCRUD.attendences(dateTime,id,sclass);
                    if(result!=null && result.size()!=0){
                        disply(result);
                    }
                    else {

                        Toast.makeText(getApplicationContext(),"There is no Attendence For Student",Toast.LENGTH_SHORT).show();

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                finally {
                    teacherCRUD.close();
                }

            }
        });
    }
    public void disply(List<String[]> result){
        int j=0;
        for(String[] data : result){
            table=new String[result.size()][5];
            for(int i=0;i<5;i++){
                table[j][i]=data[i];
                Log.i("att",table[j][i]);
            }
            j++;
        }
        columnModel.setColumnWidth(1, 50);
        columnModel.setColumnWidth(2, 100);
        columnModel.setColumnWidth(3,150);
        columnModel.setColumnWidth(4,50);
        columnModel.setColumnWidth(5,50);
        tableView.setColumnModel(columnModel);
        tableView.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this,spaceProbeHeaders));
        tableView.setColumnCount(4);
        tableView.setDataAdapter(new SimpleTableDataAdapter(StudentTable.this, table));
    }

}