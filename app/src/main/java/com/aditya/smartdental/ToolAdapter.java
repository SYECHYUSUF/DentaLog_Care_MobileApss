package com.aditya.smartdental;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

    public interface OnItemClickListener {
        void onItemClick(ToolModel tool);
        void onRepairClick(ToolModel tool);
        void onUseClick(ToolModel tool);
    }

    public ToolAdapter(List<ToolModel> tools, OnItemClickListener listener) {
        this.tools = tools;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ToolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tool_card, parent, false);
        return new ToolViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ToolViewHolder holder, int position) {
        ToolModel tool = tools.get(position);
        holder.tvName.setText(tool.getName());
        holder.tvUsage.setText("Penggunaan: " + tool.getUsageCount() + " kali");
        holder.tvStatus.setText(tool.getStatus());

        // Load Real Image if available
        if (tool.getImagePath() != null && !tool.getImagePath().isEmpty()) {
            holder.ivTool.setImageURI(Uri.parse(tool.getImagePath()));
            holder.ivTool.setPadding(0, 0, 0, 0); // Remove padding for real photo
        } else {
            holder.ivTool.setImageResource(R.drawable.logos); // Default icon
            holder.ivTool.setPadding(12, 12, 12, 12);
        }

        // Fitur Smart: Visual Status Alert
        if ("Need Maintenance".equals(tool.getStatus())) {
            holder.tvStatus.setTextColor(Color.RED);
            holder.cardView.setStrokeColor(Color.RED);
            holder.cardView.setStrokeWidth(3);
            holder.btnRepair.setVisibility(View.VISIBLE);
            holder.cardToolImage.setAlpha(0.5f);
            holder.cardToolImage.setClickable(false);
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
            holder.cardView.setStrokeWidth(0);
            holder.btnRepair.setVisibility(View.GONE);
            holder.cardToolImage.setAlpha(1.0f);
            holder.cardToolImage.setClickable(true);
        }

        holder.cardToolImage.setOnClickListener(v -> {
            if (!"Need Maintenance".equals(tool.getStatus())) {
                listener.onUseClick(tool);
            }
        });

        holder.btnRepair.setOnClickListener(v -> listener.onRepairClick(tool));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(tool));
    }

    @Override
    public int getItemCount() { return tools.size(); }

    static class ToolViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvUsage, tvStatus;
        MaterialCardView cardView, cardToolImage;
        ImageView ivTool;
        Button btnRepair;

        public ToolViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvToolName);
            tvUsage = itemView.findViewById(R.id.tvUsageCount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            cardView = (MaterialCardView) itemView;
            cardToolImage = itemView.findViewById(R.id.cardToolImage);
            ivTool = itemView.findViewById(R.id.ivToolItem);
            btnRepair = itemView.findViewById(R.id.btnRepair);
        }
    }
}