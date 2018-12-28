package com.findxain.uberdriver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.findxain.uberdriver.HomeActivity;
import com.findxain.uberdriver.MyApp;
import com.findxain.uberdriver.R;
import com.findxain.uberdriver.api.model.LoginResponse;
import com.findxain.uberdriver.base.BA;
import com.findxain.uberdriver.customview.MyProgress;
import com.findxain.uberdriver.observer.EndpointObserver;
import com.findxain.uberdriver.storage.MyStorage;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.res.ResourcesCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BA {

    @BindView(R.id.buttonLogin)
    Button buttonLogin;
    @BindView(R.id.editTextUsername)
    TextInputEditText editTextUsername;
    @BindView(R.id.editTextPassword)
    TextInputEditText editTextPassword;
    @BindView(R.id.chechBoxRememberMe)
    AppCompatCheckBox chechBoxRememberMe;
    @BindView(R.id.progressBar)
    MyProgress progressBar;

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


        editTextUsername.setText("driver1");
        editTextPassword.setText("driver");


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
//                            finish();
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
}
