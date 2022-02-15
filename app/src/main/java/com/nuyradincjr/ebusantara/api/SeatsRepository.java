package com.nuyradincjr.ebusantara.api;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.nuyradincjr.ebusantara.pojo.Seats;

import java.util.ArrayList;

public class SeatsRepository {
    private static final String COLLECTION_CITIES = "seats";
    private final CollectionReference collectionReference;

    public SeatsRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collectionReference = db.collection(COLLECTION_CITIES);
    }

    public Task<Void> insertSeats(Seats cities, String document) {
        return collectionReference.document(document).set(cities);
    }

    public MutableLiveData<ArrayList<Seats>> getCollectionSeats() {
        ArrayList<Seats> Schedule = new ArrayList<>();
        final MutableLiveData<ArrayList<Seats>> ScheduleMutableLiveData = new MutableLiveData<>();

        collectionReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot  snapshot : task.getResult()) {
                    Seats data = snapshot.toObject(Seats.class);
                    Schedule.add(data);
                }
                ScheduleMutableLiveData.postValue(Schedule);
            }else ScheduleMutableLiveData.setValue(null);
        });
        return ScheduleMutableLiveData;
    }
}
