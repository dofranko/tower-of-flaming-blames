package com.example.towerofflamingblames;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.towerofflamingblames.Data.GameData;
import com.example.towerofflamingblames.GameStatistics.StatisticsTypeActivity;
import com.firebase.ui.auth.AuthUI;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final int RC_NICKNAME = 213;
    private static final int RC_GAME = 321;
    private final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    // aktualny zalogowany user
    private FirebaseUser user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView appVersion = findViewById(R.id.appVersion);
        appVersion.setText("version: " + BuildConfig.VERSION_NAME);
        
        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setGitHubUserAndRepo("dofranko", "tower-of-flaming-blames")
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(Update update, Boolean isUpdateAvailable) {
                        if (isUpdateAvailable) {
                            informAboutNewVersionApp(update.getUrlToDownload().toString());
                        }
                    }

                    @Override
                    public void onFailed(AppUpdaterError error) {
                        Log.d("AppUpdater Error", "Something went wrong");
                    }
                });
        appUpdaterUtils.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_SIGN_IN) {
                enableButtons(false);
                changeUI(true);
                this.user = FirebaseAuth.getInstance().getCurrentUser();
                database.child("Users").orderByKey().equalTo(this.user.getUid()).get().
                        addOnSuccessListener(task -> {
                            if (task.getChildrenCount() == 0) {
                                Intent intent = new Intent(this, NicknameActivity.class);
                                startActivityForResult(intent, RC_NICKNAME);
                            } else {
                                enableButtons(true);
                            }
                        });
            } else if (requestCode == RC_NICKNAME) {
                enableButtons(true);
                String nickname = data.getStringExtra("nickname");
                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nickname)
                        .build();
                user.updateProfile(profileUpdate);
                DatabaseReference myRef = database.child("Users/" + this.user.getUid());
                myRef.child("Name").setValue(nickname);
            } else if (requestCode == RC_GAME && this.user != null) {
                Long coins = Long.valueOf(data.getStringExtra("coins"));
                String date = LocalDateTime.now().toString().substring(0,19);
                saveResultsInDatabase(coins, date);
            }
        } else if (resultCode == RESULT_CANCELED && requestCode == RC_NICKNAME) {
            enableButtons(true);
            logOut();
        }
    }

    // zapisanie skończonej gry w bazie danych
    private void saveResultsInDatabase(Long coins, String date) {
        DatabaseReference myRefUser = database.child("Users/" + this.user.getUid());
        // dodanie nowej pozycji gry w bazie z datą i punktami
        DatabaseReference myRefGames = myRefUser.child("Games");
        myRefGames.child(date).setValue(coins);
        // w bazie jest przechowywane 10 ostatnich gier
        myRefGames.get().addOnSuccessListener(task -> {
            if (task.getChildrenCount() > 10) {
                for (DataSnapshot ds : task.getChildren()) {
                    ds.getRef().removeValue();
                    break;
                }
            }
        });
        // aktualizacja najwyższego wyniku gracza
        DatabaseReference myRefHighest = myRefUser.child("Highest/Scores");
        myRefHighest.get().addOnSuccessListener(task -> {
            try {
                if (task.getValue(int.class) <= coins) {
                    task.getRef().removeValue();
                    myRefUser.child("Highest/Date").setValue(date);
                    myRefHighest.setValue(coins);
                }
            } catch (NullPointerException ignored) {
                myRefUser.child("Highest/Date").setValue(date);
                myRefHighest.setValue(coins);
            }
        });
        // w bazie jest przechowywane 30 najlepszych gier
        DatabaseReference myRefTopGames = database.child("TopGames");
        myRefTopGames.push().setValue(new GameData(this.user.getDisplayName(), date, coins));
        myRefTopGames.get().addOnSuccessListener(task -> {
            if (task.getChildrenCount() > 30) {
                myRefTopGames.orderByChild("scores").limitToFirst(1).get().addOnSuccessListener(task2 -> {
                    for(DataSnapshot ds : task2.getChildren()) {
                        myRefTopGames.child(ds.getKey()).removeValue();
                    }
                });
            }
        });
    }

    // zmienia interfejs w zależności czy gracz się zalogował
    private void changeUI(boolean flag) {
        Button btnLog = findViewById(R.id.btnLog);
        Button btnStatistics = findViewById(R.id.btnStatistics);
        Button btnDelete = findViewById(R.id.btnDelete);
        if (flag) {
            btnLog.setText("LOGOUT");
            btnStatistics.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnLog.setText("LOGIN");
            btnStatistics.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }
    }

    private void enableButtons(boolean flag) {
        findViewById(R.id.btnPlay).setEnabled(flag);
        findViewById(R.id.btnLog).setEnabled(flag);
        findViewById(R.id.btnStatistics).setEnabled(flag);
        findViewById(R.id.btnDelete).setEnabled(flag);
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

    // zarządzanie kontem (logowanie, wylogowanie)
    public void manageAccount(View view) {
        if (this.user == null) {
            logIn();
        } else {
            logOut();
        }
    }

    // tworzenie okna logowania
    private void logIn() {
        // dostępne metody logowania (mail)
        AuthUI.IdpConfig emailSignIn = new AuthUI.IdpConfig.EmailBuilder().setRequireName(false).build();
        List<AuthUI.IdpConfig> providers = Collections.singletonList(emailSignIn);
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.LoginTheme)
                        .build(),
                RC_SIGN_IN);
    }

    private void logOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> { });
        this.user = null;
        changeUI(false);
    }

    public void deleteAccount(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Are you sure you want to delete your account?");
        dialog.setTitle("Warning");
        dialog.setPositiveButton("Yes", (dialog1, id) -> {
            // usuwamy nasze dane w bazie
            database.child("Users/" + user.getUid()).removeValue();
            // usuwamy z najlepszych gier nasze własne gry
            database.child("TopGames").orderByChild("name").equalTo(this.user.getDisplayName()).get()
                    .addOnSuccessListener(task -> {
                        for(DataSnapshot ds : task.getChildren()) {
                            database.child("TopGames/" + ds.getKey()).removeValue();
                        }
                    });
            user.delete();
            user = null;
            changeUI(false);
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    // dodatkowo wylogowuje danego użytkownika
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.user != null) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> { });
        }
    }

    private void informAboutNewVersionApp(String url) {
        AlertDialog newUpdateDialog = new AlertDialog.Builder(this)
                .setTitle("New update available!")
                .setMessage("Enjoy a new version of Tower of Flaming Blames!")
                .setPositiveButton("Update", (dialog, which) -> {
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(launchBrowser);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {})
                .show();
        newUpdateDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
    }
}
