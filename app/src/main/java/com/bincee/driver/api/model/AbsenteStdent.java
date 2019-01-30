package com.bincee.driver.api.model;

import com.google.gson.annotations.SerializedName;

public  class AbsenteStdent {

    @SerializedName("status")
    public String status;
    @SerializedName("driver_id")
    public int driver_id;
    @SerializedName("parent_id")
    public int parent_id;
    @SerializedName("shift_evening")
    public int shift_evening;
    @SerializedName("shift_morning")
    public int shift_morning;
    @SerializedName("photo")
    public String photo;
    @SerializedName("grade")
    public int grade;
    @SerializedName("fullname")
    public String fullname;
    @SerializedName("id")
    public int id;
}
