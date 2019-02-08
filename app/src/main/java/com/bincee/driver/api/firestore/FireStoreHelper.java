package com.bincee.driver.api.firestore;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FireStoreHelper {

    public static Task<QuerySnapshot> getToken(String parentId) {
        return FirebaseFirestore.getInstance().collection("token")
                .document(parentId)
                .collection("tokens")
                .get();
    }

}
