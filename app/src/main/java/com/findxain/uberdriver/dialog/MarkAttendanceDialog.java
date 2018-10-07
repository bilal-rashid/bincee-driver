package com.findxain.uberdriver.dialog;

import android.content.Context;
import android.view.View;

import com.findxain.uberdriver.R;
import com.findxain.uberdriver.base.BDialog;

import butterknife.ButterKnife;

public class MarkAttendanceDialog extends BDialog {
    public MarkAttendanceDialog(Context context) {
        super(context);
        View view = getLayoutInflater()
                .inflate(R.layout.dialog_absent_present, null, false);
        setContentView(view);

        ButterKnife.bind(this, view);
    }
}
