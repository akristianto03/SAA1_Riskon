package com.uc.riskon;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this);
                startActivity(intent);
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
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupAddCourse(){
        addCourse.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StarterActivity.this, AddCourseActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupLogStudent(){
        logStudent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StarterActivity.this, LogActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this);
                startActivity(intent);
                finish();
            }
        });
    }

    public boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onResume() {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(a);
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(StarterActivity.this, "Press back again to close the apps!", Toast.LENGTH_SHORT).show();
    }

}