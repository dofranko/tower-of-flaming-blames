package com.example.towerofflamingblames;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StatisticsActivity extends AppCompatActivity {

    private ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.activity_statistics_item);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        StatisticsAdapter adapter = new StatisticsAdapter(null);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}