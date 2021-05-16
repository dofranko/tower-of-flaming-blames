package com.example.towerofflamingblames.GameStatistics;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.towerofflamingblames.R;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        ArrayList<StatisticsGame> listData;
        if (type == "topPlayers") {
            listData = getTopPlayers();
        } else {
            listData = getYourGames();
        }
        setContentView(R.layout.activity_statistics);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        StatisticsAdapter adapter = new StatisticsAdapter(listData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<StatisticsGame> getTopPlayers() {
        ArrayList<StatisticsGame> listData = new ArrayList<>();
        
        return listData;
    }

    private ArrayList<StatisticsGame> getYourGames() {
        ArrayList<StatisticsGame> listData = new ArrayList<>();
        return listData;
    }
}