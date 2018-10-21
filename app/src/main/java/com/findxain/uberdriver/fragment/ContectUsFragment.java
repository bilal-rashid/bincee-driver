package com.findxain.uberdriver.fragment;


import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.findxain.uberdriver.HomeActivity;
import com.findxain.uberdriver.R;
import com.findxain.uberdriver.base.BA;

import java.util.Objects;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContectUsFragment extends Fragment {


    @BindView(R.id.textViewContextUs)
    TextView textViewContextUs;

    public ContectUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contect_us, container, false);
        ButterKnife.bind(this, view);
        SpannableStringBuilder append = new SpannableStringBuilder().append(getResources().getString(R.string.u_contactus_bincee_com_u));
//        append.setSpan(new UnderlineSpan(),0,append.length(),);


        textViewContextUs.setText(Html.fromHtml("<u>" + getResources().getString(R.string.u_contactus_bincee_com_u) + "</u>"));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BA) Objects.requireNonNull(getActivity())).textViewTitle.setText("Contact Us");

    }

}
