package com.bincee.driver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bincee.driver.R;
import com.bincee.driver.base.BDialog;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendAlertDialog extends BDialog {
    @BindView(R.id.spinnerSelectMessage)
    AppCompatSpinner spinnerSelectMessage;
    @BindView(R.id.editTextMessage)
    AppCompatEditText editTextMessage;
    @BindView(R.id.buttonSend)
    Button buttonSend;
    @BindView(R.id.buttonCancel)
    TextView buttonCancel;
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;

    public SendAlertDialog(Context context) {
        super(context);
        int layout = R.layout.send_alert_dialog;
        View view = getLayoutInflater().inflate(layout, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.buttonSend)
    public void onButtonSendClicked() {

    }

    @OnClick(R.id.buttonCancel)
    public void onButtonCancelClicked() {
        dismiss();
    }


    @OnClick(R.id.imageViewCross)
    public void onCrossClicked() {
        dismiss();
    }

}
