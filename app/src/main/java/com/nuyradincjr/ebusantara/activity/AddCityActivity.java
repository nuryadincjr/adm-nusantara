package com.nuyradincjr.ebusantara.activity;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.FirebaseFirestore;
import com.nuyradincjr.ebusantara.R;
import com.nuyradincjr.ebusantara.api.CitiesRepository;
import com.nuyradincjr.ebusantara.databinding.ActivityAddCityBinding;
import com.nuyradincjr.ebusantara.pojo.Cities;
import com.nuyradincjr.ebusantara.util.MainViewModel;

import java.util.Locale;

public class AddCityActivity extends AppCompatActivity {
    private ActivityAddCityBinding binding;
    private Cities cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        binding  = ActivityAddCityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = db.collection("cities").document().getId();
        binding.etID.setText(id);

        cities = new Cities();

        binding.btnAddItem.setOnClickListener(v->{
            onDataSet();
        });
    }

    private void onDataSet() {
        String id = String.valueOf(binding.etID.getText());
        String city = String.valueOf(binding.etCity.getText()).toLowerCase(Locale.ROOT);
        String terminal = String.valueOf(binding.etTerminal.getText()).toLowerCase(Locale.ROOT);

        if(!id.isEmpty() && !city.isEmpty() && !terminal.isEmpty() ) {
            cities = new Cities(id, city, terminal);
            onDateCreated(cities);

        } else Toast.makeText(this,"Empty credentials!", Toast.LENGTH_SHORT).show();
    }

    private void onDateCreated(Cities cities) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Creating Data..");
        dialog.setCancelable(false);
        dialog.show();
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getCities(cities).observe(this, user -> {
            if(user.size() == 0){
                new CitiesRepository().insertCities(cities).addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference);
                    Toast.makeText(this,"Success.", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                    finish();
                }).addOnFailureListener(e -> {
                    dialog.dismiss();
                    Log.w(TAG, "Error adding document", e);
                    Toast.makeText(this, "Error adding document.", Toast.LENGTH_SHORT).show();
                });
            }else {
                dialog.dismiss();
                Toast.makeText(this,
                        "This " +cities.getCity()+" city with terminal "+ cities.getTerminal() +" is already used",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}