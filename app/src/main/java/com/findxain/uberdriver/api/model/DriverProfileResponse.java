package com.findxain.uberdriver.api.model;

import com.google.gson.annotations.SerializedName;

public class DriverProfileResponse {


    @SerializedName("status")
    public String status;
    @SerializedName("school_id")
    public int school_id;
    @SerializedName("phone_no")
    public String phone_no;
    @SerializedName("fullname")
    public String fullname;
    @SerializedName("driver_id")
    public int driver_id;
    public String photo;
}
