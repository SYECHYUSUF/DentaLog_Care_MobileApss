package com.aditya.smartdental;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.List;

public class UsageFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_usage, container, false);
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        List<ToolModel> tools = dbHelper.getAllTools();
        
        TextView tvStats = v.findViewById(R.id.tvStats);
        int totalUsage = 0;
        for (ToolModel tool : tools) {
            totalUsage += tool.getUsageCount();
        }
        tvStats.setText("Total Penggunaan Alat: " + totalUsage + "\nSync dengan IoT: Aktif");
        
        return v;
    }
}