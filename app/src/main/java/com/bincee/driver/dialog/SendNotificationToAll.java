package com.bincee.driver.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bincee.driver.R;
import com.bincee.driver.base.BDialog;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendNotificationToAll extends BDialog {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.buttonSend)
    Button buttonSend;
    @BindView(R.id.buttonCancel)
    TextView buttonCancel;
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;
    private Listner listner;

    public SendNotificationToAll(Context context) {
        super(context);
        View view = getLayoutInflater().inflate(R.layout.send_notification_all_dialog, null, false);

        setContentView(view);
        ButterKnife.bind(this, view);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(getLayoutInflater().inflate(R.layout.recycler_row_mark_student_absent, parent, false)) {

                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 3;
            }
        });


    }

    @OnClick(R.id.buttonSend)
    public void onButtonSendClicked() {
        dismiss();
        listner.yes();

    }

    @OnClick(R.id.buttonCancel)
    public void onButtonCancelClicked() {
        dismiss();
        listner.cancel();

    }

    @OnClick(R.id.imageViewCross)
    public void onImageViewCrossClicked() {
        dismiss();

    }

    public void setListner(Listner listner) {
        this.listner = listner;
    }

    public interface Listner {
        void yes();

        void cancel();

    }
}
