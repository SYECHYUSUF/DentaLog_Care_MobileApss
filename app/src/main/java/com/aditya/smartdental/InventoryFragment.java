package com.aditya.smartdental;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.List;

/**
 * Fragment untuk menampilkan daftar alat.
 * Implementasi Materi 4: Fragments
 */
public class InventoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private ToolAdapter adapter;
    private DatabaseHelper dbHelper;
    private View emptyState;
    private boolean isMaintenanceOnly = false;

    public static InventoryFragment newInstance(boolean maintenanceOnly) {
        InventoryFragment fragment = new InventoryFragment();
        Bundle args = new Bundle();
        args.putBoolean("maintenance_only", maintenanceOnly);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isMaintenanceOnly = getArguments().getBoolean("maintenance_only");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inventory, container, false);
        
        recyclerView = v.findViewById(R.id.recyclerView);
        ExtendedFloatingActionButton fab = v.findViewById(R.id.fabAdd);
        emptyState = v.findViewById(R.id.emptyState);
        dbHelper = new DatabaseHelper(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        if (isMaintenanceOnly) {
            fab.setVisibility(View.GONE);
        }

        refreshData();

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddEditActivity.class);
            startActivity(intent);
        });

        return v;
    }

    public void refreshData() {
        List<ToolModel> tools;
        if (isMaintenanceOnly) {
            tools = dbHelper.getMaintenanceTools();
        } else {
            tools = dbHelper.getAllTools();
        }
        
        if (tools.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter = new ToolAdapter(tools, new ToolAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ToolModel tool) {
                Intent intent = new Intent(getActivity(), AddEditActivity.class);
                intent.putExtra("tool_data", tool);
                startActivity(intent);
            }

            @Override
            public void onRepairClick(ToolModel tool) {
                dbHelper.resetMaintenance(tool.getId());
                Toast.makeText(getContext(), "Alat telah diperbaiki", Toast.LENGTH_SHORT).show();
                refreshData();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }
}