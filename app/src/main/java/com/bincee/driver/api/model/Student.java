package com.bincee.driver.api.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Student {

    public static final int STATUS_MORNING_BUS_IS_COMMING = 1;
    public static final int STATUS_MORNING_ATYOURLOCATION = 2;
    public static final int STATUS_MORNING_ONTHEWAY = 3;
    public static final int STATUS_MORNING_REACHED = 4;


    public static final int STATUS_AFTERNOON_INTHEBUS = 2;
    public static final int STATUS_AFTERNOON_ALMOSTTHERE = 3;//500meters
    public static final int STATUS_AFTERNOON_ATYOURDOORSTEP = 4;

    public static int UNKNOWN = -1;
    public static int PRESENT = 1;
    public static int ABSENT = 2;

    public String email;
    public String photo;
    public double lng;
    public double lat;
    public String address;
    public String phone_no;
    public String parentname;
    public int shift;
    public String fullname;
    public int id;
    public int status = 1;
    public int parent_id;

    public int present = UNKNOWN;
    public Double distance;
    public Double duration;

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Integer) {
            int objint = (int) obj;
            if (objint == parent_id) {
                return true;
            }

        }
        return super.equals(obj);
    }


    @Override
    public int hashCode() {
        return parent_id;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
