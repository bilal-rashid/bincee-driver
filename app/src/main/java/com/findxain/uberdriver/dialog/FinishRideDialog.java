package com.findxain.uberdriver.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.findxain.uberdriver.R;
import com.findxain.uberdriver.base.BDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FinishRideDialog extends BDialog {

    @BindView(R.id.buttonLogout)
    Button buttonLogout;
    @BindView(R.id.buttonCancel)
    Button buttonCancel;
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;

    public FinishRideDialog(Context context) {
        super(context);
        int layout = R.layout.dialog_finish_ride;
        View view = getLayoutInflater().inflate(layout, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.buttonLogout)
    public void onButtonLogoutClicked() {
    }

    @OnClick(R.id.buttonCancel)
    public void onButtonCancelClicked() {
        dismiss();

    }

    @OnClick(R.id.imageViewCross)
    public void onImageViewCrossClicked() {
        dismiss();
    }
}
