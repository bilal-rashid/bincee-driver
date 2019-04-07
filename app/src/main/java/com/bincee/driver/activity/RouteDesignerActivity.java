package com.bincee.driver.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;

import com.bincee.driver.HomeActivity;
import com.bincee.driver.R;
import com.bincee.driver.base.BA;
import com.bincee.driver.fragment.RouteDesignerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RouteDesignerActivity extends BA {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    public static void start(Context homeActivity) {
        homeActivity.startActivity(new Intent(homeActivity, RouteDesignerActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_designer);

        setSupportActionBar(toolbar);


        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewTitle.setText("Route Designer");
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, RouteDesignerFragment.getInstance()).commit();


    }
}
