package com.example.prakash.copyprint.loginactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakash.copyprint.R;
import com.example.prakash.copyprint.all_dialogcollection.AddSubjectDialog;
import com.example.prakash.copyprint.database.Depart;
import com.example.prakash.copyprint.database.Subject;
import com.example.prakash.copyprint.database.TeacherCRUD;
import com.example.prakash.copyprint.database.Techer;

import org.opencv.core.Mat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PRAKASH on 25-09-2018.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private static EditText fullName, emailId, subject, department,
            password, username;
    LinearLayout l1;
    // Get ListView object from xml
    List<String> ty=new ArrayList<>();
    List<String> sy=new ArrayList<>();
    List<String> fy=new ArrayList<>();
    ListView listView;
    private static TextView login;
    LinearLayout mRelativeLayout;
    private static Button signUpButton ;
    private  static ImageButton imageButton;
    private PopupWindow mPopupWindow;
    ImageButton AddButton;
    private static CheckBox terms_conditions;
    private ListView subjectList;
    String[] subjects;
    TeacherCRUD teacherCRUD ;
    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
    Spinner  sp;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
       l1 = findViewById(R.id.l1);
        initViews();
        setListeners();

        teacherCRUD =new TeacherCRUD(getApplicationContext());
    }
    public void popup(){

        LayoutInflater layoutInflater=(LayoutInflater)SignUpActivity.this.getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        View customView=layoutInflater.inflate(R.layout.subject_pop,null);


        // Get a reference for the custom view close button
        Button closeButton = customView.findViewById(R.id.submit);
        Button addButton = customView.findViewById(R.id.add);
        final EditText subname=customView.findViewById(R.id.subject);
        listView= customView.findViewById(R.id.list_item);
        sp=customView.findViewById(R.id.clas);
        // Spinner Drop down elements
        List<String> spclass = new ArrayList<String>();
        spclass.add(" TY ");
        spclass.add(" SY ");
        spclass.add(" FY ");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,spclass);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(dataAdapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp.getSelectedItem()==" TY "){
                    if(listView.getCount()>0){
                        listView.setAdapter(null);
                    }
                    adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, android.R.id.text1,ty);
                    listView.setAdapter(adapter);

                }
                else  if(sp.getSelectedItem()==" SY "){
                    if(listView.getCount()>0){
                        listView.setAdapter(null);
                    }
                    adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, android.R.id.text1,sy);
                    listView.setAdapter(adapter);
                }
                else {
                    if(listView.getCount()>0){
                        listView.setAdapter(null);
                    }
                    adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, android.R.id.text1,fy);
                    listView.setAdapter(adapter);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });
        final Pattern RegSubject=Pattern.compile("^[A-Za-z][A-Za-z.*\\s]+$");


        addButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
            Matcher subjectt =RegSubject.matcher(subname.getText().toString());
             if (subname.getText().length()==0){
                 Toast.makeText(getApplicationContext(),"Enter Some Thing inside list",Toast.LENGTH_LONG).show();
             }
             else if (!subjectt.find()){
                 Toast.makeText(getApplicationContext(),"Enter Correct Subject",Toast.LENGTH_LONG).show();
              }
             else {
                 if (sp.getSelectedItem()==" TY "){
                     ty.add(subname.getText().toString());
                     adapter = new ArrayAdapter<String>(getApplicationContext(),
                             android.R.layout.simple_list_item_1, android.R.id.text1,ty);
                     listView.setAdapter(adapter);
                 }
                 else  if(sp.getSelectedItem()==" SY "){
                     sy.add(subname.getText().toString());
                     adapter = new ArrayAdapter<String>(getApplicationContext(),
                             android.R.layout.simple_list_item_1, android.R.id.text1,sy);
                     listView.setAdapter(adapter);
                 }
                 else {
                     fy.add(subname.getText().toString());
                     adapter = new ArrayAdapter<String>(getApplicationContext(),
                             android.R.layout.simple_list_item_1, android.R.id.text1,fy);
                     listView.setAdapter(adapter);
                 }
                 subname.setText("");
             }
            }
        });
        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String  itemValue    = (String) listView.getItemAtPosition(position);
                if (sp.getSelectedItem()==" TY "){
                ty.remove(position);
                adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, android.R.id.text1,ty);
                listView.setAdapter(adapter);
            }
            else  if(sp.getSelectedItem()==" SY "){
                    sy.remove(position);
                    adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, android.R.id.text1,sy);
                    listView.setAdapter(adapter);
            }
             else {
                    fy.remove(position);
                    adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, android.R.id.text1,fy);
                    listView.setAdapter(adapter);
             }
            }

        });


        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data="";
                for (int i=0;i<ty.size();i++){
                    data=data+ty.get(i)+" ";
                }
                for (int i=0;i<fy.size();i++){
                    data=data+fy.get(i)+" ";
                }
                for (int i=0;i<sy.size();i++){
                    data=data+sy.get(i)+" ";
                }
                subject.setText(data);
                // Dismiss the popup window
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }
        mPopupWindow.showAtLocation(l1, Gravity.CENTER,0,0);
    }

    // Initialize all views
    private void initViews() {
        imageButton=findViewById(R.id.add);
        fullName =findViewById(R.id.fullName);
        emailId = findViewById(R.id.userEmailId);
        department =findViewById(R.id.Dipartment);
        subject =findViewById(R.id.Subject);
        password =findViewById(R.id.Password);
        username = findViewById(R.id.username);
        signUpButton = findViewById(R.id.signUpBtn);
        login =findViewById(R.id.already_user);


        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.textselector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
        imageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        case R.id.signUpBtn:

        // Call checkValidation method
        checkValidation();
        break;

        case R.id.already_user:
            finish();
            startActivity(new Intent(SignUpActivity.this,LoginActivity.class));

         break;

        case R.id.add:
            popup();
            break;

      }

    }


    // Check Validation Method
    private void checkValidation() {



        // Get all edittext texts
        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getSubject = subject.getText().toString();
        String getDepartment= department.getText().toString();
        String getPassword = password.getText().toString();
        String getusername = username.getText().toString();

        //Pattern match for email id
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher passwordmach = pattern.matcher(getPassword);

        // Pattern match for email id
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(getEmailId);
        //Pattern match for Name,Department
        Pattern Reguname=Pattern.compile("^[A-Za-z\\s]+$");
        Matcher name=Reguname.matcher(getFullName);
        final Pattern RegDep=Pattern.compile("^[A-Za-z][A-Za-z.*\\s]+$");
        Matcher departmentPattern=RegDep.matcher(getDepartment);




        //Pttern match for user name
        Pattern userpatteren=Pattern.compile(USERNAME_PATTERN);
        Matcher userpatterens=userpatteren.matcher(getusername);

        boolean status=true;
        // Check if all strings are null or not
       if (getFullName.equals("") || getFullName.length() == 0){
           fullName.setError("Enter Some Thinge");
           status=false;
       }
        if(getEmailId.equals("") || getEmailId.length() == 0){
            emailId.setError("Enter Some Thinge");
            status=false;
        }
        if(getSubject.equals("") || getSubject.length() == 0){
            subject.setError("Enter Some Thinge");
            status=false;
        }
        if(getDepartment.equals("") || getDepartment.length() == 0){
            department.setError("Enter Some Thinge");
            status=false;
         }
         if(getPassword.equals("") || getPassword.length() == 0){
             password.setError("Enter Some Thinge");
             status=false;
         }
         if(getusername.equals("") || getusername.length() == 0){
             username.setError("Enter Some Thinge");
            status=false;
        }
        if(!name.find()){
            fullName.setError("Enter Correct Name");
            status=false;
        }
        else if(!departmentPattern.find()){
             department.setError("Enter Correct Department");
             status=false;
        }
        else if(!matcher.find()){

            emailId.setError("Enter Correct Email");
            status=false;
        }
        else if(!userpatterens.find()){
            username.setError("Enter Correct UserName");
            status=false;
        }
        else if(!passwordmach.find()){
            password.setError("Try mix password with letter,digit,and symbol");
            status=false;
        }
        try {
            teacherCRUD.open();
            if (teacherCRUD.getUserNameCheck(getusername)>0){
                AlertDialog alertDialog = new AlertDialog.Builder(
                        SignUpActivity.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Alert Dialog");

                // Setting Dialog Message
                alertDialog.setMessage("The User Name already exist");
                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                // Showing Alert Message
                alertDialog.show();
                status=false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            teacherCRUD.close();
        }


        if(status){
            //List<String> elephantList = Arrays.asList(getSubject.split(",[ ]*"));
            try {
                teacherCRUD.open();
                int dptId = 0;
                new Depart(getDepartment);
                boolean value=teacherCRUD.cheakDepartme();
                if(value){
                    teacherCRUD.createDipartment(getDepartment.toLowerCase());
                    dptId=Depart.DepartmentId;
                }
                else {
                    dptId=Depart.DepartmentId;
                }
                teacherCRUD.createTeacher(dptId,getFullName,getusername,getEmailId,getPassword);
                for(int i=0 ;i<ty.size();i++){
                    teacherCRUD.createSubject(ty.get(i),teacherCRUD.getTeacherId(getFullName),"TY");
                }
                for(int i=0 ;i<sy.size();i++){
                    teacherCRUD.createSubject(sy.get(i),teacherCRUD.getTeacherId(getFullName),"SY");
                }
                for(int i=0 ;i<fy.size();i++){
                    teacherCRUD.createSubject(fy.get(i),teacherCRUD.getTeacherId(getFullName),"FY");
                }
               /* for(int i=0;i<elephantList.size();i++){
                    Log.d("SubjectList",elephantList.get(i));
                    teacherCRUD.createSubject(elephantList.get(i),teacherCRUD.getTeacherId(getFullName));
                }*/
                fullName.setText("");
                emailId.setText("");
                subject.setText("");
                department.setText("");
                password.setText("");
                username.setText("");
                Toast.makeText(getApplicationContext(),"User Created",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                teacherCRUD.close();
            }

        }

    }

}