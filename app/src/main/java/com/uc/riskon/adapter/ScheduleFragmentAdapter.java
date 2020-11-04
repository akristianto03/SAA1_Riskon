package com.uc.riskon.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.riskon.LoadingActivity;
import com.uc.riskon.R;
import com.uc.riskon.model.Course;

import java.util.ArrayList;

public class ScheduleFragmentAdapter extends RecyclerView.Adapter<ScheduleFragmentAdapter.ViewHolder> {

    private ArrayList<Course> listCourse;
    private Context context;

    public ScheduleFragmentAdapter(Context context) {
        this.context = context;
    }

    public void setScheduleFragmentAdapter(ArrayList<Course> listCourse){
        this.listCourse = listCourse;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView fragSchName, fragSchLec, fragSchDay, fragSchTime;
        Button btnScheduleRemove;
        Dialog dialogLoading;
        DatabaseReference dbStudent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fragSchDay = itemView.findViewById(R.id.fragSchDay);
            fragSchLec = itemView.findViewById(R.id.fragSchLec);
            fragSchName = itemView.findViewById(R.id.fragSchName);
            fragSchTime = itemView.findViewById(R.id.fragSchTime);
            btnScheduleRemove = itemView.findViewById(R.id.btnScheduleRemove);
            dialogLoading = LoadingActivity.loadingDialog(context);
            dbStudent = FirebaseDatabase.getInstance().getReference("Student");
        }
    }

    @NonNull
    @Override
    public ScheduleFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_fragment_adapter,parent,false);
        ViewHolder evh = new ViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Course course = listCourse.get(position);
        holder.fragSchName.setText(course.getSubject());
        holder.fragSchLec.setText("Lecturer: "+course.getLecturer());
        holder.fragSchDay.setText("Day: "+course.getDay());
        holder.fragSchTime.setText("Time: "+course.getTimeStart()+" - "+course.getTimeEnd());

        holder.btnScheduleRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                holder.dialogLoading.show();

                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                holder.dbStudent.child(uid).child("courseId").child(course.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        holder.dialogLoading.dismiss();
                                        Toast.makeText(context, "Course Removed From Schedule", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                             case DialogInterface.BUTTON_NEGATIVE:
                                 dialog.dismiss();
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure to remove " + course.getSubject() + " from schedule?")
                        .setIcon(R.drawable.ic_baseline_delete_forever_24)
                        .setTitle("Confirmation")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCourse.size();
    }
}