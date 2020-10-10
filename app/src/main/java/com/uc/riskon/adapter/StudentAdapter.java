package com.uc.riskon.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.riskon.LoadingActivity;
import com.uc.riskon.R;
import com.uc.riskon.RegActivity;
import com.uc.riskon.StudentData;
import com.uc.riskon.model.Student;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private ArrayList<Student> listStudent;
    private Context context;

    public StudentAdapter(Context context) {
        this.context = context;
    }

    public void setStudentAdapter(ArrayList<Student> listStudent){
        this.listStudent = listStudent;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView cardStuName, cardStuNim, cardStuGen, cardStuEmail,cardStuAddress, cardStuAge;
        public ImageView btnEditStudent, btnDelStudent;
        Dialog dialogLoading;
        DatabaseReference dbStudent;
        FirebaseAuth fAuth;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardStuName = itemView.findViewById(R.id.cardStuName);
            cardStuNim = itemView.findViewById(R.id.cardStuNim);
            cardStuGen = itemView.findViewById(R.id.cardStuGen);
            cardStuEmail = itemView.findViewById(R.id.cardStuEmail);
            cardStuAddress = itemView.findViewById(R.id.cardStuAddress);
            cardStuAge = itemView.findViewById(R.id.cardStuAge);
            btnEditStudent = itemView.findViewById(R.id.btnEditStudent);
            btnDelStudent = itemView.findViewById(R.id.btnDelStudent);
            dialogLoading = LoadingActivity.loadingDialog(context);
            dbStudent = FirebaseDatabase.getInstance().getReference("Student");
            fAuth = FirebaseAuth.getInstance();

        }
    }

    @NonNull
    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_adapter,parent,false);
        ViewHolder evh = new ViewHolder(v);
        return evh;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Student student = listStudent.get(position);
        holder.cardStuName.setText(student.getFname());
        holder.cardStuNim.setText(student.getNim());
        holder.cardStuEmail.setText(student.getEmail());
        holder.cardStuAddress.setText(student.getAddress());
        holder.cardStuAge.setText(student.getAge());

        if (student.getGender().equalsIgnoreCase("Male")){
            holder.cardStuGen.setText("M");
        }else{
            holder.cardStuGen.setText("F");
        }

        holder.btnDelStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                holder.dialogLoading.show();
                                holder.dbStudent.child(student.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                        holder.fAuth.signInWithEmailAndPassword(student.getEmail(),student.getPass()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                holder.fAuth.getCurrentUser().delete();
                                            }
                                        });

                                        Intent in = new Intent(context, StudentData.class);
                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        holder.dialogLoading.cancel();
                                        Toast.makeText(context,"Delete Student Success",Toast.LENGTH_SHORT).show();
//                                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context);
                                        context.startActivity(in);
                                        ((Activity)context).finish();

                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure to delete " + student.getFname() + " data?")
                        .setIcon(R.drawable.ic_baseline_delete_forever_24)
                        .setTitle("Connfirmation")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        holder.btnEditStudent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, RegActivity.class);
                in.putExtra("action","edit");
                in.putExtra("editDataStudent", student);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context);
                context.startActivity(in);
                ((Activity)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listStudent.size();
    }
}