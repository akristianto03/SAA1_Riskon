package com.uc.riskon;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import  androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.riskon.model.Student;

import java.util.HashMap;
import java.util.Map;

public class RegActivity extends AppCompatActivity {

    String email="",pass="",fname="",nim="",age="",address="",gender="",action="";

    EditText regEmail, regPass, regFname, regNim, regAge, regAddress;
    RadioGroup rgGen;
    RadioButton regGentext;
    Button btnReg;
    Toolbar toolbarReg;
    TextView titleReg;

    Student student;

    Dialog dialog;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        regEmail = findViewById(R.id.regEmail);
        regPass = findViewById(R.id.regPass);
        regFname = findViewById(R.id.regFname);
        regNim = findViewById(R.id.regNim);
        regAge = findViewById(R.id.regAge);
        regAddress = findViewById(R.id.regAddress);
        rgGen = findViewById(R.id.rgGen);
        btnReg = findViewById(R.id.btnReg);
        toolbarReg = findViewById(R.id.toolbarRegister);
        titleReg = findViewById(R.id.titleReg);

        dialog = LoadingActivity.loadingDialog(RegActivity.this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Student");
        fAuth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbarReg);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupTextWatch();
        setupRadio();
        setupAddorEdit();
    }

    private TextWatcher registerTextWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            email = regEmail.getText().toString().trim();
            pass = regPass.getText().toString().trim();
            fname = regFname.getText().toString().trim();
            nim = regNim.getText().toString().trim();
            age = regAge.getText().toString().trim();
            address = regAddress.getText().toString().trim();

            if(!email.isEmpty() && !pass.isEmpty() && !fname.isEmpty() && !nim.isEmpty() && !age.isEmpty() && !address.isEmpty() && !gender.isEmpty()){
                btnReg.setEnabled(true);
            }else {
                btnReg.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setupTextWatch(){
        //textwatcher
        regEmail.addTextChangedListener(registerTextWatch);
        regPass.addTextChangedListener(registerTextWatch);
        regFname.addTextChangedListener(registerTextWatch);
        regNim.addTextChangedListener(registerTextWatch);
        regAge.addTextChangedListener(registerTextWatch);
        regAddress.addTextChangedListener(registerTextWatch);
    }

    private void setupRadio(){
        rgGen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                regGentext = findViewById(checkedId);
                if (regGentext.getText().toString().equalsIgnoreCase("M")){
                    gender = "Male";
                }else{gender = "Female";}
            }
        });
    }

    private void setupAddorEdit(){
        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if(action.equals("add")){
            titleReg.setText("REGISTER");
            btnReg.setText("Register");

            btnReg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    if (TextUtils.isEmpty(email)){
//                        regEmail.setError("Email is Required!");
//                        return;
//                    }
//
//                    if(TextUtils.isEmpty(pass)){
//                        regPass.setError("Password is Required!");
//                        return;
//                    }

                    if (pass.length()<6){
                        regPass.setError("Password must be at least 6 Characters!");
                        return;
                    }else{
                        dialog.show();
                    }

                    fAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(RegActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String id = fAuth.getCurrentUser().getUid();

                                Student student = new Student(id,email,pass,fname,nim,age,address,gender);
                                reference.child(id).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                         dialog.cancel();
                                         Toast.makeText(RegActivity.this,"Add Student Successfuly",Toast.LENGTH_SHORT).show();

                                         regEmail.setText("");
                                         regPass.setText("");
                                         regFname.setText("");
                                         regNim.setText("");
                                         regAge.setText("");
                                         regAddress.setText("");
                                         rgGen.check(R.id.regGenM);

                                    }
                                });
                                fAuth.signOut();
                            }else{
                                dialog.cancel();
                                try {
                                    throw task.getException();
                                }catch (FirebaseAuthInvalidCredentialsException malFormed){
                                    Toast.makeText(RegActivity.this,"Invalid email!",Toast.LENGTH_SHORT).show();
                                }catch (FirebaseAuthUserCollisionException existEmail){
                                    Toast.makeText(RegActivity.this,"Email already registered!",Toast.LENGTH_SHORT).show();
                                }catch (Exception e){
                                    Toast.makeText(RegActivity.this,"Register failed!",Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });

                }
            });

        }else{//editActivity
            titleReg.setText("EDIT");
            btnReg.setText("edit");
            student = intent.getParcelableExtra("editDataStudent");

            regEmail.setText(student.getEmail());
            regPass.setText(student.getPass());
            regEmail.setFocusable(false);
            regEmail.setTextColor(Color.parseColor("#aaaaaa"));
            regPass.setFocusable(false);
            regPass.setTextColor(Color.parseColor("#aaaaaa"));

            regFname.setText(student.getFname());
            regNim.setText(student.getNim());
            regAge.setText(student.getAge());
            regAddress.setText(student.getAddress());
            if(student.getGender().equalsIgnoreCase("Male")){
                rgGen.check(R.id.regGenM);
            }else{
                rgGen.check(R.id.regGenF);
            }

            btnReg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.show();
                    Map<String,Object> params = new HashMap<>();
                    params.put("fname", fname);
                    params.put("address", address);
                    params.put("age", age);
                    params.put("gender", gender);
                    params.put("nim", nim);

                    reference.child(student.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onSuccess(Void aVoid) {

                            //KARNA GAPAKE UPDATE EMAIL & PASSWORD//
//                            fAuth.signInWithEmailAndPassword(student.getEmail(),student.getPass()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    fAuth.getCurrentUser().updateEmail(email);
//                                    fAuth.getCurrentUser().updatePassword(pass);
//
//                                    fAuth.signOut();
//                                }
//                            });

                            Intent in;
                            if (action.equals("editlogin")){
                                in = new Intent(RegActivity.this, MainActivity.class);
                            }else{
                                in = new Intent(RegActivity.this,StudentData.class);
                            }

                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            dialog.cancel();
                            Toast.makeText(RegActivity.this,"Edit Student Successfuly",Toast.LENGTH_SHORT).show();
                            startActivity(in);
                        }
                    });
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        if(action.equals("editlogin")){

        }else{
            getMenuInflater().inflate(R.menu.lecturer_menu, menu);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.lecturerList){
            Intent intent;
            intent = new Intent(RegActivity.this, StudentData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegActivity.this);
            startActivity(intent);
            finish();
            return true;
        }else if(id == android.R.id.home){
            if(action.equals("add")){
                Intent intent;
                intent = new Intent(RegActivity.this,StarterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegActivity.this);
                startActivity(intent);
                finish();
            }else if (action.equals("edit")){
                Intent intent;
                intent = new Intent(RegActivity.this,StudentData.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegActivity.this);
                startActivity(intent);
                finish();
            }else{
                Intent intent;
                intent = new Intent(RegActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegActivity.this);
                startActivity(intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        if(action.equals("add")){
            Intent intent;
            intent = new Intent(RegActivity.this,StarterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegActivity.this);
            startActivity(intent);
            finish();
        }else if (action.equals("edit")){
            Intent intent;
            intent = new Intent(RegActivity.this,StudentData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegActivity.this);
            startActivity(intent);
            finish();
        }else{
            Intent intent;
            intent = new Intent(RegActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegActivity.this);
            startActivity(intent);
            finish();
        }
    }

}