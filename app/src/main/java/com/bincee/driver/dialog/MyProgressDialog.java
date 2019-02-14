package com.bincee.driver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.bincee.driver.R;
import com.bincee.driver.base.BDialog;

public class MyProgressDialog extends BDialog {
    public MyProgressDialog(@NonNull Context context) {
        super(context);

        View view = getLayoutInflater().inflate(R.layout.progress_layout, null, false);
        setContentView(view);
    }


    public void setMessage(String creating_route) {

    }
}
