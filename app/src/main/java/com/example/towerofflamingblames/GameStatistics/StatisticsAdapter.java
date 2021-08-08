package com.example.towerofflamingblames.GameStatistics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.towerofflamingblames.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

// Szablon Adaptera dla recycleView
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
        holder.leftUp.setText(item.leftUp);
        holder.rightUp.setText(item.rightUp);
        holder.leftDown.setText(item.leftDown);
        holder.rightDown.setText(item.rightDown);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView leftUp;
        public TextView rightUp;
        public TextView leftDown;
        public TextView rightDown;

        public ViewHolder(View itemView) {
            super(itemView);
            this.leftUp = itemView.findViewById(R.id.leftUp);
            this.rightUp = itemView.findViewById(R.id.rightUp);
            this.leftDown = itemView.findViewById(R.id.leftDown);
            this.rightDown = itemView.findViewById(R.id.rightDown);
        }
    }
}