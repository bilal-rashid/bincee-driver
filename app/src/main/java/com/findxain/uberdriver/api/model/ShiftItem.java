package com.findxain.uberdriver.api.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ShiftItem {


    @SerializedName("school_id")
    public int school_id;
    @SerializedName("end_time")
    public String end_time;
    @SerializedName("start_time")
    public String start_time;
    @SerializedName("shift_name")
    public String shift_name;
    @SerializedName("shift_id")
    public int shift_id;

    @NonNull
    @Override
    public String toString() {
        return shift_name;
    }
}
