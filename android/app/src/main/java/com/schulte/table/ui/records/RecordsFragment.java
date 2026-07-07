package com.schulte.table.ui.records;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.schulte.table.R;
import com.schulte.table.data.entity.RecordEntity;
import com.schulte.table.util.BenchmarkUtil;

import java.util.ArrayList;
import java.util.List;

public class RecordsFragment extends Fragment {

    private TabLayout tabsFilter;
    private RecyclerView rvRecords;
    private RecordsAdapter adapter;
    private TextView tvBestTime;
    private TextView tvBestLevel;
    private TextView tvEmpty;
    private RecordsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_records, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabsFilter = view.findViewById(R.id.tabs_filter);
        rvRecords = view.findViewById(R.id.rv_records);
        tvBestTime = view.findViewById(R.id.tv_best_time);
        tvBestLevel = view.findViewById(R.id.tv_best_level);
        tvEmpty = view.findViewById(R.id.tv_empty);

        viewModel = new ViewModelProvider(this).get(RecordsViewModel.class);

        // Setup filter tabs
        tabsFilter.addTab(tabsFilter.newTab().setText(R.string.filter_all));
        for (int size = 3; size <= 10; size++) {
            tabsFilter.addTab(tabsFilter.newTab().setText(size + "x" + size));
        }

        tabsFilter.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int filterSize = tab.getPosition() == 0 ? 0 : tab.getPosition() + 2;
                viewModel.setFilterGridSize(filterSize);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Setup RecyclerView
        adapter = new RecordsAdapter(new ArrayList<>());
        rvRecords.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecords.setAdapter(adapter);

        // Observe records
        viewModel.getFilteredRecords().observe(getViewLifecycleOwner(), records -> {
            adapter.updateRecords(records != null ? records : new ArrayList<>());
            tvEmpty.setVisibility(records == null || records.isEmpty() ? View.VISIBLE : View.GONE);
            rvRecords.setVisibility(records == null || records.isEmpty() ? View.GONE : View.VISIBLE);
            updateBestRecord(records);
        });
    }

    private void updateBestRecord(List<RecordEntity> records) {
        if (records == null || records.isEmpty()) {
            tvBestTime.setText("--");
            tvBestLevel.setText("");
            return;
        }

        RecordEntity best = records.get(0);
        for (RecordEntity r : records) {
            if (r.elapsedMillis < best.elapsedMillis) {
                best = r;
            }
        }

        tvBestTime.setText(String.format("%.2fs", best.elapsedMillis / 1000.0));
        String level = BenchmarkUtil.getLevel(best.gridSize, best.elapsedMillis);
        tvBestLevel.setText(level);
    }
}
