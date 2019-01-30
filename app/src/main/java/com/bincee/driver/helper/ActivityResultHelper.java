package com.bincee.driver.helper;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class ActivityResultHelper {
    public int requestCode;
    private Result result;
    private Fragment fragment;
    private Before before;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (this.requestCode != requestCode) {
            return;
        }

        if (result != null) {
            if (resultCode == RESULT_OK) {
                result.ResultOk(data);

            } else {
                result.Failed();
            }
        }


    }

    public ActivityResultHelper setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public ActivityResultHelper with(Fragment fragment) {
        this.fragment = fragment;
        return this;
    }

    public ActivityResultHelper OnResult(Result result) {
        this.result = result;
        return this;


    }

    public ActivityResultHelper before(Before before) {
        this.before = before;
        return this;
    }

    public void start() {

        if (before != null) {
            before.before();
        }
    }

    public interface Result {
        void ResultOk(Intent data);

        void Failed();

    }

    public interface Before {
        void before();


    }
}
