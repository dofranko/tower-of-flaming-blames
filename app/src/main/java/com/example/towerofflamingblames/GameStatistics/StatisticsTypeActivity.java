package com.example.towerofflamingblames.GameStatistics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.towerofflamingblames.R;

public class StatisticsTypeActivity extends AppCompatActivity {

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.userID = intent.getStringExtra("userID");
        setContentView(R.layout.activity_type_statistics);
    }

    public void onClickTopPlayers(View view) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        intent.putExtra("type", "topPlayers");
        intent.putExtra("userID", this.userID);
        startActivity(intent);
    }

    public void onClickYourGames(View view) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        intent.putExtra("type", "yourGames");
        intent.putExtra("userID", this.userID);
        startActivity(intent);
    }
}
