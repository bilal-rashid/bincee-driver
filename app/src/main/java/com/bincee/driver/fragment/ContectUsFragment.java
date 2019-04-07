package com.bincee.driver.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bincee.driver.HomeActivity;
import com.bincee.driver.R;
import com.bincee.driver.base.BA;

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


        textViewContextUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String[] addresses = {getResources().getString(R.string.u_contactus_bincee_com_u)};
                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                intent.putExtra(Intent.EXTRA_SUBJECT, "");

//                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
//                }


            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BA) Objects.requireNonNull(getActivity())).textViewTitle.setText("Contact Us");

    }

}
