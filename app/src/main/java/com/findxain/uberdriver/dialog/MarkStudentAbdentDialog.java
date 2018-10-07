package com.findxain.uberdriver.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.findxain.uberdriver.R;
import com.findxain.uberdriver.base.BDialog;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarkStudentAbdentDialog extends BDialog {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.buttonSend)
    Button buttonSend;
    @BindView(R.id.buttonCancel)
    Button buttonCancel;
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;

    public MarkStudentAbdentDialog(Context context) {
        super(context);
        View view = getLayoutInflater()
                .inflate(R.layout.dialog_mark_absent, null, false);
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
    public void onImageViewCrossClicked() {

        dismiss();

    }
}
