package com.uc.riskon.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.riskon.R;
import com.uc.riskon.adapter.ScheduleFragmentAdapter;
import com.uc.riskon.model.Course;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment {

    DatabaseReference dbStudent;
    ArrayList<Course> listCourse = new ArrayList<>();
    RecyclerView recyclerDataSchedule;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_schedule,container,false);
        recyclerDataSchedule = view.findViewById(R.id.recScheduleFragment);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbStudent = FirebaseDatabase.getInstance().getReference("Student").child(uid).child("courseId");

        fetchScheduleData();

        return view;
    }

    public void fetchScheduleData(){
        dbStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCourse.clear();
                recyclerDataSchedule.setAdapter(null);
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    Course course = childSnapshot.getValue(Course.class);
                    listCourse.add(course);
                }
                showScheduleData(listCourse);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showScheduleData(ArrayList<Course> list){
        recyclerDataSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        ScheduleFragmentAdapter scheduleFragmentAdapter = new ScheduleFragmentAdapter(getContext());
        scheduleFragmentAdapter.setScheduleFragmentAdapter(list);
        recyclerDataSchedule.setAdapter(scheduleFragmentAdapter);

    }

}