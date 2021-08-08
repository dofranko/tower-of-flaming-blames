package com.example.towerofflamingblames;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NicknameActivity extends AppCompatActivity {

    private final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);
    }

    // sprawdza, czy nick nie jest już zajęty
    public void checkNickname(View view) {
        EditText editTextNickname = findViewById(R.id.editTextTextPersonName);
        String result = editTextNickname.getText().toString();
        String nickname = result.trim().replaceAll(" +", " ");
        if (!nickname.equals("") ) {
            dbRef.orderByChild("Name").equalTo(nickname).get().addOnSuccessListener(task -> {
                if (task.getChildrenCount() > 0) {
                    TextView textView = findViewById(R.id.textViewWarning);
                    textView.setText("This name is unavailable");
                    textView.setVisibility(View.VISIBLE);
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("nickname", nickname);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        } else {
            TextView textView = findViewById(R.id.textViewWarning);
            textView.setText("Your nickname cannot be empty");
            textView.setVisibility(View.VISIBLE);
        }
    }
}
