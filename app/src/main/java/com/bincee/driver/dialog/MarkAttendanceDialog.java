package com.bincee.driver.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bincee.driver.R;
import com.bincee.driver.api.model.Student;
import com.bincee.driver.base.BDialog;
import com.bincee.driver.helper.ImageBinder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarkAttendanceDialog extends BDialog {
    @BindView(R.id.imageViewProfilePic)
    ImageView imageViewProfilePic;
    @BindView(R.id.textViewName)
    TextView textViewName;
    @BindView(R.id.textViewAddress)
    TextView textViewAddress;
    @BindView(R.id.buttonSend)
    Button buttonSend;
    @BindView(R.id.buttonCancel)
    TextView buttonCancel;
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;
    private Listner listner;
    private Student student;

    public MarkAttendanceDialog(Context context) {
        super(context);
        View view = getLayoutInflater()
                .inflate(R.layout.dialog_absent_present, null, false);
        setContentView(view);

        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.buttonSend)
    public void onButtonSendClicked() {
        if (listner != null) {
            listner.markPresent();
        }
        onImageViewCrossClicked();
        dismiss();
    }

    @OnClick(R.id.buttonCancel)
    public void onButtonCancelClicked() {
        if (listner != null) {
            listner.markAbsent();
        }
        onImageViewCrossClicked();
        dismiss();
    }

    @OnClick(R.id.imageViewCross)
    public void onImageViewCrossClicked() {
        dismiss();
    }

    public MarkAttendanceDialog setListner(Listner listner) {
        this.listner = listner;
        return this;
    }

    public MarkAttendanceDialog setStudent(Student student) {
        this.student = student;

        textViewName.setText(student.fullname);
        textViewAddress.setText(student.address);
        ImageBinder.setImageUrl(imageViewProfilePic, student.photo);

        return this;
    }

    public static interface Listner {
        void markAbsent();

        void markPresent();


    }

}
