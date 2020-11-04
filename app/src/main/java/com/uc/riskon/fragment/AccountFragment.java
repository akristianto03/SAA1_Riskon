package com.uc.riskon.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.riskon.LoadingActivity;
import com.uc.riskon.R;
import com.uc.riskon.RegActivity;
import com.uc.riskon.StarterActivity;
import com.uc.riskon.model.Student;

public class AccountFragment extends Fragment {

    TextView accName,accNim,accEmail,accGender,accAge,accAddress;
    Button btnLogOut, btnEdit;
    FirebaseAuth fAuth;
    DatabaseReference fBase;
    Student student;

    Dialog dialogLoading;

    String uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_account,container,false);

        accName = view.findViewById(R.id.accName);
        accNim = view.findViewById(R.id.accNim);
        accEmail = view.findViewById(R.id.accEmail);
        accGender = view.findViewById(R.id.accGender);
        accAge = view.findViewById(R.id.accAge);
        accAddress = view.findViewById(R.id.accAddress);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnEdit = view.findViewById(R.id.btnEditFragAcc);

        dialogLoading = LoadingActivity.loadingDialog(getActivity());

        fAuth = FirebaseAuth.getInstance();
        fBase = FirebaseDatabase.getInstance().getReference("Student");

        setupAcc();
        setupBtnLogOut();
        setupBtnEdit();

        return view;
    }

    private void setupAcc(){
        uid = fAuth.getCurrentUser().getUid();
        fBase.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    student = dataSnapshot.getValue(Student.class);

                    accName.setText(student.getFname());
                    accNim.setText(student.getNim());
                    accEmail.setText(student.getEmail());
                    accGender.setText(student.getGender());
                    accAge.setText(student.getAge());
                    accAddress.setText(student.getAddress());

                }else{
                    Toast.makeText(getActivity(), "document doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupBtnLogOut(){
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                dialogLoading.show();
                                fAuth.signOut(); //logout
                                Intent intent = new Intent(getActivity(),StarterActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity());

                                dialogLoading.cancel();
                                Toast.makeText(getActivity(), "Sign Out", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                getActivity().finish();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure to Sign Out?")
                        .setIcon(R.drawable.ic_baseline_bubble_chart_24)
                        .setTitle("Confirmation")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });
    }

    private void setupBtnEdit() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("action", "editlogin");
                intent.putExtra("editDataStudent", student);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

}