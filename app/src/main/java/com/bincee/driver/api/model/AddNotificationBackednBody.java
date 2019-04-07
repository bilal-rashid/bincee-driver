package com.bincee.driver.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AddNotificationBackednBody {

    @SerializedName("type")
    public String type = "student";
    @SerializedName("description")
    public String description;
    @SerializedName("last_updated")
    public long last_updated;
    @SerializedName("studentArray")
    public List<Integer> studentArray = new ArrayList<>();

    public AddNotificationBackednBody(String text, long time, int id) {
        this.description = text;
        this.last_updated = time;
        studentArray.add(id);
    }
}
