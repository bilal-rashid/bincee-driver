package com.bincee.driver.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bincee.driver.R;
import com.bincee.driver.base.BDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendNotificationDialog extends BDialog {
    @BindView(R.id.buttonSend)
    Button buttonSend;
    @BindView(R.id.buttonCancel)
    TextView buttonCancel;
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;
    private Listner listner;

    public SendNotificationDialog(Context context) {
        super(context);
        View view = getLayoutInflater().inflate(R.layout.send_notification_dialog, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.buttonSend)
    public void onButtonSendClicked() {
        listner.send();
        dismiss();
    }

    @OnClick(R.id.buttonCancel)
    public void onButtonCancelClicked() {
        listner.cancel();
        dismiss();

    }

    @OnClick(R.id.imageViewCross)
    public void onImageViewCrossClicked() {
        listner.cancel();

        dismiss();

    }

    public SendNotificationDialog setListner(Listner listner) {
        this.listner = listner;
        return this;

    }

    public interface Listner {
        public void send();

        public void cancel();
    }
}
