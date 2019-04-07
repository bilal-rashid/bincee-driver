package com.bincee.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.bincee.driver.HomeActivity;
import com.bincee.driver.R;
import com.bincee.driver.base.BA;
import com.bincee.driver.databinding.ActivityContectUsBinding;
import com.bincee.driver.fragment.ContectUsFragment;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

//import butterknife.BindView;

public class ContectUsActivity extends BA {

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

    public static void start(HomeActivity homeActivity) {
        homeActivity.startActivity(new Intent(homeActivity, ContectUsActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contect_us);
        ActivityContectUsBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_contect_us);


        setSupportActionBar(viewDataBinding.include.toolbar);
        getSupportActionBar().setTitle("");
        textViewTitle.setText("CONTACT US");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new ContectUsFragment())
                .commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class VM extends ViewModel {

    }
}
