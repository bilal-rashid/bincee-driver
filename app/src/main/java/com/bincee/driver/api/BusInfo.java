package com.bincee.driver.api;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BusInfo {


    @SerializedName("buses")
    public List<BusesEntity> buses = new ArrayList<>();
    @SerializedName("status")
    public String status;
    @SerializedName("school_id")
    public int school_id;
    @SerializedName("photo")
    public String photo;
    @SerializedName("phone_no")
    public String phone_no;
    @SerializedName("fullname")
    public String fullname;
    @SerializedName("driver_id")
    public int driver_id;

    public class BusesEntity {
        @SerializedName("driver_id")
        public int driver_id;
        @SerializedName("description")
        public String description;
        @SerializedName("registration_no")
        public String registration_no;
        @SerializedName("id")
        public int id;

        @NonNull
        @Override
        public String toString() {
            return registration_no;
        }
    }
}
