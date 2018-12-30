package com.findxain.uberdriver.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class BFragment extends Fragment {


    protected void setActivityTitle(String title) {

        BA activity = (BA) getActivity();
        if (activity != null) {
            activity.textViewTitle.setText(title);
        }
    }
}
