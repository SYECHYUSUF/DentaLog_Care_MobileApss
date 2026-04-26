package com.aditya.smartdental;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Entry point aplikasi.
 * Implementasi Materi 1: Activity Lifecycle & Materi 4: Fragment Navigation
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView nav = findViewById(R.id.bottomNavigation);
        
        // Load default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new InventoryFragment())
                    .commit();
        }

        nav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_inventory) {
                selected = new InventoryFragment();
            } else if (itemId == R.id.nav_usage) {
                selected = new UsageFragment();
            } else if (itemId == R.id.nav_maintenance) {
                // Sekarang menggunakan instance khusus untuk maintenance
                selected = InventoryFragment.newInstance(true);
            }

            if (selected != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selected)
                        .commit();
            }
            return true;
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}