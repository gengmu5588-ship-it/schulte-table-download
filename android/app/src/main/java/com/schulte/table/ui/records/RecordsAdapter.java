package com.schulte.table.ui.records;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.schulte.table.R;
import com.schulte.table.data.entity.RecordEntity;
import com.schulte.table.util.BenchmarkUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.RecordViewHolder> {

    private List<RecordEntity> records;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());

    public RecordsAdapter(List<RecordEntity> records) {
        this.records = records;
    }

    public void updateRecords(List<RecordEntity> records) {
        this.records = records;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        RecordEntity record = records.get(position);

        holder.tvSize.setText(record.gridSize + "x" + record.gridSize);
        holder.tvTime.setText(formatTime(record.elapsedMillis));

        String level = BenchmarkUtil.getLevel(record.gridSize, record.elapsedMillis);
        holder.tvLevel.setText(level);
        holder.tvLevel.setTextColor((int) BenchmarkUtil.getLevelColor(record.gridSize, record.elapsedMillis));

        if (record.mistakes > 0) {
            holder.tvMistakes.setText("错误: " + record.mistakes);
            holder.tvMistakes.setVisibility(View.VISIBLE);
        } else {
            holder.tvMistakes.setVisibility(View.GONE);
        }

        holder.tvDate.setText(dateFormat.format(new Date(record.completedAt)));

        if (record.isBest) {
            holder.tvTime.setTextColor(holder.itemView.getContext().getColor(R.color.accent));
        } else {
            holder.tvTime.setTextColor(holder.itemView.getContext().getColor(R.color.primary));
        }
    }

    @Override
    public int getItemCount() {
        return records != null ? records.size() : 0;
    }

    private String formatTime(long millis) {
        return String.format("%.2fs", millis / 1000.0);
    }

    static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView tvSize;
        TextView tvTime;
        TextView tvLevel;
        TextView tvMistakes;
        TextView tvDate;

        RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSize = itemView.findViewById(R.id.tv_record_size);
            tvTime = itemView.findViewById(R.id.tv_record_time);
            tvLevel = itemView.findViewById(R.id.tv_record_level);
            tvMistakes = itemView.findViewById(R.id.tv_record_mistakes);
            tvDate = itemView.findViewById(R.id.tv_record_date);
        }
    }
}
