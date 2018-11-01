package com.findxain.uberdriver.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.findxain.uberdriver.R;
import com.findxain.uberdriver.base.BDialog;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.res.ResourcesCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectRouteDialog extends BDialog {

    @BindView(R.id.buttonSend)
    Button buttonSend;
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;
    @BindView(R.id.checkBoxSuggested)
    AppCompatCheckBox checkBoxSuggested;
    @BindView(R.id.checkBoxDefault)
    AppCompatCheckBox checkBoxDefault;

    public SelectRouteDialog(Context context) {
        super(context);
        int layout = R.layout.dialog_select_route;
        View view = getLayoutInflater().inflate(layout, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);

        checkBoxDefault.setTypeface(ResourcesCompat.getFont(getContext(),R.font.gotham_book));
        checkBoxSuggested.setTypeface(ResourcesCompat.getFont(getContext(),R.font.gotham_book));


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
