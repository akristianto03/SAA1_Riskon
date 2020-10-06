package com.uc.riskon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.riskon.model.Lecturer;

import java.util.ArrayList;

public class DetailLecActivity extends AppCompatActivity {

    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    DatabaseReference dbLecturer;

    int pos = 0;
    Lecturer lecturer;

    ArrayList<Lecturer> listLecturer = new ArrayList<>();

    TextView detailLecName, detailLecGen, detailLecExpert;
    Toolbar toolbarLecDetail;
    ImageView btnLecEdit, btnLecDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lec);

        detailLecName = findViewById(R.id.detailLecName);
        detailLecGen = findViewById(R.id.detailLecGen);
        detailLecExpert = findViewById(R.id.detailLecExpert);
        toolbarLecDetail = findViewById(R.id.toolbarLecDetail);
        btnLecEdit = findViewById(R.id.btnLecEdit);
        btnLecDelete = findViewById(R.id.btnLecDelete);

        dbLecturer = FirebaseDatabase.getInstance().getReference("Lecturer");

        setSupportActionBar(toolbarLecDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        pos = intent.getIntExtra("position",0);
        lecturer = intent.getParcelableExtra("dataLec");

        setupDetailLec();
        setupButtonDel();
        setupButtonEdit();

    }

    private void setupDetailLec(){
        detailLecName.setText(lecturer.getName());
        detailLecGen.setText(lecturer.getGender());
        detailLecExpert.setText(lecturer.getExpertise());
    }

    private void setupButtonDel(){
        btnLecDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                dbLecturer.child(lecturer.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    final Intent in = new Intent(DetailLecActivity.this, LecturerData.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    Toast.makeText(DetailLecActivity.this,"Delete Success",Toast.LENGTH_SHORT).show();
                                    final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DetailLecActivity.this);

                                    final LoadingActivity loadingDialog = new LoadingActivity(DetailLecActivity.this);
                                    loadingDialog.startLoading();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            loadingDialog.stopLoading();
                                            startActivity(in,options.toBundle());
                                            finish();
                                        }
                                    },3000);

                                }
                            });
                            break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailLecActivity.this);
                builder.setMessage("Are you sure to delete " + lecturer.getName() + " data?")
                        .setIcon(R.drawable.ic_baseline_delete_forever_24)
                        .setTitle("Connfirmation")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });
    }

    private void setupButtonEdit(){
        btnLecEdit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                Intent in = new Intent(DetailLecActivity.this, AddLectActivity.class);
                in.putExtra("action","edit");
                in.putExtra("editDataLec", lecturer);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DetailLecActivity.this);
                startActivity(in, options.toBundle());
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
            intent = new Intent(DetailLecActivity.this, LecturerData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DetailLecActivity.this);
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
        intent = new Intent(DetailLecActivity.this, LecturerData.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DetailLecActivity.this);
        startActivity(intent, options.toBundle());
        finish();
    }

}