package com.aditya.smartdental;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddEditActivity extends AppCompatActivity {
    private TextInputEditText etName;
    private AutoCompleteTextView acType;
    private MaterialButton btnSave, btnDelete;
    private ImageView ivToolImage;
    private DatabaseHelper dbHelper;
    private ToolModel existingTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        etName = findViewById(R.id.etToolName);
        acType = findViewById(R.id.acToolType);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        ivToolImage = findViewById(R.id.ivToolImage);
        
        dbHelper = new DatabaseHelper(this);

        // Setup Dropdown Categories
        String[] categories = {"Eskavator", "Tang Ekstraksi", "Sonde Dental"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        acType.setAdapter(adapter);

        if (getIntent().hasExtra("tool_data")) {
            existingTool = getIntent().getParcelableExtra("tool_data");
            if (existingTool != null) {
                etName.setText(existingTool.getName());
                acType.setText(existingTool.getType(), false);
                btnSave.setText("UPDATE ALAT");
                btnDelete.setVisibility(View.VISIBLE);
            }
        }

        btnSave.setOnClickListener(v -> saveTool());
        
        btnDelete.setOnClickListener(v -> {
            if (existingTool != null) {
                dbHelper.deleteTool(existingTool.getId());
                Toast.makeText(this, "Alat berhasil dihapus", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        findViewById(R.id.cardImagePicker).setOnClickListener(v -> {
            // Simple toast for image picker placeholder
            Toast.makeText(this, "Membuka Galeri...", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveTool() {
        String name = etName.getText().toString();
        String type = acType.getText().toString();

        if (name.isEmpty() || type.isEmpty()) {
            Toast.makeText(this, "Harap isi semua bidang", Toast.LENGTH_SHORT).show();
            return;
        }

        if (existingTool == null) {
            dbHelper.insertTool(new ToolModel(name, type, 0, "Available"));
            Toast.makeText(this, "Alat Baru Ditambahkan", Toast.LENGTH_SHORT).show();
        } else {
            ToolModel updatedTool = new ToolModel(existingTool.getId(), name, type, existingTool.getUsageCount(), existingTool.getStatus());
            dbHelper.updateTool(updatedTool);
            Toast.makeText(this, "Data Berhasil Diperbarui", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}