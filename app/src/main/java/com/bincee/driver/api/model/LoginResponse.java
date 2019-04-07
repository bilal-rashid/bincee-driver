package com.bincee.driver.api.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.bincee.driver.MyApp;
import com.bincee.driver.activity.LoginActivity;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

public class LoginResponse {


    public User data;
    public int status;

    public static class User {
        public String token;
        public int type;
        public String username;
        public int id;
        public String message;
        public String profilepic;
        public String fullName;

        public void save(Context context) {
            SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
            pref.edit().putString(User.class.getSimpleName(), toString()).apply();

        }

        public String getUsername() {
            return username;
        }

        @Override
        public String toString() {
            return MyApp.instance.gson.toJson(this);
        }
    }
}
