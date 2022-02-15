package com.nuyradincjr.ebusantara.fragments;

import static com.nuyradincjr.ebusantara.databinding.FragmentBusesBinding.inflate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nuyradincjr.ebusantara.activity.AddBusesActivity;
import com.nuyradincjr.ebusantara.activity.AddCityActivity;
import com.nuyradincjr.ebusantara.adapters.BusesAdapter;
import com.nuyradincjr.ebusantara.databinding.FragmentBusesBinding;
import com.nuyradincjr.ebusantara.interfaces.ItemClickListener;
import com.nuyradincjr.ebusantara.pojo.Buses;
import com.nuyradincjr.ebusantara.util.MainViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class BusesFragment extends Fragment {

    FragmentBusesBinding binding;

    public BusesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflate(inflater, container, false);

        getData();
        binding.fabAdd.setOnClickListener(v ->
                startActivity(new Intent(getContext(), AddBusesActivity.class)));
        binding.etSearch.setFocusable(true);
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
        return binding.getRoot();
    }

    private void getData() {
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getBuses().observe(getViewLifecycleOwner(), cities -> {
            BusesAdapter citiesAdapter = new BusesAdapter(cities);
            binding.rvSearch.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvSearch.setAdapter(citiesAdapter);

            onListener(citiesAdapter, cities);
        });
    }

    private void getData(String city) {
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getBuses(city).observe(this, cities -> {
            BusesAdapter citiesAdapter = new BusesAdapter(cities);
            binding.rvSearch.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvSearch.setAdapter(citiesAdapter);

            onListener(citiesAdapter, cities);
        });
    }

    private void onListener(BusesAdapter productsAdapter, ArrayList<Buses> buses) {
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