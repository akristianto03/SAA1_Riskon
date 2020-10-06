package com.uc.riskon;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;

public class LoadingActivity{

    private Activity activity;
    private AlertDialog dialog;

    public LoadingActivity(Activity activity){
        this.activity = activity;
    }

    public void startLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_loading, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();;
    }

    public void stopLoading(){
        dialog.dismiss();;
    }

}