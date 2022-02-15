package com.nuyradincjr.ebusantara.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nuyradincjr.ebusantara.pojo.Buses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewsRepository {
    private static final String COLLECTION_CITIES = "reviews";
    private final CollectionReference collectionReference;

    public ReviewsRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collectionReference = db.collection(COLLECTION_CITIES);
    }

    public Task<Void> insertReviews(Buses buses) {
        ArrayList<Map> mapList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("reviewer", mapList);

        return collectionReference.document(buses.getId()).set(map);
    }
}
