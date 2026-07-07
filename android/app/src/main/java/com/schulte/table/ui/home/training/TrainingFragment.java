package com.schulte.table.ui.home.training;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.schulte.table.R;

import java.util.ArrayList;
import java.util.List;

public class TrainingFragment extends Fragment {

    private RecyclerView gridRecyclerView;
    private SchulteGridAdapter gridAdapter;
    private TextView tvTimer;
    private TextView tvNextNumber;
    private TextView tvMistakes;
    private Button btnStart;
    private Button btnStop;
    private TrainingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_training, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTimer = view.findViewById(R.id.tv_timer);
        tvNextNumber = view.findViewById(R.id.tv_next_number);
        tvMistakes = view.findViewById(R.id.tv_mistakes);
        gridRecyclerView = view.findViewById(R.id.grid_recycler);
        btnStart = view.findViewById(R.id.btn_start);
        btnStop = view.findViewById(R.id.btn_stop);

        viewModel = new ViewModelProvider(requireActivity()).get(TrainingViewModel.class);

        int gridSize = viewModel.getGridSize().getValue() != null ? viewModel.getGridSize().getValue() : 5;
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), gridSize);
        gridRecyclerView.setLayoutManager(layoutManager);

        gridAdapter = new SchulteGridAdapter(
                new ArrayList<>(), viewModel.getCorrectSet(),
                1, -1, gridSize,
                (index, number) -> viewModel.onCellClick(index, number)
        );
        gridRecyclerView.setAdapter(gridAdapter);

        // Observe LiveData
        viewModel.getGridNumbers().observe(getViewLifecycleOwner(), numbers -> {
            Integer size = viewModel.getGridSize().getValue();
            if (size != null && !numbers.isEmpty()) {
                GridLayoutManager lm = new GridLayoutManager(getContext(), size);
                gridRecyclerView.setLayoutManager(lm);
            }
            updateAdapter();
        });

        viewModel.getElapsedMillis().observe(getViewLifecycleOwner(), millis -> {
            tvTimer.setText(formatTime(millis != null ? millis : 0L));
        });

        viewModel.getNextNumber().observe(getViewLifecycleOwner(), next -> {
            tvNextNumber.setText(String.valueOf(next != null ? next : 1));
            updateAdapter();
        });

        viewModel.getMistakes().observe(getViewLifecycleOwner(), m -> {
            // Display just the number, label is in the layout
            tvMistakes.setText(String.valueOf(m != null ? m : 0));
        });

        viewModel.getWrongCellIndex().observe(getViewLifecycleOwner(), idx -> updateAdapter());

        viewModel.getIsComplete().observe(getViewLifecycleOwner(), complete -> {
            if (complete != null && complete) {
                btnStart.setText(R.string.start_training);
                btnStop.setVisibility(View.GONE);
                showResultDialog();
            }
        });

        viewModel.getIsRunning().observe(getViewLifecycleOwner(), running -> {
            if (running != null && running) {
                btnStart.setText(R.string.retry);
                btnStop.setVisibility(View.VISIBLE);
            } else {
                btnStop.setVisibility(View.GONE);
            }
        });

        viewModel.getGridSize().observe(getViewLifecycleOwner(), size -> {
            Boolean running = viewModel.getIsRunning().getValue();
            if (running == null || !running) {
                viewModel.resetGame();
            }
        });

        btnStart.setOnClickListener(v -> viewModel.startNewGame());
        btnStop.setOnClickListener(v -> viewModel.stopGameWithoutSave());
    }

    private void updateAdapter() {
        List<Integer> numbers = viewModel.getGridNumbers().getValue();
        Integer next = viewModel.getNextNumber().getValue();
        Integer wrongIdx = viewModel.getWrongCellIndex().getValue();
        Integer size = viewModel.getGridSize().getValue();

        if (numbers != null && !numbers.isEmpty()) {
            gridAdapter.update(
                    numbers,
                    viewModel.getCorrectSet(),
                    next != null ? next : 1,
                    wrongIdx != null ? wrongIdx : -1,
                    size != null ? size : 5
            );
        }
    }

    private void showResultDialog() {
        Integer size = viewModel.getGridSize().getValue();
        Long time = viewModel.getFinalTime().getValue();
        Integer m = viewModel.getMistakes().getValue();

        if (size == null || time == null) return;

        ResultDialogFragment dialog = ResultDialogFragment.newInstance(
                size, time, m != null ? m : 0
        );
        dialog.show(getChildFragmentManager(), "result");
    }

    private String formatTime(long millis) {
        return String.format("%.2fs", millis / 1000.0);
    }
}
