package com.uc.riskon.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.riskon.AddCourseActivity;
import com.uc.riskon.CourseData;
import com.uc.riskon.LoadingActivity;
import com.uc.riskon.R;
import com.uc.riskon.model.Course;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private ArrayList<Course> listCourse;
    private Context context;

    public CourseAdapter(Context context) {
        this.context = context;
    }

    public void setCourseAdapter(ArrayList<Course> listCourse){
        this.listCourse = listCourse;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView cardCourName, cardCourLec, cardCourDay, cardCourTime;
        ImageView btnEditCourse, btnDelCourse;
        Dialog dialogLoading;
        DatabaseReference dbCourse;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardCourName = itemView.findViewById(R.id.cardCourName);
            cardCourLec = itemView.findViewById(R.id.cardCourLec);
            cardCourDay = itemView.findViewById(R.id.cardCourDay);
            cardCourTime = itemView.findViewById(R.id.cardCourTime);
            btnEditCourse = itemView.findViewById(R.id.btnEditCourse);
            btnDelCourse = itemView.findViewById(R.id.btnDelCourse);
            dialogLoading = LoadingActivity.loadingDialog(context);
            dbCourse = FirebaseDatabase.getInstance().getReference("Course");
        }
    }

    @NonNull
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_adapter,parent,false);
        ViewHolder evh = new ViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Course course = listCourse.get(position);
        holder.cardCourName.setText(course.getSubject());
        holder.cardCourLec.setText("Lecturer: "+course.getLecturer());
        holder.cardCourDay.setText("Day: "+course.getDay());
        holder.cardCourTime.setText("Time: "+course.getTimeStart()+" - "+course.getTimeEnd());

        holder.btnDelCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case  DialogInterface.BUTTON_POSITIVE:
                                holder.dialogLoading.show();
                                holder.dbCourse.child(course.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                        Intent in = new Intent(context, CourseData.class);
                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        holder.dialogLoading.cancel();
                                        Toast.makeText(context, "Delete Course Success",Toast.LENGTH_SHORT).show();
                                        context.startActivity(in);
                                        ((Activity)context).finish();

                                    }
                                });
                                break;

                             case  DialogInterface.BUTTON_NEGATIVE:
                                 dialog.dismiss();
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure to delete " + course.getSubject() + " course?")
                        .setIcon(R.drawable.ic_baseline_delete_forever_24)
                        .setTitle("Confirmation")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        holder.btnEditCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, AddCourseActivity.class);
                in.putExtra("action", "edit");
                in.putExtra("editDataCourse", course);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(in);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCourse.size();
    }
}