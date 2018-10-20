package com.findxain.uberdriver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.findxain.uberdriver.HomeActivity;
import com.findxain.uberdriver.R;
import com.findxain.uberdriver.base.BA;
import com.findxain.uberdriver.fragment.ParentProfileFragment;

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
                .replace(R.id.frameLayout, new ParentProfileFragment())
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


