package com.example.towerofflamingblames;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.towerofflamingblames.GameStatistics.StatisticsActivity;
import com.example.towerofflamingblames.GameStatistics.StatisticsTypeActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final int RC_GAME = 321;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    // dostępne metody logowania (mail)
    private List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (this.user != null) {
                    DatabaseReference myRef = database.getReference().child("Users").child(this.user.getUid());
                    myRef.child("Name").setValue(this.user.getDisplayName());
                    myRef.child("Email").setValue(this.user.getEmail());
                    myRef.child("Games").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                            long count = snapshot.getChildrenCount();
                            if (count > 10) {
                                Query oldestGame = myRef.child("Games").orderByKey().limitToFirst(1);
                                DataSnapshot cos = oldestGame.get().getResult();
                                assert cos != null;
                                cos.getRef().removeValue();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {}
                    });
                }
            }
        } else if (requestCode == RC_GAME) {
            if (resultCode == RESULT_OK) {
                int coins = data.getIntExtra("coins", 0);
                DatabaseReference myRef = database.getReference().child("Users").child(this.user.getUid());
                myRef.child("Games").child(LocalDateTime.now().toString().substring(0,19)).setValue(coins);
                /*
                Query gamesQuery = myRef.child("Games").orderByKey();
                Task<DataSnapshot> gamesQueryExecute = myRef.child("Games").orderByKey().get();
                DataSnapshot games = gamesQueryExecute.getResult();
                if (games != null) {
                    long count = games.getChildrenCount();
                    System.out.println(count);
                    /*
                    if (count > 10) {
                        Query oldestGame = games.limitToFirst(1);
                        DataSnapshot cos = oldestGame.get().getResult();
                        assert cos != null;
                        cos.getRef().removeValue();
                    }
                }*/
            }
        }
    }

    public void showStatistics(View view) {
        Intent intent = new Intent(this, StatisticsTypeActivity.class);
        startActivity(intent);
    }

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