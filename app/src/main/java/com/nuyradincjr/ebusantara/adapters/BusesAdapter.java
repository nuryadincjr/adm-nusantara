package com.nuyradincjr.ebusantara.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nuyradincjr.ebusantara.databinding.ListItemCitiesBinding;
import com.nuyradincjr.ebusantara.interfaces.ItemClickListener;
import com.nuyradincjr.ebusantara.pojo.Buses;

import java.util.List;

public class BusesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ItemClickListener itemClickListener;
    private final List<Buses> citiesList;

    public BusesAdapter(List<Buses> citiesList) {
        this.citiesList = citiesList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemCitiesBinding binding = ListItemCitiesBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ScheduleViewHolder(binding, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ScheduleViewHolder scheduleViewHolder = (ScheduleViewHolder) holder;
        scheduleViewHolder.setDataToView(citiesList.get(position));
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private final ListItemCitiesBinding binding;
        private final BusesAdapter scheduleAdapter;

        public ScheduleViewHolder(ListItemCitiesBinding binding, BusesAdapter scheduleAdapter) {
            super(binding.getRoot());
            this.binding = binding;
            this.scheduleAdapter = scheduleAdapter;
        }

        public void setDataToView(Buses citiesList) {
            binding.tvLabel1.setText("Bus No.");
            binding.tvLabel2.setText("PO Name");
            binding.tvLabel3.setText("Class Type");

            binding.tvId.setText(citiesList.getBusNo());
            binding.tvCity.setText(citiesList.getPoName());
            binding.tvTerminal.setText(citiesList.getClassType());

            binding.getRoot().setOnLongClickListener(this);
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(scheduleAdapter.itemClickListener != null){
                scheduleAdapter.itemClickListener.onClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(scheduleAdapter.itemClickListener != null){
                scheduleAdapter.itemClickListener.onLongClick(v, getAdapterPosition());
            }
            return true;
        }
    }
}

