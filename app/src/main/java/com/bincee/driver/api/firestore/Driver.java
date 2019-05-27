package com.bincee.driver.api.firestore;

import com.google.firebase.firestore.GeoPoint;

public class Driver {

    public String rideId;

    public GeoPoint latLng;
    public float driverDirection;
    public String shift;


    public Driver() {
    }

//    public Student getCurrentStudent() {
//        for (Student student : students) {
//            if (student.present == Student.UNKNOWN) {
//                return student;
//            }
//        }
//        return null;
//    }
}
