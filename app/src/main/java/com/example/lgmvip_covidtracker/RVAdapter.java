package com.example.lgmvip_covidtracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVViewHolder> {

    List<Model> modelList;

    public RVAdapter(List<Model> mList) {
        modelList = mList;
    }

    @NonNull
    @Override
    public RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvview, parent, false);
        return new RVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVViewHolder holder, int position) {
        Model data = modelList.get(position);
        System.out.println(holder.DistrictNameView);
        holder.DistrictNameView.setText(data.getDistrict());
        holder.ActiveCasesView.setText(String.valueOf(data.getActive()));
        holder.RecoveredCasesView.setText(String.valueOf(data.getRecovered()));
        holder.DeathCasesView.setText(String.valueOf(data.getDeceased()));
        holder.TotalCasesView.setText(String.valueOf(data.getConfirmed()));
    }

    @Override
    public int getItemCount() {
        if (modelList == null)
            modelList = new ArrayList<>();
        return modelList.size();
    }

    class RVViewHolder extends RecyclerView.ViewHolder {
        TextView DistrictNameView;
        TextView ActiveCasesView;
        TextView RecoveredCasesView;
        TextView DeathCasesView;
        TextView TotalCasesView;


        public RVViewHolder(@NonNull View itemView) {
            super(itemView);
            DistrictNameView = itemView.findViewById(R.id.DistrictName);
            ActiveCasesView = itemView.findViewById(R.id.ActiveCases);
            TotalCasesView = itemView.findViewById(R.id.TotalCases);
            RecoveredCasesView = itemView.findViewById(R.id.Recovered);
            DeathCasesView = itemView.findViewById(R.id.Deaths);
        }
    }
}
