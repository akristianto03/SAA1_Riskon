package com.uc.riskon;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.riskon.adapter.StudentAdapter;
import com.uc.riskon.model.Student;

import java.util.ArrayList;

public class StudentData extends AppCompatActivity {

    Toolbar toolbar;
    DatabaseReference dbStudent;
    ArrayList<Student> listStudent = new ArrayList<>();
    RecyclerView recyclerDataStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_data);

        toolbar = findViewById(R.id.toolbarDataStudent);
        recyclerDataStudent = findViewById(R.id.recyclerDataStudent);
        dbStudent = FirebaseDatabase.getInstance().getReference("Student");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fetchStudentData();

    }

    public void  fetchStudentData(){
        dbStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listStudent.clear();
                recyclerDataStudent.setAdapter(null);
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    Student student = childSnapshot.getValue(Student.class);
                    listStudent.add(student);
                }
                showStudentData(listStudent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showStudentData(ArrayList<Student> list){
        recyclerDataStudent.setLayoutManager(new LinearLayoutManager(StudentData.this));
        StudentAdapter studentAdapter = new StudentAdapter(StudentData.this);
        studentAdapter.setStudentAdapter(list);
        recyclerDataStudent.setAdapter(studentAdapter);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(StudentData.this, RegActivity.class);
            intent.putExtra("action", "add");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentData.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(StudentData.this, RegActivity.class);
        intent.putExtra("action", "add");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentData.this);
        startActivity(intent, options.toBundle());
        finish();
    }
}