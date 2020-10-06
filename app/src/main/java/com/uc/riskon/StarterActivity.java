package com.uc.riskon;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class StarterActivity extends AppCompatActivity {

    ImageView addStudent, addLecturer, addCourse, logStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        addStudent = findViewById(R.id.addStudent);
        addLecturer = findViewById(R.id.addLecturer);
        addCourse = findViewById(R.id.addCourse);
        logStudent = findViewById(R.id.logStudent);

        setupAddStudent();
        setupAddLecturer();
        setupAddCourse();
        setupLogStudent();

    }

    private void setupAddStudent(){
        addStudent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StarterActivity.this, RegActivity.class);
                intent.putExtra("action","add");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this);
                startActivity(intent,options.toBundle());
                finish();
            }
        });
    }

    private void setupAddLecturer(){
        addLecturer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StarterActivity.this, AddLectActivity.class);
                intent.putExtra("action","add");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this);
                startActivity(intent,options.toBundle());
                finish();
            }
        });
    }

    private void setupAddCourse(){
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StarterActivity.this, AddCourseActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupLogStudent(){
        logStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StarterActivity.this, LogActivity.class);
                startActivity(intent);
            }
        });
    }

}