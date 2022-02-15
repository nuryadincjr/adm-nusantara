package com.nuyradincjr.ebusantara.util;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.nuyradincjr.ebusantara.api.BusesRepository;
import com.nuyradincjr.ebusantara.api.CitiesRepository;
import com.nuyradincjr.ebusantara.api.ScheduleRepository;
import com.nuyradincjr.ebusantara.pojo.Buses;
import com.nuyradincjr.ebusantara.pojo.Cities;
import com.nuyradincjr.ebusantara.pojo.Schedule;

import java.util.ArrayList;


public class MainViewModel extends AndroidViewModel {

    private final CitiesRepository citiesRepository;
    private final BusesRepository busesRepository;
    private final ScheduleRepository scheduleRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.citiesRepository = new CitiesRepository();
        this.busesRepository = new BusesRepository();
        this.scheduleRepository = new ScheduleRepository();
    }

    public MutableLiveData<ArrayList<Cities>> getCities(Cities value) {
        return citiesRepository.getCities(value);
    }

    public MutableLiveData<ArrayList<Cities>> getCities() {
        return citiesRepository.getCollectionCities();
    }

    public MutableLiveData<ArrayList<Cities>> getCities(String collection) {
        return citiesRepository.getSearchCities(collection);
    }

    public MutableLiveData<ArrayList<Schedule>> getSchedule(Schedule value) {
        return scheduleRepository.getSchedule(value);
    }

    public MutableLiveData<ArrayList<Buses>> getBuses(Buses value) {
        return busesRepository.getBuses(value);
    }

    public MutableLiveData<ArrayList<Buses>> getBuses() {
        return busesRepository.getCollectionBuses();
    }

    public MutableLiveData<ArrayList<Buses>> getBuses(String collection) {
        return busesRepository.getSearchBuses(collection);
    }
}
