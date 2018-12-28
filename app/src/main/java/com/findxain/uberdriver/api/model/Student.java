package com.findxain.uberdriver.api.model;

import com.google.gson.annotations.SerializedName;

public class Student {

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

    public int present = UNKNOWN;

}
