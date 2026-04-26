package com.aditya.smartdental;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

/**
 * Adapter untuk RecyclerView.
 * Implementasi Materi 5: RecyclerView & Materi 7: Visual Alert
 */
public class ToolAdapter extends RecyclerView.Adapter<ToolAdapter.ToolViewHolder> {
    private List<ToolModel> tools;
    private OnItemClickListener listener;
    private DatabaseHelper dbHelper;

    public interface OnItemClickListener {
        void onItemClick(ToolModel tool);
        void onRepairClick(ToolModel tool);
    }

    public ToolAdapter(List<ToolModel> tools, OnItemClickListener listener) {
        this.tools = tools;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ToolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        dbHelper = new DatabaseHelper(parent.getContext());
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tool_card, parent, false);
        return new ToolViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ToolViewHolder holder, int position) {
        ToolModel tool = tools.get(position);
        holder.tvName.setText(tool.getName());
        holder.tvUsage.setText("Usage: " + tool.getUsageCount());
        holder.tvStatus.setText(tool.getStatus());

        // Fitur Smart: Visual Status Alert
        if ("Need Maintenance".equals(tool.getStatus())) {
            holder.tvStatus.setTextColor(Color.RED);
            holder.cardView.setStrokeColor(Color.RED);
            holder.cardView.setStrokeWidth(3);
            holder.btnRepair.setVisibility(View.VISIBLE);
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
            holder.cardView.setStrokeWidth(0);
            holder.btnRepair.setVisibility(View.GONE);
        }

        holder.btnRepair.setOnClickListener(v -> listener.onRepairClick(tool));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(tool));
    }

    @Override
    public int getItemCount() { return tools.size(); }

    static class ToolViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvUsage, tvStatus;
        MaterialCardView cardView;
        Button btnRepair;

        public ToolViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvToolName);
            tvUsage = itemView.findViewById(R.id.tvUsageCount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            cardView = (MaterialCardView) itemView;
            btnRepair = itemView.findViewById(R.id.btnRepair);
        }
    }
}