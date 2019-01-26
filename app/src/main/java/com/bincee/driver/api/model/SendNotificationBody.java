package com.bincee.driver.api.model;

import com.bincee.driver.api.model.notification.Notification;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SendNotificationBody {


    public List<String> registration_ids = new ArrayList<>();
    public Notification.Notific notification;

    public SendNotificationBody(String token, Notification.Notific notification) {
        this.registration_ids.add(token);
        this.notification = notification;
    }

    public SendNotificationBody(List<String> token, Notification.Notific notification) {
        this.registration_ids = token;
        this.notification = notification;
    }


}
