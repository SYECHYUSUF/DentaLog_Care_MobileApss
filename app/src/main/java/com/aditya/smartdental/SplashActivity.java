package com.aditya.smartdental;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.ivSplashLogo);
        TextView text = findViewById(R.id.tvSplashText);

        logo.animate()
                .alpha(1f)
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(1000)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() -> {
                    logo.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(500)
                            .start();
                })
                .start();

        text.animate()
                .alpha(1f)
                .translationY(-20f)
                .setStartDelay(500)
                .setDuration(1000)
                .start();

        new Handler().postDelayed(() -> {
            // Updated to navigate to LoginActivity instead of MainActivity
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 3000);
    }
}