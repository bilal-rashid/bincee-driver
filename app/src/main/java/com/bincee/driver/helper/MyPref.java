package com.bincee.driver.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.bincee.driver.activity.LoginActivity;
import com.bincee.driver.api.model.LoginResponse;
import com.google.gson.Gson;

public class MyPref {


    public static final String CREDANTIALS = "credantials";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    private static final String REMEMBER_ME = "remember_me";


    public static void SAVE_USER(Context loginActivity, LoginResponse.User user) {
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

    public static void SAVE_CREDATIALS(LoginActivity loginActivity, String username, String pass) {
        SharedPreferences sharedPreferences = getRemeberMePrefrance(loginActivity);
        sharedPreferences.edit()
                .putString(USERNAME, username)
                .putString(PASSWORD, pass)
                .putBoolean(REMEMBER_ME, true)
                .apply();


    }

    public static String getUSER_NAME(LoginActivity loginActivity) {
        SharedPreferences sharedPreferences = getRemeberMePrefrance(loginActivity);
        return sharedPreferences.getString(USERNAME, "");
    }

    public static String getPASSWORD(LoginActivity loginActivity) {
        SharedPreferences sharedPreferences = getRemeberMePrefrance(loginActivity);
        return sharedPreferences.getString(PASSWORD, "");
    }

    public static void REMOVE_REMEBER(LoginActivity loginActivity) {
        SharedPreferences sharedPreferences = getRemeberMePrefrance(loginActivity);
        sharedPreferences.edit().clear().apply();


    }

    private static SharedPreferences getRemeberMePrefrance(LoginActivity loginActivity) {
        return loginActivity.getSharedPreferences(CREDANTIALS, Context.MODE_PRIVATE);
    }

    public static boolean getREMEMBERME(LoginActivity loginActivity) {
        SharedPreferences sharedPreferences = getRemeberMePrefrance(loginActivity);
        return sharedPreferences.getBoolean(REMEMBER_ME, false);

    }

}
