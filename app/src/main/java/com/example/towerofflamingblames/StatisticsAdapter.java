package com.example.towerofflamingblames;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder>{
    private ArrayList<StatisticsGame> listData;

    // RecyclerView recyclerView;
    public StatisticsAdapter(ArrayList<StatisticsGame> listData) {
        this.listData = listData;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.activity_statistics_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final StatisticsGame myListData = listData.get(position);
        //holder.textView.setText(listdata[position].getDescription());
        //holder.imageView.setImageResource(listdata[position].getImgId());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //public ImageView imageView;
        //public TextView textView;
        //public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            //this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            //this.textView = (TextView) itemView.findViewById(R.id.textView);
            //relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}