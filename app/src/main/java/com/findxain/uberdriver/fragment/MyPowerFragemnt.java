package com.findxain.uberdriver.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.findxain.uberdriver.HomeActivity;
import com.findxain.uberdriver.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import hotchemi.stringpicker.StringPicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPowerFragemnt extends Fragment {


    private static MyPowerFragemnt instance;
    @BindView(R.id.stringPickerDate)
    StringPicker stringPickerDate;
    @BindView(R.id.stringPickerMonth)
    StringPicker stringPickerMonth;
    @BindView(R.id.stringPickerYear)
    StringPicker stringPickerYear;

    public MyPowerFragemnt() {
        // Required empty public constructor
    }

    public static Fragment getInstance() {

        if (instance == null) {
            instance = new MyPowerFragemnt();
        }
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_power_fragemnt, container, false);
        ButterKnife.bind(this, view);

        FragmentActivity activity = getActivity();
        if (activity instanceof HomeActivity) {
            ((HomeActivity) activity).textViewTitle.setText("MY POWER");
        }


        String[] values = new String[]{"a", "b", "c", "d", "e", "f"};


        stringPickerDate.setValues(values);
        stringPickerMonth.setValues(values);
        stringPickerYear.setValues(values);


        return view;
    }

}
