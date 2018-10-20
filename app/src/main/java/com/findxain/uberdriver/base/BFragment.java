package com.findxain.uberdriver.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class BFragment extends Fragment {


    protected void setActivityTitle(String title) {
        FragmentActivity activity = getActivity();

        ((BA) activity).textViewTitle.setText(title);
    }
}
