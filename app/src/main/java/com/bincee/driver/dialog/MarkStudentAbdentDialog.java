package com.bincee.driver.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bincee.driver.R;
import com.bincee.driver.api.model.Student;
import com.bincee.driver.base.BDialog;
import com.bincee.driver.databinding.RecyclerRowMarkStudentAbsentBinding;
import com.bincee.driver.helper.ImageBinder;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarkStudentAbdentDialog extends BDialog {
    private List<Student> students = new ArrayList<>();
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.buttonSend)
    Button buttonSend;
    @BindView(R.id.buttonCancel)
    TextView buttonCancel;
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;
    private Listner listner;

    public MarkStudentAbdentDialog(Context context, List<Student> absentStudents) {
        super(context);
        setCancelable(false);
        this.students = absentStudents;
        View view = getLayoutInflater()
                .inflate(R.layout.dialog_mark_absent, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);

        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setAdapter(new RecyclerView.Adapter<VH>() {
            @NonNull
            @Override
            public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new VH(getLayoutInflater().inflate(R.layout.recycler_row_mark_student_absent, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull VH holder, int position) {

                holder.bind(students.get(holder.getAdapterPosition()));
            }

            @Override
            public int getItemCount() {
                return students.size();
            }
        });

    }


    @OnClick(R.id.buttonSend)
    public void onButtonSendClicked() {
        listner.yes();
        dismiss();

    }

    @OnClick(R.id.buttonCancel)
    public void onButtonCancelClicked() {
        dismiss();
    }

    @OnClick(R.id.imageViewCross)
    public void onImageViewCrossClicked() {
        listner.no();

        dismiss();

    }

    public void setListner(Listner listner) {
        this.listner = listner;
    }

    public interface Listner {
        void yes();

        void no();
    }

    private class VH extends RecyclerView.ViewHolder {

        RecyclerRowMarkStudentAbsentBinding binding;

        public VH(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

        }

        public void bind(Student student) {
            binding.textViewName.setText(student.fullname);
            binding.textViewAddress.setText(student.address);

            ImageBinder.setImageUrl(binding.imageViewProfileImage, student.photo);


        }
    }

}
