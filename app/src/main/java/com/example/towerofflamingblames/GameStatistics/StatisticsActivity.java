package com.example.towerofflamingblames.GameStatistics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.towerofflamingblames.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class StatisticsActivity extends AppCompatActivity {

    private ArrayList<StatisticsGame> listData = new ArrayList<>();
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String userID = intent.getStringExtra("userID");
        // wyświetla albo top playersów, albo Twoje wyniki
        if (type.equals("topPlayers")) {
            addTopPlayers();
        } else {
            addYourGames(userID);
        }
        // tworzenie recycleView adaptera
        setContentView(R.layout.activity_statistics);
        this.recyclerView = findViewById(R.id.recyclerView);
        StatisticsAdapter adapter = new StatisticsAdapter(listData);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(adapter);
    }

    // wyświetla najlepszych graczy w grze
    private void addTopPlayers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("Users").orderByChild("Highest/Scores").limitToLast(3);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                // dane najlepszych użytkoników
                ArrayList<String> name = new ArrayList<>();
                ArrayList<String> dates = new ArrayList<>();
                ArrayList<String> scores = new ArrayList<>();
                if (dataSnapshot != null) {
                    // odpowienie wyciąganie wartości z bazy danych
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        for (DataSnapshot dsChild : ds.getChildren()) {
                            if (Objects.equals(dsChild.getKey(), "Highest")) {
                                for (DataSnapshot dsNestedChild : dsChild.getChildren()) {
                                    if (Objects.equals(dsNestedChild.getKey(), "Date")) {
                                        dates.add((String) dsNestedChild.getValue());
                                    } else {
                                        scores.add(String.valueOf(dsNestedChild.getValue()));
                                    }
                                }
                            } else if (Objects.equals(dsChild.getKey(), "Name")) {
                                name.add((String) dsChild.getValue());
                            }
                        }
                    }
                    // tworzenie pozycji w recyclerView
                    for (int i = 0; i < dates.size(); i++) {
                        String date = dates.get(i).replace("T", " ");
                        StatisticsGame statisticsGame = new StatisticsGame("", scores.get(i), name.get(i), date);
                        this.listData.add(statisticsGame);
                    }
                }
                // aktualizacja recycleView
                Objects.requireNonNull(this.recyclerView.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    // analogicznie do powyższej funkcji tylko, że wyciągamy własne gry
    private void addYourGames(String userID) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("Users").child(userID);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<String> dates = new ArrayList<>();
                ArrayList<String> scores = new ArrayList<>();
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (Objects.equals(ds.getKey(), "Games")) {
                            for (DataSnapshot dsChild : ds.getChildren()) {
                                dates.add(dsChild.getKey());
                                scores.add((String) dsChild.getValue());
                            }
                        }
                    }
                    for (int i = 0; i < dates.size(); i++) {
                        String date = dates.get(i).replace("T", " ");
                        StatisticsGame statisticsGame = new StatisticsGame("Scores:", scores.get(i), "Date:", date);
                        this.listData.add(statisticsGame);
                    }
                }
                Objects.requireNonNull(this.recyclerView.getAdapter()).notifyDataSetChanged();
            }
        });
    }
}