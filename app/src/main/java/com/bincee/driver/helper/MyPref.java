package com.bincee.driver.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.bincee.driver.activity.LoginActivity;
import com.bincee.driver.api.model.LoginResponse;
import com.google.gson.Gson;

public class MyPref {


    public static void SAVE_USER(LoginActivity loginActivity, LoginResponse.User user) {
        SharedPreferences sharedPref = getSharedPref(loginActivity);
        sharedPref.edit().putString("user", new Gson().toJson(user)).apply();


    }

    public static LoginResponse.User GET_USER(Context loginActivity) {
        SharedPreferences sharedPref = getSharedPref(loginActivity);
        String userJson = sharedPref.getString("user", null);
        LoginResponse.User user = null;
        if (userJson != null) {
            user = new Gson().fromJson(userJson, LoginResponse.User.class);

        }
        return user;


    }

    private static SharedPreferences getSharedPref(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static void logout(Context context) {
        getSharedPref(context).edit().clear().apply();

    }
}
