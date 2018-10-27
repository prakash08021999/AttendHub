package com.example.prakash.copyprint.loginactivity;

/**
 * Created by PRAKASH on 08-10-2018.
 */
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakash.copyprint.Camera;
import com.example.prakash.copyprint.R;
import com.example.prakash.copyprint.database.TeacherCRUD;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class ForgetPassword extends AppCompatActivity  {

    EditText email;
    String subject ;
    String username="";
    Button btn_feedback_submit;

    HashMap<String,String> hashMap;
    ProgressDialog pDialog;
    String url3="https://scrawliest-props.000webhostapp.com/forgotpassword.php";
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forget_password);
        btn_feedback_submit = findViewById(R.id.buttonSend);


        email = findViewById(R.id.editTextEmail);



        btn_feedback_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TeacherCRUD teacherCRUD=new TeacherCRUD(getApplicationContext());
                try {
                    teacherCRUD.open();
                    List<String> user=teacherCRUD.getUsername(email.getText().toString());
                    if (user == null)
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext())
                                //set icon
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                //set title
                                .setTitle("Email Aleart")
                                .setMessage("Enter Correct Email Id").show();
                    }
                    else {
                        username="UserName :- "+user.get(0)+" and ";
                        username +="Password :- "+user.get(1);
                        boolean isdata=true;
                        hashMap=new HashMap<>();
                        hashMap.put("email",email.getText().toString() );
                        hashMap.put("subject","ForgetPassword");
                        hashMap.put("message",username);
                        if(TextUtils.isEmpty(email.getText().toString())){
                            showMessage("Error","please provide some content ");
                            isdata=false;
                        }

                        email.setText("");
                        if(isdata)
                            new PostDataTOServerFeedback().execute();
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

    public class PostDataTOServerFeedback extends AsyncTask<Void, Void, String> {
        String ans;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForgetPassword.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try {
                httpConnection obj = new httpConnection();
                ans = obj.ServerData(url3, hashMap);
                Log.d("response", ans);

            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return ans;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

                showMessage("Thanks :)","your feedback save succsessfully");
        }

    }



    public void showMessage(String title, String msg) {
        AlertDialog.Builder ab = new AlertDialog.Builder(ForgetPassword.this);
        ab.setCancelable(true).setTitle(title).setMessage(msg).show();

    }

}
