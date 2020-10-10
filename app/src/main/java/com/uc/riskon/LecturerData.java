package com.uc.riskon;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.riskon.adapter.LecturerAdapter;
import com.uc.riskon.model.Lecturer;
import com.uc.riskon.utils.ItemClickSupport;

import java.util.ArrayList;

public class LecturerData extends AppCompatActivity {

    Toolbar toolbar;
    DatabaseReference dbLecturer;
    ArrayList<Lecturer> listLecturer = new ArrayList<>();
    RecyclerView recyclerDataLec;


    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_data);

        toolbar = findViewById(R.id.toolbarDataLec);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbLecturer = FirebaseDatabase.getInstance().getReference("Lecturer");
        recyclerDataLec = findViewById(R.id.recyclerDataLec);

        fetchLecturerData();
        setupLectureDetail();

    }

    public void fetchLecturerData(){
        dbLecturer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listLecturer.clear();
                recyclerDataLec.setAdapter(null);
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    Lecturer lecturer = childSnapshot.getValue(Lecturer.class);
                    listLecturer.add(lecturer);
                }
                showLecturerData(listLecturer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showLecturerData(ArrayList<Lecturer> list){
        recyclerDataLec.setLayoutManager(new LinearLayoutManager(LecturerData.this));
        LecturerAdapter lecturerAdapter = new LecturerAdapter(LecturerData.this);
        lecturerAdapter.setLecturerAdapter(list);
        recyclerDataLec.setAdapter(lecturerAdapter);
    }

    private void setupLectureDetail(){
        ItemClickSupport.addTo(recyclerDataLec).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                String id,nama,gender,expert;
                id = listLecturer.get(position).getId();
                nama = listLecturer.get(position).getName();
                gender = listLecturer.get(position).getGender();
                expert = listLecturer.get(position).getExpertise();

                v.startAnimation(klik);
                Intent intent = new Intent(LecturerData.this,DetailLecActivity.class);
                Lecturer lecturer = new Lecturer(id,nama,gender,expert);
                intent.putExtra("dataLec", lecturer);
                intent.putExtra("position", position);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerData.this);
                startActivity(intent);
                finish();

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(LecturerData.this, AddLectActivity.class);
            intent.putExtra("action", "add");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerData.this);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(LecturerData.this, AddLectActivity.class);
        intent.putExtra("action", "add");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerData.this);
        startActivity(intent);
        finish();
    }


}