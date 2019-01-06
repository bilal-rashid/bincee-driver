package com.findxain.uberdriver.api.model;

import com.google.gson.annotations.SerializedName;

public class GetSchoolResponce {


    @SerializedName("lng")
    public double lng;
    @SerializedName("lat")
    public double lat;
    @SerializedName("email")
    public String email;
    @SerializedName("phone_no")
    public String phone_no;
    @SerializedName("name")
    public String name;
    @SerializedName("address")
    public String address;
    @SerializedName("school_id")
    public int school_id;
}
