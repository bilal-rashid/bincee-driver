package com.findxain.uberdriver.base;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

public class BA extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }
}
