package com.bincee.driver.dialog;

import android.content.Context;
import android.view.View;
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
    private Listner listner;

    public SendAlertDialog(Context context) {
        super(context);
        int layout = R.layout.send_alert_dialog;
        View view = getLayoutInflater().inflate(layout, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.buttonSend)
    public void onButtonSendClicked() {

        String text = editTextMessage.getText().toString();
        if (!text.isEmpty()) {
            listner.send(text);
            dismiss();
        }

    }

    @OnClick(R.id.buttonCancel)
    public void onButtonCancelClicked() {
        listner.cancel();
        dismiss();
    }


    @OnClick(R.id.imageViewCross)
    public void onCrossClicked() {
        dismiss();
    }

    public SendAlertDialog setListner(Listner listner) {
        this.listner = listner;
        return this;
    }

    public interface Listner {
        void send(String text);

        void cancel();
    }
}
