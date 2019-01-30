package com.bincee.driver.activity;

import android.os.Bundle;
import android.os.Handler;
import android.telephony.SubscriptionPlan;


import com.bincee.driver.HomeActivity;
import com.bincee.driver.MyApp;
import com.bincee.driver.R;
import com.bincee.driver.api.model.LoginResponse;
import com.bincee.driver.helper.MyPref;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginResponse.User user = MyPref.GET_USER(SplashActivity.this);
                if (user != null) {
                    MyApp.instance.user = user;
                    HomeActivity.start(SplashActivity.this);
                } else {
                    LoginActivity.start(SplashActivity.this);
                }
                finish();
            }
        }, 2000);
    }
}
