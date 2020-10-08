package com.uc.riskon.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


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
import com.uc.riskon.R;

public class AccountFragment extends Fragment {

    TextView accName,accNim,accEmail,accGender,accAge,accAddress;
    Button btnLogOut;
    FirebaseAuth fAuth;
    DatabaseReference fBase;

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

        fAuth = FirebaseAuth.getInstance();
        fBase = FirebaseDatabase.getInstance().getReference("Student");

        setupAcc();

        return view;
    }

    private void setupAcc(){
        uid = fAuth.getCurrentUser().getUid();
        fBase.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name,nim,email,gender,age,address;
                    name = (String) dataSnapshot.child("fname").getValue();
                    nim = (String) dataSnapshot.child("nim").getValue();
                    email = (String) dataSnapshot.child("email").getValue();
                    gender = (String) dataSnapshot.child("gender").getValue();
                    age = (String) dataSnapshot.child("age").getValue() + " years old";
                    address = (String) dataSnapshot.child("address").getValue();

                    accName.setText(name);
                    accNim.setText(nim);
                    accEmail.setText(email);
                    accGender.setText(gender);
                    accAge.setText(age);
                    accAddress.setText(address);

                }else{
                    Toast.makeText(getActivity(), "document doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}