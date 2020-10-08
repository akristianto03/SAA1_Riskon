package com.uc.riskon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class LoadingActivity{

    public LoadingActivity(){

    }

    public static final Dialog loadingDialog(Context context){
        final Dialog dialog = new Dialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.activity_loading,null);
        dialog.setContentView(dialogView);
        dialog.setCancelable(false);
        return dialog;
    }

}