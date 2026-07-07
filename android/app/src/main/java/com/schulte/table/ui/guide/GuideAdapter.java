package com.schulte.table.ui.guide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.schulte.table.R;
import com.schulte.table.data.model.GuideItem;

import java.util.List;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.GuideViewHolder> {

    private List<GuideItem> guideItems;
    private final OnGuideClickListener listener;

    public interface OnGuideClickListener {
        void onGuideClick(GuideItem item);
    }

    public GuideAdapter(List<GuideItem> guideItems, OnGuideClickListener listener) {
        this.guideItems = guideItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GuideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_guide, parent, false);
        return new GuideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuideViewHolder holder, int position) {
        GuideItem item = guideItems.get(position);
        holder.tvIcon.setText(String.valueOf(position + 1));
        holder.tvTitle.setText(item.title);
        holder.tvSummary.setText(item.summary);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onGuideClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return guideItems != null ? guideItems.size() : 0;
    }

    static class GuideViewHolder extends RecyclerView.ViewHolder {
        TextView tvIcon;
        TextView tvTitle;
        TextView tvSummary;

        GuideViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIcon = itemView.findViewById(R.id.tv_guide_icon);
            tvTitle = itemView.findViewById(R.id.tv_guide_title);
            tvSummary = itemView.findViewById(R.id.tv_guide_summary);
        }
    }
}
