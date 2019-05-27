package com.bincee.driver.api.firestore;

import com.bincee.driver.MyApp;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FireStoreHelper {

    public static Task<QuerySnapshot> getToken(String parentId) {
        return FirebaseFirestore.getInstance().collection("token")
                .document(parentId)
                .collection("tokens")
                .get();
    }

    public static DocumentReference getRide() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection("ride").document(MyApp.instance.user.getValue().id + "");
    }

    public static DocumentReference getRealTimeDriver() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection("real_time").document(MyApp.instance.user.getValue().id + "");
    }
    public static DocumentReference getRideByRideId(String rideId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection("ride").document(rideId);
    }

    public static CollectionReference getHistory() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection("history");
    }

    public static DocumentReference getRouteDesigner(String shiftId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db
                .collection("routeDesigner")
                .document(MyApp.instance.user.getValue().id + "")
                .collection(shiftId)
                .document("students");
    }
}
