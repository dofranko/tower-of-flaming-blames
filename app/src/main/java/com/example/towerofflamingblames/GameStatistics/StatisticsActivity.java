package com.example.towerofflamingblames.GameStatistics;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.towerofflamingblames.Data.GameData;
import com.example.towerofflamingblames.Data.PlayerData;
import com.example.towerofflamingblames.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    private ArrayList<StatisticsGame> listData = new ArrayList<>();
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        if (type.equals("topPlayers")) {
            getTopPlayers(ref);
        } else if (type.equals("topGames")) {
            getTopGames(ref);
        } else {
            String userID = intent.getStringExtra("userID");
            getYourGames(userID, ref);
        }
        // tworzenie recycleView adaptera
        setContentView(R.layout.activity_statistics);
        this.recyclerView = findViewById(R.id.recyclerView);
        StatisticsAdapter adapter = new StatisticsAdapter(listData);
        this.recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        manager.setReverseLayout(true);
        this.recyclerView.setLayoutManager(manager);
        this.recyclerView.setAdapter(adapter);
    }

    // wyświetla najlepszych graczy
    private void getTopPlayers(DatabaseReference ref) {
        Query query = ref.child("Users").orderByChild("Highest/Scores").limitToLast(100);
        query.get().addOnSuccessListener(task -> {
            for (DataSnapshot ds : task.getChildren()) {
                PlayerData playerData = ds.getValue(PlayerData.class);
                if (playerData.Highest != null) {
                    playerData.Highest.Date = playerData.Highest.Date.replace("T", " ");
                    this.listData.add(
                            new StatisticsGame(null, playerData.Highest.Scores.toString(),
                                playerData.Name, playerData.Highest.Date)
                    );
                }
            }
            this.recyclerView.getAdapter().notifyDataSetChanged();
        });
    }

    // wyciągamy najlepsze gry z bazy
    private void getTopGames(DatabaseReference ref) {
        Query query = ref.child("TopGames").orderByChild("scores").limitToLast(100);
        query.get().addOnSuccessListener(task -> {
            for (DataSnapshot ds : task.getChildren()) {
                GameData gameData = ds.getValue(GameData.class);
                gameData.date = gameData.date.replace("T", " ");
                this.listData.add(new StatisticsGame("", gameData.scores.toString(), gameData.name, gameData.date));
            }
            this.recyclerView.getAdapter().notifyDataSetChanged();
        });
    }

    // wyciągamy własne gry z bazy
    private void getYourGames(String userID, DatabaseReference ref) {
        DatabaseReference refGames = ref.child("Users/" + userID + "/Games");
        refGames.get().addOnSuccessListener(task -> {
            for (DataSnapshot ds : task.getChildren()) {
                String date = ds.getKey().replace("T", " ");
                this.listData.add(new StatisticsGame("Scores:", ds.getValue().toString(), "Date:", date));
            }
            this.recyclerView.getAdapter().notifyDataSetChanged();
        });
    }
}