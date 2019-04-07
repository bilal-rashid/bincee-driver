package com.bincee.driver.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bincee.driver.HomeActivity;
import com.bincee.driver.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
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
    @BindView(R.id.imageViewCloud)
    ImageView imageViewCloud;
    @BindView(R.id.imageViewProfilePic)
    CircleImageView imageViewProfilePic;
    @BindView(R.id.textViewName)
    TextView textViewName;
    @BindView(R.id.textView13)
    TextView textView13;

    @BindView(R.id.textViewPowerBar)
    TextView textViewPowerBar;
    @BindView(R.id.seekBarPowerBar)
    SeekBar seekBarPowerBar;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView9)
    TextView textView9;
    @BindView(R.id.textView8)
    TextView textView8;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.imageViewAttendanceBackground)
    ImageView imageViewAttendanceBackground;
    @BindView(R.id.progressBarAttandence)
    ProgressBar progressBarAttandence;
    @BindView(R.id.textViewAttendance)
    TextView textViewAttendance;
    @BindView(R.id.textViewAttendanceTotal)
    TextView textViewAttendanceTotal;
    @BindView(R.id.cardViewAttendance)
    CardView cardViewAttendance;
    @BindView(R.id.imageViewEligibleForBonus)
    ImageView imageViewEligibleForBonus;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.imageViewEligibleForBonusYesNo)
    TextView imageViewEligibleForBonusYesNo;
    @BindView(R.id.cardViewBonusEligible)
    CardView cardViewBonusEligible;
    @BindView(R.id.textView10)
    TextView textView10;
    @BindView(R.id.textView11)
    TextView textView11;
    @BindView(R.id.cardView)
    CardView cardView;

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


        ArrayList<String> days = new ArrayList<>();
        for (int i = 1; i < 32; i++) {
            days.add(i + "");
        }

        ArrayList<String> months = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            months.add(i + "");
        }


        ArrayList<String> year = new ArrayList<>();
        for (int i = 1900; i < 2018; i++) {
            year.add(i + "");
        }


        String[] daysArray = new String[days.size()];
        stringPickerDate.setValues(days.toArray(daysArray));
        String[] monthsArray = new String[months.size()];
        stringPickerMonth.setValues(months.toArray(monthsArray));
        String[] yearArray = new String[year.size()];
        stringPickerYear.setValues(year.toArray(yearArray));


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
