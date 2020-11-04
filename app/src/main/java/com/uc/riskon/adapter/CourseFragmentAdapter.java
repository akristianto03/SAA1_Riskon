package com.uc.riskon.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.riskon.LoadingActivity;
import com.uc.riskon.R;
import com.uc.riskon.model.Course;

import java.util.ArrayList;

public class CourseFragmentAdapter extends RecyclerView.Adapter<CourseFragmentAdapter.ViewHolder> {

    private ArrayList<Course> listCourse;
    private Context context;

    public CourseFragmentAdapter(Context context) {this.context = context;}

    public void setCourseFragmentAdapter(ArrayList<Course> listCourse) {
        this.listCourse = listCourse;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView fragCourName, fragCourLec, fragCourDay, fragCourTime;
        Button btnCourseEnroll;
        Dialog dialogLoading;
        DatabaseReference dbStudent;
        boolean con;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fragCourName = itemView.findViewById(R.id.fragCourName);
            fragCourLec = itemView.findViewById(R.id.fragCourLec);
            fragCourDay = itemView.findViewById(R.id.fragCourDay);
            fragCourTime = itemView.findViewById(R.id.fragCourTime);
            btnCourseEnroll = itemView.findViewById(R.id.btnCourseEnroll);
            dialogLoading = LoadingActivity.loadingDialog(context);
            dbStudent = FirebaseDatabase.getInstance().getReference("Student");
        }
    }

    MutableLiveData<Course> enrollCourse = new MutableLiveData<>();

    public MutableLiveData<Course> getEnrollCourse(){
        return enrollCourse;
    }

    @NonNull
    @Override
    public CourseFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_fragment_adapter,parent,false);
        ViewHolder evh = new ViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Course course = listCourse.get(position);
        holder.fragCourName.setText(course.getSubject());
        holder.fragCourLec.setText("Lecturer: "+course.getLecturer());
        holder.fragCourDay.setText("Day: "+course.getDay());
        holder.fragCourTime.setText("Time: "+course.getTimeStart()+" - "+course.getTimeEnd());


        holder.btnCourseEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int timeStartParameter = Integer.parseInt(course.getTimeStart().replace(":",""));
                final int timeEndParameter = Integer.parseInt(course.getTimeEnd().replace(":",""));
                final String dayParameter = course.getDay();

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                holder.con = false;

                holder.dbStudent.child(uid).child("courseId").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                            Course scheduleCourse = childSnapshot.getValue(Course.class);
                            int schTimeStart = Integer.parseInt(scheduleCourse.getTimeStart().replace(":",""));
                            int schTimeEnd = Integer.parseInt(scheduleCourse.getTimeEnd().replace(":",""));
                            String schDay = scheduleCourse.getDay();

                            if (schDay.equalsIgnoreCase(dayParameter)){
                                if (timeStartParameter > schTimeStart && timeStartParameter < schTimeEnd){
                                    holder.con = true;
                                    break;
                                }else if(timeEndParameter > schTimeStart && timeEndParameter < schTimeEnd){
                                    holder.con = true;
                                    break;
                                }else if(timeStartParameter == schTimeStart && timeEndParameter == schTimeEnd){
                                    holder.con = true;
                                    break;
                                }
                            }

                        }


                        if (holder.con == true){
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Course schedule conflict with the others!")
                                    .setIcon(R.drawable.wizardbook)
                                    .setTitle("Warning")
                                    .setPositiveButton("Ok", dialogClickListener).show();
                        }else {
                            enrollCourse.setValue(course);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return listCourse.size();
    }
}