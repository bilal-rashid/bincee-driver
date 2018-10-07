package com.findxain.uberdriver.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findxain.uberdriver.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceFragemnt extends Fragment {


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

    public static Fragment getInstance() {
        if (attendanceFragemnt == null) {
            attendanceFragemnt = new AttendanceFragemnt();
        }
        return attendanceFragemnt;
    }

    MyAttandanceAdapter attandanceAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attandanceAdapter=new MyAttandanceAdapter();
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

    private class MyAttandanceAdapter extends RecyclerView.Adapter<VH> {


        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(getContext()).inflate(R.layout.attendance_recycler_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
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

    }
}
