package com.uc.riskon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uc.riskon.fragment.AccountFragment;
import com.uc.riskon.fragment.CoursesFragment;
import com.uc.riskon.fragment.ScheduleFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navlist);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CoursesFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlist =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_courses:
                            selectedFragment = new CoursesFragment();
                            break;
                        case R.id.nav_schedule:
                            selectedFragment = new ScheduleFragment();
                            break;
                        case R.id.nav_account:
                            selectedFragment = new AccountFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };
}