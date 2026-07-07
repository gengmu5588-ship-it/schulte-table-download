package com.schulte.table.ui.home.training;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.schulte.table.R;
import com.schulte.table.util.BenchmarkUtil;

import java.util.List;
import java.util.Set;

public class SchulteGridAdapter extends RecyclerView.Adapter<SchulteGridAdapter.CellViewHolder> {

    private List<Integer> numbers;
    private final Set<Integer> correctSet;
    private int nextNumber;
    private int wrongCellIndex;
    private int gridSize;
    private final OnCellClickListener listener;

    public interface OnCellClickListener {
        void onCellClick(int index, int number);
    }

    public SchulteGridAdapter(List<Integer> numbers, Set<Integer> correctSet,
                               int nextNumber, int wrongCellIndex, int gridSize,
                               OnCellClickListener listener) {
        this.numbers = numbers;
        this.correctSet = correctSet;
        this.nextNumber = nextNumber;
        this.wrongCellIndex = wrongCellIndex;
        this.gridSize = gridSize;
        this.listener = listener;
    }

    public void update(List<Integer> numbers, Set<Integer> correctSet,
                       int nextNumber, int wrongCellIndex, int gridSize) {
        this.numbers = numbers;
        this.nextNumber = nextNumber;
        this.wrongCellIndex = wrongCellIndex;
        this.gridSize = gridSize;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_cell, parent, false);
        return new CellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CellViewHolder holder, int position) {
        if (numbers == null || position >= numbers.size()) return;

        int number = numbers.get(position);
        holder.tvNumber.setText(String.valueOf(number));

        // Dynamic text size based on grid size
        float textSize;
        if (gridSize <= 4) textSize = 28f;
        else if (gridSize <= 6) textSize = 20f;
        else if (gridSize <= 8) textSize = 16f;
        else textSize = 13f;
        holder.tvNumber.setTextSize(textSize);

        // Cell state - only show wrong state, no visual feedback for correct clicks
        if (position == wrongCellIndex) {
            holder.tvNumber.setBackgroundResource(R.drawable.bg_grid_cell_wrong);
            holder.tvNumber.setTextColor(holder.itemView.getContext().getColor(R.color.cell_wrong_text));
        } else {
            holder.tvNumber.setBackgroundResource(R.drawable.bg_grid_cell);
            holder.tvNumber.setTextColor(holder.itemView.getContext().getColor(R.color.cell_text));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCellClick(position, number);
            }
        });
    }

    @Override
    public int getItemCount() {
        return numbers != null ? numbers.size() : 0;
    }

    static class CellViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber;

        CellViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tv_cell_number);
        }
    }
}
