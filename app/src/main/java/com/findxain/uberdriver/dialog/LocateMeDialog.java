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

public class LocateMeDialog extends BDialog {
    @BindView(R.id.buttonOK)
    Button buttonOK;
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;

    public LocateMeDialog(Context context) {
        super(context);
        View view = getLayoutInflater().inflate(R.layout.locate_me_dialog, null, false);

        setContentView(view);
        ButterKnife.bind(this, view);

    }

    @OnClick(R.id.buttonOK)
    public void onButtonOKClicked() {
        dismiss();
    }

    @OnClick(R.id.imageViewCross)
    public void onImageViewCrossClicked() {
        dismiss();

    }
}
