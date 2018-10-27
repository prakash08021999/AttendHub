package com.example.prakash.copyprint.loginactivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prakash.copyprint.FaceRecognitionCamera;
import com.example.prakash.copyprint.MainActivity;
import com.example.prakash.copyprint.R;
import com.example.prakash.copyprint.database.StudentCRUD;
import com.example.prakash.copyprint.database.TeacherCRUD;
import com.example.prakash.copyprint.database.Techer;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PRAKASH on 04-10-2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static EditText emailid, password;
    private static Button loginButton;
    private static TextView forgotPassword, signUp;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static TeacherCRUD  teacherCRUD;

    @Override
    public void onCreate(Bundle SaveInstanceState){
        super.onCreate(SaveInstanceState);
        setContentView(R.layout.login_layout);
        initViews();
        setListeners();

    }

    // Initiate Views
    private void initViews() {
        teacherCRUD=new TeacherCRUD(getApplicationContext());
        emailid = (EditText)findViewById(R.id.login_emailid);
        password = (EditText)findViewById(R.id.login_password);
        loginButton = (Button)findViewById(R.id.loginBtn);
        forgotPassword = (TextView)findViewById(R.id.forgot_password);
        signUp = (TextView)findViewById(R.id.createAccount);
        show_hide_password = (CheckBox)findViewById(R.id.show_hide_password);
        loginLayout = (LinearLayout)findViewById(R.id.login_layout);



        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.textselector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            forgotPassword.setTextColor(csl);
            show_hide_password.setTextColor(csl);
            signUp.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);

        // Set check listener over checkbox for showing and hiding password
        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });
    }


    @Override
    public void onClick(View view) {
        int iteam=view.getId();
        switch (iteam){
            case R.id.loginBtn:
                checkValidation();
                break;

            case R.id.forgot_password:
                emailid.setText("");
                password.setText("");
                startActivity(new Intent(LoginActivity.this,ForgetPassword.class));

                break;
            case R.id.createAccount:
                emailid.setText("");
                password.setText("");
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));

                break;
        }
    }
    // Check Validation before login
    private void checkValidation() {
        // Get email id and password
        String getEmailId = emailid.getText().toString();
        String getPassword = password.getText().toString();

        boolean flag=true;
        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0){
           emailid.setError("Enter User Name");
           flag=false;

        }
         if(getPassword.equals("") || getPassword.length() == 0) {
            password.setError("Enter Password Name");
            flag=false;

         }
        // Check if email id is valid or not
        try {
            teacherCRUD.open();
            Cursor cursor=teacherCRUD.getLoginAuthentication(getEmailId,getPassword);
             if (cursor.getCount()==0 && flag){
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        //set icon
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        //set title
                        .setTitle("Please Enter Correct Password or User Name").show();
            }
            else {
                while (cursor.moveToNext()){
                    int getTeacherId=Integer.parseInt(cursor.getString(0));
                    int getDepId=Integer.parseInt(cursor.getString(1));
                    String  getTeacherName=cursor.getString(2);
                    String  getFaceUri=cursor.getString(3);
                    String  getUserName=cursor.getString(4);
                    String  getEmaiId=cursor.getString(5);
                    String  getPassWord =cursor.getString(6);
                    new Techer(getTeacherId,getDepId ,getTeacherName, getFaceUri, getUserName, getEmaiId,getPassWord);
                }
                emailid.setText("");
                password.setText("");
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            teacherCRUD.close();
        }


    }
}
