package com.nuyradincjr.ebusantara.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class SpinnersAdapter {

    private static SpinnersAdapter instance;
    private final Context context;

    public SpinnersAdapter(Context context) {
        this.context = context;
    }

    public static SpinnersAdapter getInstance(Context context) {
        if(instance == null) {
            instance = new SpinnersAdapter(context);
        }
        return instance;
    }

    public void getSpinnerAdapter(AutoCompleteTextView spinner, int array, String item) {
        final String[] stringArray = context.getResources().getStringArray(array);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, stringArray);

        spinner.setAdapter(adapter);

        if(item != null) {
            int itemSelected = adapter.getPosition(item);
            spinner.setText(String.valueOf(adapter.getItem(itemSelected)), false);
        }
    }
}
