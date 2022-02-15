package com.nuyradincjr.ebusantara.fragments;


import static com.nuyradincjr.ebusantara.databinding.FragmentCitiesBinding.inflate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nuyradincjr.ebusantara.activity.AddCityActivity;
import com.nuyradincjr.ebusantara.adapters.CitiesAdapter;
import com.nuyradincjr.ebusantara.databinding.FragmentCitiesBinding;
import com.nuyradincjr.ebusantara.interfaces.ItemClickListener;
import com.nuyradincjr.ebusantara.pojo.Cities;
import com.nuyradincjr.ebusantara.util.MainViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class CitiesFragment extends Fragment {

    private FragmentCitiesBinding binding;

    public CitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = inflate(inflater, container, false);

        getData();
        binding.fabAdd.setOnClickListener(v ->
                startActivity(new Intent(getContext(), AddCityActivity.class)));

        binding.etSearch.setFocusable(true);
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

        return binding.getRoot();
    }

    private void getData() {
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getCities().observe(getViewLifecycleOwner(), cities -> {
            CitiesAdapter citiesAdapter = new CitiesAdapter(cities);
            binding.rvSearch.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvSearch.setAdapter(citiesAdapter);

            onListener(citiesAdapter, cities);
        });
    }

    private void getData(String city) {
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getCities(city).observe(getViewLifecycleOwner(), cities -> {
            CitiesAdapter citiesAdapter = new CitiesAdapter(cities);
            binding.rvSearch.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvSearch.setAdapter(citiesAdapter);

            onListener(citiesAdapter, cities);
        });
    }

    private void onListener(CitiesAdapter productsAdapter, ArrayList<Cities> citiesList) {
        productsAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }
}