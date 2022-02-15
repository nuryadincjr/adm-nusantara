package com.nuyradincjr.ebusantara.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuyradincjr.ebusantara.R;
import com.nuyradincjr.ebusantara.activity.AddBusesActivity;
import com.nuyradincjr.ebusantara.activity.AddCityActivity;
import com.nuyradincjr.ebusantara.activity.AddScheduleActivity;
import com.nuyradincjr.ebusantara.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardBinding
                .inflate(inflater, container, false);

        binding.tvCities.setOnClickListener(v ->
                startActivity(new Intent(getContext(), AddCityActivity.class)));

        binding.tvBuses.setOnClickListener(v ->
                startActivity(new Intent(getContext(), AddBusesActivity.class)));

        binding.tvTransactions.setOnClickListener(v ->
                startActivity(new Intent(getContext(), AddCityActivity.class)));

        binding.tvSchedule.setOnClickListener(v ->
                startActivity(new Intent(getContext(), AddScheduleActivity.class)));
        return binding.getRoot();
    }
}