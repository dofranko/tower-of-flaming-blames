package com.example.towerofflamingblames.GameStatistics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.towerofflamingblames.R;

public class StatisticsTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_statistics);
    }

    public void onClickTopPlayers(View view) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        intent.putExtra("type", "topPlayers");
        startActivity(intent);
    }

    public void onClickYourGames(View view) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        intent.putExtra("type", "yourGames");
        startActivity(intent);
    }
}
