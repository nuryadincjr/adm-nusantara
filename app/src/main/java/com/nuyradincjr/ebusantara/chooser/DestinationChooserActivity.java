package com.nuyradincjr.ebusantara.chooser;

import static com.nuyradincjr.ebusantara.databinding.ActivityDestinationChooserBinding.inflate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nuyradincjr.ebusantara.R;
import com.nuyradincjr.ebusantara.adapters.CitiesAdapter;
import com.nuyradincjr.ebusantara.databinding.ActivityDestinationChooserBinding;
import com.nuyradincjr.ebusantara.interfaces.ItemClickListener;
import com.nuyradincjr.ebusantara.pojo.Cities;
import com.nuyradincjr.ebusantara.util.MainViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class DestinationChooserActivity extends AppCompatActivity {
    private ActivityDestinationChooserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_chooser);

        binding = inflate(getLayoutInflater());
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
                        .toLowerCase(Locale.ROOT));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getData() {
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getCities().observe(this, cities -> {
            CitiesAdapter citiesAdapter = new CitiesAdapter(cities);
            binding.rvSearch.setLayoutManager(new LinearLayoutManager(this));
            binding.rvSearch.setAdapter(citiesAdapter);

            onListener(citiesAdapter, cities);
        });
    }

    private void getData(String city) {
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getCities(city).observe(this, cities -> {
            CitiesAdapter citiesAdapter = new CitiesAdapter(cities);
            binding.rvSearch.setLayoutManager(new LinearLayoutManager(this));
            binding.rvSearch.setAdapter(citiesAdapter);

            onListener(citiesAdapter, cities);
        });
    }

    private void onListener(CitiesAdapter productsAdapter, ArrayList<Cities> citiesList) {
        productsAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("city", citiesList.get(position));
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