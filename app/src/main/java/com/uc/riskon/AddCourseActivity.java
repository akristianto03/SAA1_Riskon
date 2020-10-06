package com.uc.riskon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

        setupSpinner();
        setupTextWatch();
        setupBackButton();
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

    private void setupBackButton(){
        toolbarAddCourse.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        adapterTime2 = ArrayAdapter.createFromResource(this,R.array.spinTime, android.R.layout.simple_spinner_item);
        adapterLec = ArrayAdapter.createFromResource(this,R.array.spinLec, android.R.layout.simple_spinner_item);
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDay.setAdapter(adapterDay);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTime.setAdapter(adapterTime);
        adapterTime2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTime2.setAdapter(adapterTime2);
        adapterLec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLec.setAdapter(adapterLec);

        spinDay.setOnItemSelectedListener(this);
        spinTime.setOnItemSelectedListener(this);
        spinLec.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}