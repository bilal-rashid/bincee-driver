package com.bincee.driver.api.firestore;

import com.bincee.driver.api.model.Student;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class Ride {

    public static final String SHIFT_MORNING = "morning";
    public static final String SHIFT_AFTERNOON = "afternoon";

    public String rideId;

    public Timestamp endTime;
    public GeoPoint latLng;
    public boolean rideInProgress;
    public Timestamp startTime;
    public List<Student> students;
    public int driverId;
    public String shift;
    public GeoPoint schoolLatLng;
    public boolean rechtoSchoolNotificationSent = false;
    public int shiftId;
    public boolean routeCreated = false;


    public Ride() {
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
