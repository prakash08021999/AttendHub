package com.example.prakash.copyprint.recognize;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakash.copyprint.FaceRecognitionCamera;
import com.example.prakash.copyprint.R;

import java.util.ArrayList;

/**
 * Created by PRAKASH on 28-09-2018.
 */

//Horizontal recyclear view


public class AttendenceList extends RecyclerView.Adapter<AttendenceList.ViewHolder> {
   public static  ArrayList<String> alName;
    ArrayList<Bitmap> alImage;
    Context mContext;
    public AttendenceList(Context context,ArrayList<String> alName, ArrayList<Bitmap> alImage){
        super();
        mContext = context;
        this.alName = alName;
        this.alImage = alImage;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendence_list,parent,false);
        view.setOnClickListener(FaceRecognitionCamera.myOnClickListener);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.student_name.setText(alName.get(position));
        holder.stdent_image.setImageBitmap(alImage.get(position));
    }

    @Override
    public int getItemCount() {
        return alName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView student_name;
        ImageView stdent_image;

        public ViewHolder(View itemView) {
            super(itemView);
            student_name=itemView.findViewById(R.id.student_name);
            stdent_image=itemView.findViewById(R.id.student_attendence_image);
        }
    }
}
