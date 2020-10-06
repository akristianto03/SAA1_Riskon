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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogActivity extends AppCompatActivity {

    Toolbar toolbarLogin;
    EditText logEmail, logPass;
    Button btnLog;
    String email, pass;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        toolbarLogin = findViewById(R.id.toolbarLogin);
        logEmail = findViewById(R.id.logEmail);
        logPass = findViewById(R.id.logPass);
        btnLog = findViewById(R.id.btnLog);
        fAuth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbarLogin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupTextWatch();
        setupLoginButton();

    }

    private TextWatcher loginWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            email = logEmail.getText().toString().trim();
            pass = logPass.getText().toString().trim();

            if(!email.isEmpty() && !pass.isEmpty()){
                btnLog.setEnabled(true);
            }else {
                btnLog.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setupTextWatch(){
        logEmail.addTextChangedListener(loginWatch);
        logPass.addTextChangedListener(loginWatch);
    }

    private void setupLoginButton(){
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        final Intent in = new Intent(LogActivity.this,MainActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LogActivity.this);

                        final LoadingActivity loadingDialog = new LoadingActivity(LogActivity.this);
                        loadingDialog.startLoading();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.stopLoading();
                                Toast.makeText(LogActivity.this,"Logged in Successfuly",Toast.LENGTH_SHORT).show();
                                startActivity(in,options.toBundle());
                                finish();
                            }
                        },3000);
                    }
                });

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(LogActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LogActivity.this);
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
        intent = new Intent(LogActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LogActivity.this);
        startActivity(intent, options.toBundle());
        finish();
    }

}