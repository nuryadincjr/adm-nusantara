package com.nuyradincjr.ebusantara.chooser;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nuyradincjr.ebusantara.R;
import com.nuyradincjr.ebusantara.adapters.BusesAdapter;
import com.nuyradincjr.ebusantara.databinding.ActivityBusChooserBinding;
import com.nuyradincjr.ebusantara.interfaces.ItemClickListener;
import com.nuyradincjr.ebusantara.pojo.Buses;
import com.nuyradincjr.ebusantara.util.MainViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class BusChooserActivity extends AppCompatActivity {

    private ActivityBusChooserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_chooser);
        binding = ActivityBusChooserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getData();

        binding.etSearch.setFocusable(true);
        binding.tvCancel.setOnClickListener(v -> onBackPressed());
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getData(binding.etSearch.getText()
                        .toString()
                        .toUpperCase(Locale.ROOT));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getData() {
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getBuses().observe(this, cities -> {
            BusesAdapter citiesAdapter = new BusesAdapter(cities);
            binding.rvSearch.setLayoutManager(new LinearLayoutManager(this));
            binding.rvSearch.setAdapter(citiesAdapter);

            onListener(citiesAdapter, cities);
        });
    }

    private void getData(String city) {
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getBuses(city).observe(this, cities -> {
            BusesAdapter citiesAdapter = new BusesAdapter(cities);
            binding.rvSearch.setLayoutManager(new LinearLayoutManager(this));
            binding.rvSearch.setAdapter(citiesAdapter);

            onListener(citiesAdapter, cities);
        });
    }

    private void onListener(BusesAdapter productsAdapter, ArrayList<Buses> buses) {
        productsAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("bus", buses.get(position));
                setResult(RESULT_OK, intent);
                onBackPressed();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}