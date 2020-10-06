package com.uc.riskon;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.riskon.model.Lecturer;

import java.util.HashMap;
import java.util.Map;

public class AddLectActivity extends AppCompatActivity {

    Toolbar toolbarAddLec;
    EditText lecName,lecExpert;
    RadioGroup rgGenLec;
    RadioButton rgGentext;
    Button btnAddLec;
    String name="",expert="",gender="",action="";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;

    Lecturer lecturer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lect);

        lecName = findViewById(R.id.lecName);
        lecExpert = findViewById(R.id.lecExpert);
        rgGenLec = findViewById(R.id.rgGenLec);
        toolbarAddLec = findViewById(R.id.toolbarAddLec);
        btnAddLec = findViewById(R.id.btnAddLec);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Lecturer");

        setSupportActionBar(toolbarAddLec);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupTextWatch();
        setupRadio();
        setupAddorEdit();

    }

    private TextWatcher addLecWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            name = lecName.getText().toString().trim();
            expert = lecExpert.getText().toString().trim();

            if(!name.isEmpty() && !expert.isEmpty() && !gender.isEmpty()){
                btnAddLec.setEnabled(true);
            }else {
                btnAddLec.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setupTextWatch(){
        lecName.addTextChangedListener(addLecWatch);
        lecExpert.addTextChangedListener(addLecWatch);
    }

    private void setupRadio(){
        rgGenLec.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rgGentext = findViewById(checkedId);
                if (rgGentext.getText().toString().equalsIgnoreCase("M")){
                    gender = "Male";
                }else{gender = "Female";}
            }
        });
    }

    public void setupAddorEdit(){
        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if(action.equals("add")){
            getSupportActionBar().setTitle("Add Lecturer");
            btnAddLec.setText("Add Lecturer");

            btnAddLec.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    String id = reference.child("Lecturer").push().getKey();

                    Lecturer lecturer = new Lecturer(id, name, gender, expert);
                    reference.child(id).setValue(lecturer).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            final LoadingActivity loadingDialog = new LoadingActivity(AddLectActivity.this);
                            loadingDialog.startLoading();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.stopLoading();
                                    Toast.makeText(AddLectActivity.this,"Add Lecture Successfuly",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            },3000);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            final LoadingActivity loadingDialog = new LoadingActivity(AddLectActivity.this);
                            loadingDialog.startLoading();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.stopLoading();
                                    Toast.makeText(AddLectActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            },2000);
                        }
                    });
                }
            });
        }else{ //editLecActivity
            getSupportActionBar().setTitle("Edit Lecturer");
            lecturer = intent.getParcelableExtra("editDataLec");

            lecName.setText(lecturer.getName());
            lecExpert.setText(lecturer.getExpertise());
            if(lecturer.getGender().equalsIgnoreCase("Male")){
                rgGenLec.check(R.id.regGenM);
            }else{
                rgGenLec.check(R.id.regGenF);
            }
            btnAddLec.setText("Edit Lecturer");

            btnAddLec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String,Object> params = new HashMap<>();
                    params.put("name", name);
                    params.put("expertise", expert);
                    params.put("gender", gender);

                    reference.child(lecturer.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onSuccess(Void aVoid) {
                            final Intent in = new Intent(AddLectActivity.this,LecturerData.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddLectActivity.this);

                            final LoadingActivity loadingDialog = new LoadingActivity(AddLectActivity.this);
                            loadingDialog.startLoading();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.startLoading();
                                    Toast.makeText(AddLectActivity.this,"Edit Lecture Successfuly",Toast.LENGTH_SHORT).show();
                                    startActivity(in,options.toBundle());
                                }
                            },3000);
                        }
                    });
                }
            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.lecturer_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.lecturerList){
            Intent intent;
            intent = new Intent(AddLectActivity.this, LecturerData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddLectActivity.this);
            startActivity(intent, options.toBundle());

            return true;
        }else if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(AddLectActivity.this,StarterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddLectActivity.this);
            startActivity(intent,options.toBundle());

        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(AddLectActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddLectActivity.this);
        startActivity(intent, options.toBundle());
        finish();
    }


}