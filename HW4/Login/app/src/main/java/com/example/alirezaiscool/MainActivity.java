package com.example.alirezaiscool;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.alirezaiscool.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        String username = binding.username.getText().toString();
        String password = binding.password.getText().toString();

        if (username.equals("admin") && password.equals("admin")) {
            // Login success
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();


        } else {
            // Login failed
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }
}