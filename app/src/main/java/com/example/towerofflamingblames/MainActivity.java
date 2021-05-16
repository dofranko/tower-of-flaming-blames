package com.example.towerofflamingblames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.towerofflamingblames.GameStatistics.StatisticsTypeActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final int RC_GAME = 321;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    // dostępne metody logowania (mail)
    private final List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build());
    // aktualny zalogowany user
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // tworzenie okna logowania
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    // powrót z okna logowania
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (this.user != null) {
                    // tworzenie struktury bazy danych
                    DatabaseReference myRef = database.getReference().child("Users").child(this.user.getUid());
                    myRef.child("Name").setValue(this.user.getDisplayName());
                    myRef.child("Email").setValue(this.user.getEmail());
                }
            }
        } else if (requestCode == RC_GAME) {
            if (resultCode == RESULT_OK) {
                // odebranie liczby monet po skończonej grze
                String coins = data.getStringExtra("coins");
                DatabaseReference myRef = database.getReference().child("Users").child(this.user.getUid());
                DatabaseReference myRefGames = myRef.child("Games");
                // dodanie nowej pozycji gry w bazie z datą i punktami
                myRefGames.child(LocalDateTime.now().toString().substring(0,19)).setValue(coins);
                myRefGames.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        if (dataSnapshot != null) {
                            // maksymalnie zapisuje 10 ostatnich gier gracza
                            if (dataSnapshot.getChildrenCount() > 2) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ds.getRef().removeValue();
                                    break;
                                }
                            }
                            // wyciągnięcie gry z największą liczbą punktów
                            int highestScores = 0;
                            String date = "";
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (ds.getValue() != null) {
                                    int scores = Integer.parseInt((String) ds.getValue());
                                    if (highestScores <= scores) {
                                        highestScores = scores;
                                        date = ds.getKey();
                                    }
                                }
                            }
                            final int currentHighestScores = highestScores;
                            final String currentDate = date;
                            myRef.child("Highest").child("Scores").get().addOnCompleteListener(secondTask -> {
                                if (secondTask.isSuccessful()) {
                                    int previousCoins = 0;
                                    if (secondTask.getResult() != null) {
                                        previousCoins = Objects.requireNonNull(secondTask.getResult().getValue(int.class));
                                    }
                                    if (previousCoins <= currentHighestScores) {
                                        myRef.child("Highest").child("Scores").setValue(currentHighestScores);
                                        myRef.child("Highest").child("Date").setValue(currentDate);
                                    }
                                } else {
                                    myRef.child("Highest").child("Scores").setValue(currentHighestScores);
                                    myRef.child("Highest").child("Date").setValue(currentDate);
                                }
                            });

                        }
                    }
                });
            }
        }
    }

    // otwiera nową aktywność z wyborem zobaczenia swoich statystyk, albo globalnych
    public void showStatistics(View view) {
        Intent intent = new Intent(this, StatisticsTypeActivity.class);
        intent.putExtra("userID", this.user.getUid());
        startActivity(intent);
    }

    // rozpoczęcie gry
    public void play(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivityForResult(intent, RC_GAME);
    }

    // wylogowuje danego użytkownika
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                });
    }
}