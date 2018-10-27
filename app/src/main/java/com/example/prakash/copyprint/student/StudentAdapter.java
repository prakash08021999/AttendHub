package com.example.prakash.copyprint.student;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakash.copyprint.R;
import com.example.prakash.copyprint.database.StudentCRUD;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;


import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 24-09-2018.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {
    private ArrayList<StudentModel> dataSet;
    public static Context context;

    public StudentAdapter(ArrayList<StudentModel> dataSet)
    {
        this.dataSet = dataSet;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_profile_display,parent,false);
        view.setOnClickListener(StudentList.myOnClickListener);
        MyViewHolder myViewHolder= new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        TextView textView=holder.name;
        TextView textClass=holder.studentclass;
        CircleImageView imageStudent=holder.studentProfile;
        File image=new File(dataSet.get(position).getFaceuri());
        if (image.exists()){
            FileInputStream fileInputStream;
            Bitmap bitmap = null;
            try{
                fileInputStream = new FileInputStream(image);
                bitmap = BitmapFactory.decodeStream(fileInputStream);
                fileInputStream.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
            imageStudent.setImageBitmap(bitmap);
        }
        Log.d("StudentAdapter","File Doesn't Exist");
        textView.setText(dataSet.get(position).getName());
        textClass.setText(dataSet.get(position).getStudentclass());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudentModel itemLabel = dataSet.get(position);
                notifyDataSetChanged();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudentModel itemLabel = dataSet.get(position);
                StudentCRUD sc=new StudentCRUD(context);
                try {
                    sc.open();
                    sc.deleteStudent(itemLabel.getId(),itemLabel.getFaceuri());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                finally {
                    sc.close();
                }
                dataSet.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,dataSet.size());

                Toast.makeText(context,"Removed : " + itemLabel.getId(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,studentclass;
        CircleImageView studentProfile;
        ImageButton edit,delete;

        public MyViewHolder(View itemView) {
            super(itemView);
           name=itemView.findViewById(R.id.name);
           studentclass=itemView.findViewById(R.id.studentclass);
           studentProfile=itemView.findViewById(R.id.student);
           edit=itemView.findViewById(R.id.edit);
           delete=itemView.findViewById(R.id.deleat);


        }
    }
}
