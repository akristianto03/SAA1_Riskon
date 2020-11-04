package com.uc.riskon;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.riskon.model.Course;
import com.uc.riskon.model.Lecturer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinDay, spinTime, spinTime2, spinLec;
    ArrayAdapter<CharSequence> adapterDay;
    ArrayAdapter<CharSequence> adapterTime;
    ArrayAdapter<CharSequence> adapterTime2;
    ArrayAdapter<String> adapterLec;

    TextView titleCourse;
    Toolbar toolbarAddCourse;
    EditText courSubject;
    Button btnAddCourse;
    String subject = "", day = "", timestart = "", timeeend = "", lec = "", action = "";

    Course course;

    Dialog dialog;
    boolean con = false;

    private DatabaseReference dbLecturer;
    private DatabaseReference dbCourse;
    List<String> listLecturer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        titleCourse = findViewById(R.id.textViewcon);
        toolbarAddCourse = findViewById(R.id.toolbarAddCourse);
        courSubject = findViewById(R.id.courSubject);
        btnAddCourse = findViewById(R.id.btnAddCourse);
        dialog = LoadingActivity.loadingDialog(AddCourseActivity.this);

        dbLecturer = FirebaseDatabase.getInstance().getReference("Lecturer");
        dbCourse = FirebaseDatabase.getInstance().getReference("Course");

        setSupportActionBar(toolbarAddCourse);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        setupSpinner();
        setupTextWatch();
        setupAddorEdit();

    }

    private TextWatcher addCourWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            subject = courSubject.getText().toString().trim();

            if(!subject.isEmpty()){
                btnAddCourse.setEnabled(true);
            }else {
                btnAddCourse.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setupTextWatch(){
        courSubject.addTextChangedListener(addCourWatch);
    }

    private void setupAddorEdit(){
        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if (action.equals("add")){
            titleCourse.setText("Add Course");
            btnAddCourse.setText("Add Course");

            btnAddCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    String id = dbCourse.child("Course").push().getKey();

                    Course course = new Course(id,subject,day,timestart,timeeend,lec);
                    dbCourse.child(id).setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.cancel();
                            Toast.makeText(AddCourseActivity.this, "Add Course Successfuly", Toast.LENGTH_SHORT).show();
                            courSubject.setText("");
                            spinDay.setSelection(0);
                            spinTime.setSelection(0);
                            spinTime2.setSelection(0);
                            spinLec.setSelection(0);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.cancel();
                            Toast.makeText(AddCourseActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }else{ //edit activity
            titleCourse.setText("Edit Course");
            btnAddCourse.setText("Edit Course");
            course = intent.getParcelableExtra("editDataCourse");

            courSubject.setText(course.getSubject());
            int dayPosition = adapterDay.getPosition(course.getDay());
            int timePosition = adapterTime.getPosition(course.getTimeStart());

            spinTime.setSelection(timePosition);
            spinDay.setSelection(dayPosition);

            conditionPositionEnd(timePosition);
            final int time2Position = adapterTime2.getPosition(course.getTimeEnd());

            Toast.makeText(AddCourseActivity.this, String.valueOf(time2Position),Toast.LENGTH_SHORT).show();
            spinTime2.setSelection(time2Position, false);


            Log.d("end",course.getTimeEnd());
            Log.d("ends", String.valueOf(time2Position));

            btnAddCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    Map<String,Object> params = new HashMap<>();
                    params.put("day", day);
                    params.put("lecturer", lec);
                    params.put("subject", subject);
                    params.put("timeEnd", timeeend);
                    params.put("timeStart", timestart);

                    dbCourse.child(course.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent in = new Intent(AddCourseActivity.this, CourseData.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            dialog.cancel();
                            Toast.makeText(AddCourseActivity.this, "Edit Course Successfuly", Toast.LENGTH_SHORT).show();
                            startActivity(in);
                        }
                    });
                }
            });
        }
    }

    private void setupSpinner(){
        fetchDataLec();
        spinDay = findViewById(R.id.spinDay);
        spinTime = findViewById(R.id.spinTime);
        spinLec = findViewById(R.id.spinLec);
        spinTime2 = findViewById(R.id.spinTime2);
        adapterDay = ArrayAdapter.createFromResource(this,R.array.spinDay, android.R.layout.simple_spinner_item);
        adapterTime = ArrayAdapter.createFromResource(this,R.array.spinTime, android.R.layout.simple_spinner_item);
        adapterLec = new ArrayAdapter<>(AddCourseActivity.this,android.R.layout.simple_spinner_dropdown_item,listLecturer);
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDay.setAdapter(adapterDay);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTime.setAdapter(adapterTime);
        adapterLec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLec.setAdapter(adapterLec);

        //set edit spinlec
        if (action.equals("edit")){
            int lecPosition = adapterLec.getPosition(course.getLecturer());
            spinLec.setSelection(lecPosition);
        }

        spinLec.setOnItemSelectedListener(this);
        spinDay.setOnItemSelectedListener(this);


        spinTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(action.equals("add")){
                    conditionPositionEnd(position);
                }else{
                    while (con==true){
                        conditionPositionEnd(position);
                        break;
                    }
                    con = true;
                }
                timestart = spinTime.getSelectedItem().toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //fetch data spinner

        spinDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = parent.getItemAtPosition(position).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinTime2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeeend = spinTime2.getSelectedItem().toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinLec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lec = parent.getItemAtPosition(position).toString().trim();;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void conditionPositionEnd(int position){
        if(position==0){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end0730, android.R.layout.simple_spinner_item);
        }else if(position==1){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end0800, android.R.layout.simple_spinner_item);
        }else if(position==2){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end0830, android.R.layout.simple_spinner_item);
        }else if(position==3){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end0900, android.R.layout.simple_spinner_item);
        }else if(position==4){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end0930, android.R.layout.simple_spinner_item);
        }else if(position==5){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1000, android.R.layout.simple_spinner_item);
        }else if(position==6){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1030, android.R.layout.simple_spinner_item);
        }else if(position==7){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1100, android.R.layout.simple_spinner_item);
        }else if(position==8){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1130, android.R.layout.simple_spinner_item);
        }else if(position==9){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1200, android.R.layout.simple_spinner_item);
        }else if(position==10){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1230, android.R.layout.simple_spinner_item);
        }else if(position==11){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1300, android.R.layout.simple_spinner_item);
        }else if(position==12){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1330, android.R.layout.simple_spinner_item);
        }else if(position==13){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1400, android.R.layout.simple_spinner_item);
        }else if(position==14){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1430, android.R.layout.simple_spinner_item);
        }else if(position==15){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1500, android.R.layout.simple_spinner_item);
        }else if(position==16){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1530, android.R.layout.simple_spinner_item);
        }else if(position==17){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1600, android.R.layout.simple_spinner_item);
        }else if(position==18){
            adapterTime2 = ArrayAdapter.createFromResource(AddCourseActivity.this, R.array.jam_end1630, android.R.layout.simple_spinner_item);
        }

        adapterTime2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTime2.setAdapter(adapterTime2);
    }

    private void fetchDataLec(){
        listLecturer = new ArrayList<>();
        dbLecturer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listLecturer.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    Lecturer lecturer = childSnapshot.getValue(Lecturer.class);
                    String name = lecturer.getName();
                    listLecturer.add(name);
                }
                adapterLec.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lecturer_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lecturerList){
            Intent intent;
            intent = new Intent(AddCourseActivity.this, CourseData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }else if(id == android.R.id.home){
            if (action.equals("add")) {
                Intent intent;
                intent = new Intent(AddCourseActivity.this, StarterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourseActivity.this);
                startActivity(intent);
                finish();
            }else{
                Intent intent;
                intent = new Intent(AddCourseActivity.this, CourseData.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        if (action.equals("add")) {
            Intent intent;
            intent = new Intent(AddCourseActivity.this, StarterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourseActivity.this);
            startActivity(intent);
            finish();
        }else{
            Intent intent;
            intent = new Intent(AddCourseActivity.this, CourseData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}