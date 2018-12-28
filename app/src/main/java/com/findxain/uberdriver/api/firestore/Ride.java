package com.findxain.uberdriver.api.firestore;

import com.findxain.uberdriver.api.model.Student;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class Ride  {

    public String rideId;

    public Timestamp endTime;
    public GeoPoint latLng;
    public boolean rideInProgress;
    public Timestamp startTime;
    public List<Student> students;
    public int driverId;

    public Ride() {
    }
}
