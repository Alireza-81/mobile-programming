package com.example.xo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class FirstActivity extends AppCompatActivity{

    private Button loginButton;
    private Button registerButton;
    private Button historyButton;
    private UUID deviceUuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);

        // get device Uuid
        deviceUuid = getDeviceUuid();


        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
        historyButton = findViewById(R.id.history);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHistory();
            }
        });
    }

    private void goToLogin(){
        Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToRegister(){
        Intent intent = new Intent(FirstActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void goToHistory(){
        Intent intent = new Intent(FirstActivity.this, HistoryActivity.class);
        startActivity(intent);
    }

    private UUID getDeviceUuid() {
        final SharedPreferences prefs = getSharedPreferences("device_uuid.xml", MODE_PRIVATE);
        final String id = prefs.getString("device_uuid", null);
        if (id != null) {
            return UUID.fromString(id);
        } else {
            UUID newUuid = UUID.randomUUID();
            prefs.edit().putString("device_uuid", newUuid.toString()).apply();
            return newUuid;
        }
    }

}
