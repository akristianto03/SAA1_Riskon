package com.uc.riskon.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uc.riskon.R;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return listStudent.size();
    }
}