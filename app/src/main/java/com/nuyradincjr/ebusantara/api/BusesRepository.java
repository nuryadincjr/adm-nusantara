package com.nuyradincjr.ebusantara.api;

import static java.util.Objects.requireNonNull;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.nuyradincjr.ebusantara.pojo.Buses;
import com.nuyradincjr.ebusantara.pojo.Cities;

import java.util.ArrayList;

public class BusesRepository {
    private static final String COLLECTION_CITIES = "buses";
    private final CollectionReference collectionReference;

    public BusesRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collectionReference = db.collection(COLLECTION_CITIES);
    }

    public Task<Void> insertBuses(Buses buses) {
        return collectionReference.document(buses.getId()).set(buses);
    }

    public MutableLiveData<ArrayList<Buses>> getSearchBuses(String value) {
        ArrayList<Buses> citiesArrayList = new ArrayList<>();
        final MutableLiveData<ArrayList<Buses>> listMutableLiveData = new MutableLiveData<>();

        collectionReference
                .whereGreaterThanOrEqualTo("busNo", value)
                .whereLessThanOrEqualTo("busNo",value+"~")
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot document : requireNonNull(task.getResult())) {
                    Buses cities = document.toObject(Buses.class);
                    citiesArrayList.add(cities);
                }
                listMutableLiveData.postValue(citiesArrayList);
            } else listMutableLiveData.setValue(null);
        });
        return listMutableLiveData;
    }

    public MutableLiveData<ArrayList<Buses>> getBuses(Buses value) {
        ArrayList<Buses> busesArrayList = new ArrayList<>();
        final MutableLiveData<ArrayList<Buses>> listMutableLiveData = new MutableLiveData<>();

        collectionReference
                .whereEqualTo("busNo", value.getBusNo())
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot document : requireNonNull(task.getResult())) {
                    Buses cities = document.toObject(Buses.class);
                    busesArrayList.add(cities);
                }
                listMutableLiveData.postValue(busesArrayList);
            } else listMutableLiveData.setValue(null);
        });
        return listMutableLiveData;
    }

    public MutableLiveData<ArrayList<Buses>> getCollectionBuses() {
        ArrayList<Buses> buses = new ArrayList<>();
        final MutableLiveData<ArrayList<Buses>> listMutableLiveData = new MutableLiveData<>();

        collectionReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot  snapshot : task.getResult()) {
                    Buses data = snapshot.toObject(Buses.class);
                    buses.add(data);
                }
                listMutableLiveData.postValue(buses);
            }else listMutableLiveData.setValue(null);
        });
        return listMutableLiveData;
    }
}
