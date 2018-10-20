package com.findxain.uberdriver.base;

import android.widget.TextView;

import com.findxain.uberdriver.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BA extends AppCompatActivity {

    @Nullable
    @BindView(R.id.textViewTitle)
    public TextView textViewTitle;
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }
}
