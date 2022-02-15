package com.nuyradincjr.ebusantara.chooser;

import static com.nuyradincjr.ebusantara.databinding.ActivityDatePickerBinding.inflate;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.nuyradincjr.ebusantara.MainActivity;
import com.nuyradincjr.ebusantara.R;
import com.nuyradincjr.ebusantara.databinding.ActivityDatePickerBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePickerActivity extends AppCompatActivity {
        private ActivityDatePickerBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        binding.tvSelectedDate.setText(dateFormat.format(calendar.getTime()));

        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            binding.tvSelectedDate.setText(dateFormat.format(calendar.getTime()));
        });

        @SuppressLint("SimpleDateFormat")
        DateFormat format = new SimpleDateFormat("HH:mm");
        binding.tvTime.setText(format.format(calendar.getTime()));

        binding.btnTime.setOnClickListener(v-> new TimePickerDialog(this,
                (timePicker, i, i1) -> {
                    Date date = new Date();
                    date.setHours(i);
                    date.setMinutes(i1);
                    calendar.setTime(date);

                   binding.tvTime.setText(format.format(calendar.getTime()));
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show());

        binding.btnSelectedDate.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("date", calendar);
            setResult(RESULT_OK, intent);
            onBackPressed();
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