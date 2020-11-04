package com.uc.riskon.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.riskon.R;
import com.uc.riskon.adapter.CourseFragmentAdapter;
import com.uc.riskon.model.Course;

import java.util.ArrayList;

public class CoursesFragment extends Fragment {

    DatabaseReference dbCourse;
    ArrayList<Course> listCourse = new ArrayList<>();
    RecyclerView recyclerDataCourse;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_courses,container,false);

        recyclerDataCourse = view.findViewById(R.id.recCourseFragment);
        dbCourse = FirebaseDatabase.getInstance().getReference("Course");

        fetchCourseData();

        return view;
    }

    public void fetchCourseData(){
        dbCourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCourse.clear();
                recyclerDataCourse.setAdapter(null);
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    Course course = childSnapshot.getValue(Course.class);
                    listCourse.add(course);
                }
                showCourseData(listCourse);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showCourseData(final ArrayList<Course> list){
        recyclerDataCourse.setLayoutManager(new LinearLayoutManager(getContext()));
        CourseFragmentAdapter courseFragmentAdapter = new CourseFragmentAdapter(getContext());
        courseFragmentAdapter.setCourseFragmentAdapter(list);
        recyclerDataCourse.setAdapter(courseFragmentAdapter);

        final Observer<Course> courseObserver = new Observer<Course>() {
            @Override
            public void onChanged(Course course) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference("Student").child(uid).child("courseId").child(course.getId())
                        .setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Add course to schedule successful.")
                                .setIcon(R.drawable.wizardbook)
                                .setTitle("Success")
                                .setPositiveButton("Ok", dialogClickListener).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Add Course Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        courseFragmentAdapter.getEnrollCourse().observe(this, courseObserver);
    }

}