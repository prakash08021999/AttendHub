package com.example.prakash.copyprint;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.prakash.copyprint.database.TeacherCRUD;
import com.example.prakash.copyprint.database.Techer;
import com.example.prakash.copyprint.loginactivity.SignUpActivity;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by PRAKASH on 04-10-2018.
 */

public class TeacherProfile extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    String path;
    LinearLayout l1;
    private PopupWindow mPopupWindow;
    ImageButton imageButton,nameEdit;
    TextView teacherName;
    CircleImageView imageView;
    String nameE;
    final Pattern Regname=Pattern.compile("^[A-Za-z]+$");
    TeacherCRUD teacherCRUD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.techer_profile);
        imageButton=findViewById(R.id.imageButton);
        nameEdit=findViewById(R.id.nameedit);
        teacherName=findViewById(R.id.textView5);
        l1 = findViewById(R.id.l1);
        teacherName.setText(Techer.TeacherNmae);
        imageView = findViewById(R.id.profile_image);
        teacherCRUD=new TeacherCRUD(getApplicationContext());
        if(Techer.getFaceUri()!=null && Techer.getFaceUri()!=""){
            imageView.setImageBitmap(BitmapFactory.decodeFile(Techer.getFaceUri()));
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImage();
            }
        });
        nameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup();

            }
        });

    }

    public void popup(){

        LayoutInflater layoutInflater=(LayoutInflater)TeacherProfile.this.getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        View customView=layoutInflater.inflate(R.layout.name_edit,null);
        final EditText name=customView.findViewById(R.id.fullName);
        Button submit=customView.findViewById(R.id.submit);
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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Matcher matcher =Regname.matcher(name.getText().toString());
                boolean data=true;
                if (name.getText().toString()==""){
                    name.setError("Enter Something");
                    data=false;
                }
                else  if(!matcher.find()){
                    name.setError("Enter Correct Name");
                    data=false;
                }
                else if (data){
                    nameE=name.getText().toString();
                    teacherName.setText(nameE);
                    Techer.setTeacherNmae(nameE);

                    try {
                        teacherCRUD.open();
                        teacherCRUD.updateTechName();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    finally {
                        teacherCRUD.close();
                    }
                }


            }
        });

    }
    public void setImage(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            path=picturePath;
            Techer.setFaceUri(picturePath);
            try {
                teacherCRUD.open();
                teacherCRUD.updateTechName();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                teacherCRUD.close();
            }
        }

    }
}