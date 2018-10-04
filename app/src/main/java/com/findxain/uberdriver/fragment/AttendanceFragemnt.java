package com.findxain.uberdriver.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.findxain.uberdriver.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceFragemnt extends Fragment {


    private static AttendanceFragemnt attendanceFragemnt;

    public AttendanceFragemnt() {
        // Required empty public constructor
    }

    public static Fragment getInstance() {
        if (attendanceFragemnt == null) {
            attendanceFragemnt = new AttendanceFragemnt();
        }
        return attendanceFragemnt;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attendance_fragemnt, container, false);
    }

}
