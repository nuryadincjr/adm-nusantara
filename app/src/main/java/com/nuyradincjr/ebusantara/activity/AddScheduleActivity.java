package com.nuyradincjr.ebusantara.activity;

import static android.content.ContentValues.TAG;
import static java.util.Arrays.asList;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nuyradincjr.ebusantara.R;
import com.nuyradincjr.ebusantara.api.ScheduleRepository;
import com.nuyradincjr.ebusantara.api.SeatsRepository;
import com.nuyradincjr.ebusantara.chooser.BusChooserActivity;
import com.nuyradincjr.ebusantara.chooser.DatePickerActivity;
import com.nuyradincjr.ebusantara.chooser.DestinationChooserActivity;
import com.nuyradincjr.ebusantara.databinding.ActivityAddScheduleBinding;
import com.nuyradincjr.ebusantara.pojo.Buses;
import com.nuyradincjr.ebusantara.pojo.Cities;
import com.nuyradincjr.ebusantara.pojo.Schedule;
import com.nuyradincjr.ebusantara.pojo.Seats;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AddScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityAddScheduleBinding binding;
    private Schedule schedule;
    private Cities departureCity;
    private Cities arrivalCity;
    private Buses buses;
    private Calendar departureDate;
    private Calendar arrivalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        binding  = ActivityAddScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        schedule = new Schedule();
        departureCity = new Cities();
        arrivalCity = new Cities();
        buses = new Buses();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = db.collection("schedule").document().getId();
        binding.tvId.setText(id);
        binding.tvDeparture.setOnClickListener(this);
        binding.tvArrival.setOnClickListener(this);
        binding.tvDepartureTime.setOnClickListener(this);
        binding.tvArrivalTime.setOnClickListener(this);
        binding.tvBus.setOnClickListener(this);

        binding.btnAddItem.setOnClickListener(v->{
            onDataSet();
        });
    }

    private void onDataSet() {
        String id = String.valueOf(binding.tvId.getText());
        String departure = String.valueOf(binding.tvDeparture.getText());
        String arrival = String.valueOf(binding.tvArrival.getText());
        String departureTime = String.valueOf(binding.tvDepartureTime.getText());
        String arrivalTime = String.valueOf(binding.tvArrivalTime.getText());
        String bus = String.valueOf(binding.tvBus.getText());

        if(!id.isEmpty() && !departure.equals("Departure") &&
                !arrival.equals("Arrival") && !departureTime.equals("Departure Time") &&
                !arrivalTime.equals("Arrival Time") && !bus.equals("Bus")) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference drDeparture = db.document("cities/"+departureCity.getId());
            DocumentReference drArrival = db.document("cities/"+arrivalCity.getId());
            DocumentReference drBuses = db.document("buses/"+buses.getId());

            schedule.setId(id);
            schedule.setDeparture(drDeparture);
            schedule.setArrival(drArrival);
            schedule.setDepartureTime(departureTime);
            schedule.setArrivalTime(arrivalTime);
            schedule.setBus(drBuses);

            onDateCreated(schedule);

        } else Toast.makeText(this,"Please selected items!", Toast.LENGTH_SHORT).show();
    }

    private void onDateCreated(Schedule schedule) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Creating Data..");
        dialog.setCancelable(false);
        dialog.show();


//        int length = 29;
//        int length = 32;
//        int length = 31;
//        int length = 32;
//
//        for(int i=1; i<length; i++) {

//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            String id = db.collection("schedule").document().getId();
//
//            String date = String.valueOf(i);
//            if(date.length()==1){
//                date = "0"+date;
//            }
//            // Mei 5
//            String departureTime = date+"/05/2022 08:00";
//            String arrivalTime = date+"/05/2022 09:30";
//
//            schedule.setId(id);
//            schedule.setDepartureTime(departureTime);
//            schedule.setArrivalTime(arrivalTime);


            new ScheduleRepository().insertSchedule(schedule).addOnSuccessListener(documentReference -> {
                List<Boolean> seatsA = getBooleanList(true);
                List<Boolean> seatsB = getBooleanList(false);
                List<Boolean> seatsC = getBooleanList(false);
                List<Boolean> seatsD = getBooleanList(false);

                Seats seat = new Seats(seatsA, seatsB, seatsC, seatsD);
                new SeatsRepository().insertSeats(seat, schedule.getId()).addOnSuccessListener(reference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + reference);
                    Toast.makeText(this, "Success.", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                    finish();
                });
            }).addOnFailureListener(e -> {
                dialog.dismiss();
                Log.w(TAG, "Error adding document", e);
                Toast.makeText(this, "Error adding document.", Toast.LENGTH_SHORT).show();
            });
    }

    @NonNull
    private List<Boolean> getBooleanList(Boolean isListA) {
        if(isListA) return asList(true, true, true, true, true, true,true, true, true);
        return asList(true, true, true, true, true, true, true, true, true, true);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Intent intentCities = new Intent(this, DestinationChooserActivity.class);
        Intent intentDate = new Intent(this, DatePickerActivity.class);
        Intent intentBuses = new Intent(this, BusChooserActivity.class);
        switch (view.getId()){
            case R.id.tvDeparture:
                startActivityForResult(intentCities, 1);
                break;
            case R.id.tvArrival:
                startActivityForResult(intentCities, 2);
                break;
            case R.id.tvDepartureTime:
                startActivityForResult(intentDate, 3);
                break;
            case R.id.tvArrivalTime:
                startActivityForResult(intentDate, 4);
                break;
            case R.id.tvBus:
                startActivityForResult(intentBuses, 5);
                break;
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (data != null && resultCode == RESULT_OK) {
                    departureCity = data.getParcelableExtra("city");
                    binding.tvDeparture.setText(departureCity.getCity() +" - "+departureCity.getTerminal());
                }
                break;
            case 2:
                if (data != null && resultCode == RESULT_OK) {
                    arrivalCity = data.getParcelableExtra("city");
                    binding.tvArrival.setText(arrivalCity.getCity() + " - "+arrivalCity.getTerminal());
                }
                break;
            case 3:
                if (data != null && resultCode == RESULT_OK) {
                    departureDate = (Calendar) data.getSerializableExtra("date");
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    binding.tvDepartureTime.setText(format.format(departureDate.getTime()));
                }
                break;
            case 4:
                if (data != null && resultCode == RESULT_OK) {
                    arrivalDate = (Calendar) data.getSerializableExtra("date");
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    binding.tvArrivalTime.setText(format.format(arrivalDate.getTime()));
                }
                break;
            case 5:
                if (data != null && resultCode == RESULT_OK) {
                    buses = data.getParcelableExtra("bus");
                    binding.tvBus.setText(buses.getBusNo() +" - "+buses.getPoName());
                }
                break;
        }
    }
}