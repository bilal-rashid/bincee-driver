package com.findxain.uberdriver.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findxain.uberdriver.HomeActivity;
import com.findxain.uberdriver.R;
import com.findxain.uberdriver.api.firestore.Ride;
import com.findxain.uberdriver.api.model.Student;
import com.findxain.uberdriver.base.BFragment;
import com.findxain.uberdriver.dialog.MarkAttendanceDialog;
import com.findxain.uberdriver.dialog.MarkStudentAbdentDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceFragemnt extends BFragment {


    private static AttendanceFragemnt attendanceFragemnt;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.buttonStartRide)
    Button buttonStartRide;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;


    public AttendanceFragemnt() {
        // Required empty public constructor
    }

    public static AttendanceFragemnt getInstance() {
        if (attendanceFragemnt == null) {
            attendanceFragemnt = new AttendanceFragemnt();
        }
        return attendanceFragemnt;
    }

    MyAttandanceAdapter attandanceAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attandanceAdapter = new MyAttandanceAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        setActivityTitle("MARK STUDENTS");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance_fragemnt, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setAdapter(attandanceAdapter);

    }

    @OnClick(R.id.buttonStartRide)
    public void onViewClicked() {
    }

//    public void setStudentList(List<Student> students) {
//        this.students = students;
//        if (attandanceAdapter != null) {
//            attandanceAdapter.notifyDataSetChanged();
//        }
//    }

    private class MyAttandanceAdapter extends RecyclerView.Adapter<VH> {


        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(getContext()).inflate(R.layout.attendance_recycler_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {

            holder.bind();

        }

        @Override
        public int getItemCount() {
            return getStudents().size();
        }
    }

    private List<Student> getStudents() {
        Ride ride = ((HomeActivity) getActivity()).ride;
        return ride != null ? ride.students : new ArrayList<>();
    }

    public class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.imageViewGreenSelected)
        ImageView imageViewGreenSelected;
        @BindView(R.id.imageViewRedSelected)
        ImageView imageViewRedSelected;
        @BindView(R.id.viewRed)
        View viewRed;
        @BindView(R.id.viewGreen)
        View viewGreen;
        @BindView(R.id.imageViewCross)
        ImageView imageViewCross;
        @BindView(R.id.imageViewCheck)
        ImageView imageViewCheck;
        @BindView(R.id.textViewName)
        TextView textViewName;
        @BindView(R.id.textViewLocation)
        TextView textViewLocation;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;

        public VH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind() {

            Student student = getStudents().get(getAdapterPosition());
            textViewName.setText(student.fullname);
            textViewLocation.setText(student.address);

            if (student.present == Student.UNKNOWN) {

                imageViewRedSelected.setVisibility(View.GONE);
                imageViewGreenSelected.setVisibility(View.GONE);
                setTextColorBlack();

            } else if (student.present == Student.PRESENT) {


                markPresent();

            } else if (student.present == Student.ABSENT) {

                markAbsent();
            }

        }

        private void setTextColorBlack() {
            textViewName.setTextColor(Color.BLACK);
            textViewLocation.setTextColor(Color.BLACK);
        }

        private void markAbsent() {
            imageViewRedSelected.setVisibility(View.VISIBLE);
            imageViewGreenSelected.setVisibility(View.GONE);
            setTextColorWhite();
            getStudents().get(getAdapterPosition()).present = Student.ABSENT;
        }

        private void markPresent() {
            imageViewRedSelected.setVisibility(View.GONE);
            imageViewGreenSelected.setVisibility(View.VISIBLE);
            setTextColorWhite();
            getStudents().get(getAdapterPosition()).present = Student.PRESENT;
            ((HomeActivity) getActivity()).updateAttendance();


        }

        private void setTextColorWhite() {
            textViewName.setTextColor(Color.WHITE);
            textViewLocation.setTextColor(Color.WHITE);
        }

        @OnClick(R.id.imageViewCross)
        public void onCrossClicked() {

            showAttDialog();
        }


        @OnClick(R.id.imageViewCheck)
        public void onCheckClicked() {

            showAttDialog();

        }


        private void showAttDialog() {
            new MarkAttendanceDialog(getContext()).setListner(new MarkAttendanceDialog.Listner() {
                @Override
                public void markAbsent() {
                    VH.this.markAbsent();

                }

                @Override
                public void markPresent() {
                    VH.this.markPresent();

                }
            }).show();
        }

    }
}
