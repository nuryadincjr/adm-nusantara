package com.nuyradincjr.ebusantara.api;

import static java.util.Objects.requireNonNull;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.nuyradincjr.ebusantara.pojo.Schedule;

import java.util.ArrayList;


public class ScheduleRepository {
    private static final String COLLECTION_CITIES = "schedule";
    private final CollectionReference collectionReference;

    public ScheduleRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collectionReference = db.collection(COLLECTION_CITIES);
    }

    public Task<Void> insertSchedule(Schedule schedule) {
        return collectionReference.document(schedule.getId()).set(schedule);
    }

    public MutableLiveData<ArrayList<Schedule>> getSearchSchedule(String value) {
        ArrayList<Schedule> citiesArrayList = new ArrayList<>();
        final MutableLiveData<ArrayList<Schedule>> productsMutableLiveData = new MutableLiveData<>();

        collectionReference
                .whereGreaterThanOrEqualTo("city", value)
                .whereLessThanOrEqualTo("city",value+"~")
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot document : requireNonNull(task.getResult())) {
                    Schedule cities = document.toObject(Schedule.class);
                    citiesArrayList.add(cities);
                }
                productsMutableLiveData.postValue(citiesArrayList);
            } else productsMutableLiveData.setValue(null);
        });
        return productsMutableLiveData;
    }

    public MutableLiveData<ArrayList<Schedule>> getSchedule(Schedule value) {
        ArrayList<Schedule> citiesArrayList = new ArrayList<>();
        final MutableLiveData<ArrayList<Schedule>> productsMutableLiveData = new MutableLiveData<>();

        collectionReference
                .whereEqualTo("id", value.getId())
                .whereEqualTo("bus", value.getBus())
                .whereEqualTo("departure", value.getDeparture())
                .whereEqualTo("departureTime",value.getDepartureTime())
                .whereEqualTo("arrival",value.getArrival())
                .whereEqualTo("arrivalTime",value.getArrivalTime())
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot document : requireNonNull(task.getResult())) {
                    Schedule cities = document.toObject(Schedule.class);
                    citiesArrayList.add(cities);
                }
                productsMutableLiveData.postValue(citiesArrayList);
            } else productsMutableLiveData.setValue(null);
        });
        return productsMutableLiveData;
    }
}
