package com.nuyradincjr.ebusantara;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.nuyradincjr.ebusantara.activity.AboutActivity;
import com.nuyradincjr.ebusantara.activity.SettingsActivity;
import com.nuyradincjr.ebusantara.databinding.ActivityMainBinding;
import com.nuyradincjr.ebusantara.fragments.BusesFragment;
import com.nuyradincjr.ebusantara.fragments.CitiesFragment;
import com.nuyradincjr.ebusantara.fragments.DashboardFragment;
import com.nuyradincjr.ebusantara.fragments.ScheduleFragment;
import com.nuyradincjr.ebusantara.fragments.TransactionsFragment;
import com.nuyradincjr.ebusantara.fragments.UsersFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        new ActionBarDrawerToggle(this,
                binding.drawerLayout, binding.toolbar,
                R.string.navigation_open, R.string.navigation_close).syncState();

        binding.navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            binding.navigationView.setCheckedItem(R.id.itemDashboard);
            getFragmentPage(new DashboardFragment());
        }
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemDashboard:
                getFragmentPage(new DashboardFragment());
                requireNonNull(getSupportActionBar()).setTitle("Dashboard");
                break;
            case R.id.itemCities:
                getFragmentPage(new CitiesFragment());
                break;
            case R.id.itemBuses:
                getFragmentPage(new BusesFragment());
                break;
            case R.id.itemSchedule:
                getFragmentPage(new ScheduleFragment());
                break;
            case R.id.itemTransactions:
                getFragmentPage(new TransactionsFragment());
                break;
            case R.id.itemUsers:
                getFragmentPage(new UsersFragment());
                break;
            case R.id.itemSettings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.itemAbouts:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getFragmentPage(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer, fragment)
                    .commit();
        }
    }
}