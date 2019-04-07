package com.bincee.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.bincee.driver.HomeActivity;
import com.bincee.driver.R;
import com.bincee.driver.base.BA;
import com.bincee.driver.fragment.DriverProfileFragment;

import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends BA {

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    public static void start(HomeActivity context) {
        context.startActivity(new Intent(context, ProfileActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        textViewTitle.setText("MY PROFILE");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new DriverProfileFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}


