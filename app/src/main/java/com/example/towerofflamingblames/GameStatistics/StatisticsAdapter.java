package com.example.towerofflamingblames.GameStatistics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.towerofflamingblames.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder>{
    private final ArrayList<StatisticsGame> listData;

    public StatisticsAdapter(ArrayList<StatisticsGame> listData) {
        this.listData = listData;
    }

    @Override
    public @NotNull ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.activity_statistics_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final StatisticsGame item = listData.get(position);
        holder.email.setText(item.email);
        holder.score.setText(item.score);
        holder.name.setText(item.name);
        holder.date.setText(item.date);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView email;
        public TextView score;
        public TextView name;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            this.email = itemView.findViewById(R.id.email);
            this.score = itemView.findViewById(R.id.score);
            this.name = itemView.findViewById(R.id.name);
            this.date = itemView.findViewById(R.id.date);
        }
    }
}