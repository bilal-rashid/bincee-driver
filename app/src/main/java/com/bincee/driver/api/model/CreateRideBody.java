package com.bincee.driver.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CreateRideBody {

    @SerializedName("shifts")
    public List<Integer> shifts = new ArrayList<>();
    @SerializedName("driver_id")
    public int driver_id;

    public CreateRideBody(int id, int shift_id) {
        this.driver_id = id;
        shifts.add(shift_id);
    }
}
