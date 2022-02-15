package com.nuyradincjr.ebusantara.activity;

import static android.content.ContentValues.TAG;

import static com.google.common.io.Files.getFileExtension;
import static java.lang.String.valueOf;
import static java.util.Objects.requireNonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.nuyradincjr.ebusantara.R;
import com.nuyradincjr.ebusantara.adapters.ImageViewerAdapter;
import com.nuyradincjr.ebusantara.adapters.SpinnersAdapter;
import com.nuyradincjr.ebusantara.api.BusesRepository;
import com.nuyradincjr.ebusantara.api.CitiesRepository;
import com.nuyradincjr.ebusantara.api.ReviewsRepository;
import com.nuyradincjr.ebusantara.databinding.ActivityAddBusesBinding;
import com.nuyradincjr.ebusantara.databinding.ActivityAddCityBinding;
import com.nuyradincjr.ebusantara.interfaces.ItemClickListener;
import com.nuyradincjr.ebusantara.pojo.Buses;
import com.nuyradincjr.ebusantara.pojo.Cities;
import com.nuyradincjr.ebusantara.pojo.Seats;
import com.nuyradincjr.ebusantara.util.Constant;
import com.nuyradincjr.ebusantara.util.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddBusesActivity extends AppCompatActivity {

    private ActivityAddBusesBinding binding;
    private Buses buses;
    private List<Uri> uriImageList;
    private StorageReference storageReference;
    private ProgressDialog dialog;

    public AddBusesActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_buses);

        binding  = ActivityAddBusesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buses = new Buses();
        uriImageList = new ArrayList<>();
        dialog = new ProgressDialog(this);

        storageReference = FirebaseStorage.getInstance().getReference()
                .child("buses").child("images");

        binding.btnAddItem.setOnClickListener(v->{
            onDataSet();
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.clothing_type));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.actClassType.setAdapter(adapter);

        binding.btnAddPhoto.setOnClickListener(view -> Constant.getSinggleImage(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 25 && data != null) {
            uriImageList.add(data.getData());
        }

        getImageViewerAdapter();
    }

    private void getImageViewerAdapter() {
        uriImageList.remove(Uri.parse(""));
        ImageViewerAdapter imageViewerAdapter = new ImageViewerAdapter(uriImageList);
        binding.rvImageViewer.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        binding.rvImageViewer.setAdapter(imageViewerAdapter);

        onClickListener(imageViewerAdapter);
    }

    private void onClickListener(ImageViewerAdapter imageViewerAdapter) {
        imageViewerAdapter.setItemClickListener(new ItemClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view, int position) {
                uriImageList.remove(position);
                buses.setImageUrl("");
                imageViewerAdapter.notifyDataSetChanged();
                binding.btnAddPhoto.setEnabled(uriImageList.size() < 1);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
        binding.btnAddPhoto.setEnabled(uriImageList.size() < 1);
    }

    private void onDataSet() {
        String imageUrl = "";
        String busNo = String.valueOf(binding.etBusNo.getText()).toUpperCase(Locale.ROOT);
        String poName = String.valueOf(binding.etPoName.getText());
        String classType = valueOf(binding.actClassType.getSelectedItem()).toLowerCase(Locale.ROOT);
        String facility = String.valueOf(binding.etFacility.getText());
//        String imageUrl = String.valueOf(binding.etTerminal.getText());
        String price = String.valueOf(binding.etPiece.getText());
//        String seats = String.valueOf(binding.etTerminal.getText());


        Seats seats = new Seats();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = db.collection("buses").document().getId();

        if(!busNo.isEmpty() && !poName.isEmpty() && !classType.isEmpty() &&
                !facility.isEmpty() && !price.isEmpty()) {
            buses = new Buses(id, busNo, poName, classType, Constant.getList(facility), "", price);
            onDateCreated(buses);

        } else Toast.makeText(this,"Empty credentials!", Toast.LENGTH_SHORT).show();
    }

    private void onDateCreated(Buses buses) {

        dialog.setMessage("Creating Data..");
        dialog.setCancelable(false);
        dialog.show();

        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getBuses(buses).observe(this, user -> {
            if(user.size() == 0){

                if(!uriImageList.isEmpty() && !valueOf(uriImageList.get(0)).equals(this.buses.getImageUrl())) {
                    dialog.setMessage("Uploading file..");

                    StorageReference filePath = storageReference.child(buses.getId())
                            .child(buses.getId() + "." + Constant.getFileExtension(uriImageList.get(0), this));
                    StorageTask<UploadTask.TaskSnapshot> uploadTask = filePath.putFile(uriImageList.get(0));

                    uploadTask.continueWithTask(task -> {
                        if(!task.isSuccessful()) {
                            dialog.dismiss();
                            throw requireNonNull(task.getException());
                        }
                        return filePath.getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            assert downloadUri != null;
                            buses.setImageUrl(downloadUri.toString());

                            onDataCreated(buses);
                        }
                    });
                } else {
                     onDataCreated(buses);
                }
            }else {
                dialog.dismiss();
                Toast.makeText(this,
                        "This bus number of " +buses.getBusNo()+" is already used",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onDataCreated(Buses buses) {
        new BusesRepository().insertBuses(buses).addOnSuccessListener(documentReference -> {
            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference);
            new ReviewsRepository().insertReviews(buses).addOnSuccessListener(runnable -> {
                Log.d(TAG, "DocumentSnapshot added with ID: " + runnable);
                Toast.makeText(this,"Success.", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                finish();
            });
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Log.w(TAG, "Error adding document", e);
            Toast.makeText(this, "Error adding document.", Toast.LENGTH_SHORT).show();
        });
    }
}