package com.bincee.driver.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;


import com.bincee.driver.MyApp;
import com.bincee.driver.R;
import com.bincee.driver.api.model.MyResponse;
import com.bincee.driver.base.BA;
import com.bincee.driver.customview.MyProgress;
import com.bincee.driver.observer.EndpointObserver;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ForgetPasswordActivity extends BA {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.editTextUsername)
    TextInputEditText editTextUsername;
    @BindView(R.id.editTextEmail)
    TextInputEditText editTextEmail;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    @BindView(R.id.progressBar)
    MyProgress progressBar;

    public static void start(Context context) {
        context.startActivity(new Intent(context, ForgetPasswordActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        textViewTitle.setText("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.buttonSubmit)
    public void onViewClicked() {

        progressBar.setVisibility(View.VISIBLE);

        EndpointObserver<MyResponse> endpointObserver = MyApp.endPoints.forgetPassword(
                editTextUsername.getText().toString()
                , "email"
                , editTextEmail.getText().toString()
                , "Driver")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new EndpointObserver<MyResponse>() {
                    @Override
                    public void onComplete() {

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onData(MyResponse o) throws Exception {
                        if (o.status == 200) {

                        } else {
                            throw new Exception(o.message);
                        }

                    }

                    @Override
                    public void onHandledError(Throwable e) {

                        e.printStackTrace();

                        new AlertDialog.Builder(ForgetPasswordActivity.this)
                                .setMessage(e.getMessage())
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .create().show();

                    }
                });
        compositeDisposable.add(endpointObserver);

    }
}
