package com.uc.riskon.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uc.riskon.R;
import com.uc.riskon.model.Lecturer;

import java.util.ArrayList;

public class LecturerAdapter extends RecyclerView.Adapter<LecturerAdapter.ViewHolder> {

    private ArrayList<Lecturer> listLecturer;
    private Context context;

    public LecturerAdapter(Context context) {
        this.context = context;
    }

    public void setLecturerAdapter(ArrayList<Lecturer> listLecturer){
        this.listLecturer = listLecturer;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView cardLecName,cardLecGen,cardLecExp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardLecName = itemView.findViewById(R.id.cardLecName);
            cardLecGen = itemView.findViewById(R.id.cardLecGen);
            cardLecExp = itemView.findViewById(R.id.cardLecExp);
        }
    }

    @NonNull
    @Override
    public LecturerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecturer_adapter,parent,false);
        ViewHolder evh = new ViewHolder(v);
        return evh;

    }

    @Override
    public void onBindViewHolder(@NonNull LecturerAdapter.ViewHolder holder, int position) {
        final Lecturer lecturer = listLecturer.get(position);
        holder.cardLecName.setText(lecturer.getName());
        holder.cardLecGen.setText(lecturer.getGender());
        holder.cardLecExp.setText(lecturer.getExpertise());
    }

    @Override
    public int getItemCount() {
        return listLecturer.size();
    }
}