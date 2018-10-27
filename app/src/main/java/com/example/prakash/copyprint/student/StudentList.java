package com.example.prakash.copyprint.student;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.prakash.copyprint.R;
import com.example.prakash.copyprint.database.FaceRecognizerDBHelper;
import com.example.prakash.copyprint.database.Student;
import com.example.prakash.copyprint.database.StudentCRUD;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by user on 24-09-2018.
 */

public class StudentList extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<StudentModel> dataSet;
    Toolbar toolbar;
    static MyOnClickListener myOnClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.student_details_list);
        myOnClickListener =new MyOnClickListener(this);
        recyclerView=(RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        dataSet=new ArrayList<>();
        setStudentData();
        for (int i=0;i<StudentData.nameArray.length;i++){
            dataSet.add(new StudentModel(StudentData.nameArray[i],
                    StudentData.IdArray[i],StudentData.ClassArray[i],StudentData.FaceUri[i]));
        }
        adapter=new StudentAdapter(dataSet);
        recyclerView.setAdapter(adapter);
    }
    public void setStudentData(){
        StudentCRUD helper=new StudentCRUD(getApplicationContext());
        try {
            helper.open();
            Vector<Student> student=helper.getAllStudent();
            String[] StudentName = new String[student.size()];
            Integer[] StudentId = new Integer[student.size()];
            String[] StudentClass = new String[student.size()];
            String[] StudentFaceURI = new String[student.size()];
            int i=0;
            for (Student student1:student)
            {
                StudentName[i]=student1.getStudentName();
                Log.d("id",String.valueOf(student1.getStudentId()));
                StudentId[i] =student1.getStudentId();
                StudentClass[i] =student1.getStudentClass();
                StudentFaceURI[i]=student1.getFaceUri()+"/0.jpg";
                i++;
            }
            new StudentData(StudentName,StudentId,StudentClass,StudentFaceURI);
            helper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private class MyOnClickListener implements View.OnClickListener{
        private Context mContext;

        public MyOnClickListener(Context context) {
            mContext=context;
        }

        @Override
        public void onClick(View view) {
            int id  = recyclerView.getChildPosition(view);
            Log.d("position",String.valueOf(id));
            Log.d("position",String.valueOf(dataSet.get(id).getId())+"class"+dataSet.get(id).getStudentclass());
            Bundle bundle = new Bundle();
            bundle.putString("id",String.valueOf(dataSet.get(id).getId()));
            bundle.putString("class",dataSet.get(id).getStudentclass());
            Intent i =new Intent(StudentList.this,StudentTable.class);
            i.putExtras(bundle);
            startActivity(i);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_settings:
                break;

        }
        return true;
    }
}
