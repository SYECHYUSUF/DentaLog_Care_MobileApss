package com.aditya.smartdental;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            // Simple login logic to proceed to MainActivity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });

        findViewById(R.id.btnFingerprint).setOnClickListener(v -> {
            Toast.makeText(this, "Memindai Sidik Jari...", Toast.LENGTH_SHORT).show();
            // Simulate successful fingerprint auth
            new android.os.Handler().postDelayed(() -> {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }, 1500);
        });
    }
}