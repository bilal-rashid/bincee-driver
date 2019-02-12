package com.bincee.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.res.ResourcesCompat;

import com.bincee.driver.HomeActivity;
import com.bincee.driver.MyApp;
import com.bincee.driver.R;
import com.bincee.driver.api.model.LoginResponse;
import com.bincee.driver.base.BA;
import com.bincee.driver.customview.MyProgress;
import com.bincee.driver.helper.MyPref;
import com.bincee.driver.observer.EndpointObserver;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BA {

    @BindView(R.id.buttonLogin)
    Button buttonLogin;
    @BindView(R.id.editTextUsername)
    TextInputEditText editTextUsername;
    @BindView(R.id.editTextPassword)
    TextInputEditText editTextPassword;
    @BindView(R.id.checkBox)
    AppCompatCheckBox chechBoxRememberMe;
    @BindView(R.id.progressBar)
    MyProgress progressBar;
    @BindView(R.id.textViewPassword)
    TextView textViewPassword;

    public static void start(SplashActivity splashActivity) {
        splashActivity.startActivity(new Intent(splashActivity, LoginActivity.class));
        splashActivity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        chechBoxRememberMe.setTypeface(ResourcesCompat.getFont(this, R.font.gotham_book));


//        editTextUsername.setText("johnd1");
//        editTextPassword.setText("ChangeMe@2");
        editTextUsername.setText("test_driverd1");
        editTextPassword.setText("ChangeMe@2");


    }

    @OnClick(R.id.buttonLogin)
    public void onViewClicked() {

        progressBar.setVisibility(View.VISIBLE);

        compositeDisposable.add(MyApp.endPoints
                .login(editTextUsername.getText().toString(), editTextPassword.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new EndpointObserver<LoginResponse>() {
                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onData(LoginResponse response) {
                        if (response.status == 200) {
                            response.data.save(LoginActivity.this);
                            MyApp.instance.user = response.data;
                            HomeActivity.start(LoginActivity.this);

                            MyPref.SAVE_USER(LoginActivity.this, MyApp.instance.user);
                            finish();


                        } else {
                            MyApp.showToast(response.data.message);
                        }
                    }

                    @Override
                    public void onHandledError(Throwable e) {
                        e.printStackTrace();
                        MyApp.showToast(e.getMessage());

                    }
                }));
    }

    @OnClick(R.id.textViewPassword)
    public void onForgrtPasswordClicked() {
        ForgetPasswordActivity.start(this);
    }
}
