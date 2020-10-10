package com.uc.riskon;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.widget.Toolbar;

public class AddCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinDay, spinTime, spinTime2, spinLec;
    ArrayAdapter<CharSequence> adapterDay;
    ArrayAdapter<CharSequence> adapterTime;
    ArrayAdapter<CharSequence> adapterTime2;
    ArrayAdapter<CharSequence> adapterLec;

    Toolbar toolbarAddCourse;
    EditText courSubject;
    Button btnAddCourse;
    String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        toolbarAddCourse = findViewById(R.id.toolbarAddCourse);
        courSubject = findViewById(R.id.courSubject);
        btnAddCourse = findViewById(R.id.btnAddCourse);

        setSupportActionBar(toolbarAddCourse);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        setupSpinner();
        setupTextWatch();
        setupBtnAddCourse();

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

    private void setupBtnAddCourse(){
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setupSpinner(){
        spinDay = findViewById(R.id.spinDay);
        spinTime = findViewById(R.id.spinTime);
        spinLec = findViewById(R.id.spinLec);
        spinTime2 = findViewById(R.id.spinTime2);
        adapterDay = ArrayAdapter.createFromResource(this,R.array.spinDay, android.R.layout.simple_spinner_item);
        adapterTime = ArrayAdapter.createFromResource(this,R.array.spinTime, android.R.layout.simple_spinner_item);
        adapterLec = ArrayAdapter.createFromResource(this,R.array.spinLec, android.R.layout.simple_spinner_item);
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDay.setAdapter(adapterDay);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTime.setAdapter(adapterTime);
        adapterLec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLec.setAdapter(adapterLec);

        spinDay.setOnItemSelectedListener(this);
        spinLec.setOnItemSelectedListener(this);

        spinTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(AddCourseActivity.this, StarterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourseActivity.this);
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
        intent = new Intent(AddCourseActivity.this, StarterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourseActivity.this);
        startActivity(intent, options.toBundle());
        finish();
    }
}