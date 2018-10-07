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

public class SelectRouteDialog extends BDialog {

    @BindView(R.id.buttonSend)
    Button buttonSend;
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;

    public SelectRouteDialog(Context context) {
        super(context);
        int layout = R.layout.dialog_select_route;
        View view = getLayoutInflater().inflate(layout, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.buttonSend)
    public void onButtonSendClicked() {
        dismiss();

    }

    @OnClick(R.id.imageViewCross)
    public void onImageViewCrossClicked() {
        dismiss();
    }
}
