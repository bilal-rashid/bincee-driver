package com.bincee.driver.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadImageResponce {
    @Expose
    @SerializedName("data")
    public DataEntity data;
    @Expose
    @SerializedName("status")
    public int status;
    public String message;

    public static class DataEntity {
        @Expose
        @SerializedName("path")
        public String path;
    }
}
