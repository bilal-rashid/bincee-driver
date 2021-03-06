package com.bincee.driver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bincee.driver.base.BA;
import com.bincee.driver.fragment.RouteDesignerFragment;

public class TestActivity extends BA {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new RouteDesignerFragment())
                .commit();
    }
}
