package com.example.towerofflamingblames;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new GameSurface(this));
    }

    public void endGame(int coins) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("coins", String.valueOf(coins));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}