package com.schulte.table.ui.home.training;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.schulte.table.R;
import com.schulte.table.util.BenchmarkUtil;

public class ResultDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_GRID_SIZE = "grid_size";
    private static final String ARG_ELAPSED = "elapsed";
    private static final String ARG_MISTAKES = "mistakes";

    public static ResultDialogFragment newInstance(int gridSize, long elapsedMillis, int mistakes) {
        ResultDialogFragment fragment = new ResultDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GRID_SIZE, gridSize);
        args.putLong(ARG_ELAPSED, elapsedMillis);
        args.putInt(ARG_MISTAKES, mistakes);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args == null) return;

        int gridSize = args.getInt(ARG_GRID_SIZE, 5);
        long elapsedMillis = args.getLong(ARG_ELAPSED, 0);
        int mistakes = args.getInt(ARG_MISTAKES, 0);

        TextView tvTime = view.findViewById(R.id.tv_result_time);
        TextView tvLevel = view.findViewById(R.id.tv_result_level);
        TextView tvMistakes = view.findViewById(R.id.tv_result_mistakes);
        TextView tvBest = view.findViewById(R.id.tv_result_best);
        MaterialButton btnRetry = view.findViewById(R.id.btn_retry);
        MaterialButton btnViewRecords = view.findViewById(R.id.btn_view_records);

        tvTime.setText(formatTime(elapsedMillis));
        String level = BenchmarkUtil.getLevel(gridSize, elapsedMillis);
        tvLevel.setText(getString(R.string.level_label, level));
        tvLevel.setTextColor(BenchmarkUtil.getLevelColor(gridSize, elapsedMillis));
        tvMistakes.setText(getString(R.string.mistakes_label, mistakes));

        TrainingViewModel viewModel = new ViewModelProvider(requireActivity()).get(TrainingViewModel.class);

        viewModel.getBestTime().observe(getViewLifecycleOwner(), best -> {
            if (best != null) {
                tvBest.setText(getString(R.string.best_time_label, formatTime(best)));
            } else {
                tvBest.setText(getString(R.string.best_time_label, formatTime(elapsedMillis)));
            }
        });

        btnRetry.setOnClickListener(v -> {
            dismiss();
            viewModel.startNewGame();
        });

        btnViewRecords.setOnClickListener(v -> dismiss());
    }

    private String formatTime(long millis) {
        return String.format("%.2fs", millis / 1000.0);
    }
}
