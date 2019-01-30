package com.bincee.driver.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SendNotificationResponce {


    @SerializedName("multicast_id")
    @Expose
    public int multicastId;

    @SerializedName("success")
    @Expose
    public int success;

    @SerializedName("failure")
    @Expose
    public int failure;

    @SerializedName("canonical_ids")
    @Expose
    public int canonicalIds;

    @SerializedName("results")
    @Expose
    public List<Result> results = new ArrayList<Result>();

    public static class Result {

        @SerializedName("message_id")
        @Expose
        public String messageId;

    }
}
