package com.uc.riskon;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        fAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                if (fAuth.getCurrentUser()!=null){
                    Intent start = new Intent(SplashActivity.this, MainActivity.class);
                    Toast.makeText(SplashActivity.this, "Welcome Back!",Toast.LENGTH_SHORT).show();
                    start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
                    startActivity(start);
                    finish();
                }else{
                    Intent start = new Intent(SplashActivity.this, StarterActivity.class);
                    start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
                    startActivity(start);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);

    }
}