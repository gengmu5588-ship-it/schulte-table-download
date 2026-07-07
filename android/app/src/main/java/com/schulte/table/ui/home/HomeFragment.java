package com.schulte.table.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.schulte.table.R;
import com.schulte.table.ui.home.training.TrainingViewModel;

public class HomeFragment extends Fragment {

    private TabLayout subTabs;
    private ViewPager2 subPager;
    private TextView gridSizeSelector;
    private TrainingViewModel trainingViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subTabs = view.findViewById(R.id.sub_tabs);
        subPager = view.findViewById(R.id.sub_pager);
        gridSizeSelector = view.findViewById(R.id.grid_size_selector);

        trainingViewModel = new ViewModelProvider(requireActivity()).get(TrainingViewModel.class);

        HomeSubPagerAdapter adapter = new HomeSubPagerAdapter(this);
        subPager.setAdapter(adapter);
        subPager.setUserInputEnabled(false);

        new TabLayoutMediator(subTabs, subPager, (tab, position) -> {
            tab.setText(position == 0 ? getString(R.string.tab_training) : getString(R.string.tab_plan));
        }).attach();

        gridSizeSelector.setOnClickListener(v -> showGridSizePopup());
    }

    private void showGridSizePopup() {
        PopupMenu popup = new PopupMenu(requireContext(), gridSizeSelector);
        // Difficulty levels: 3x3=初级, 4x4=简单, 5x5=标准, 6x6=中级, 7x7=进阶, 8x8=高级, 9x9=专家, 10x10=大师
        String[] levels = {"初级", "简单", "标准", "中级", "进阶", "高级", "专家", "大师"};
        for (int size = 3; size <= 10; size++) {
            int index = size - 3;
            String label = size + "×" + size + " " + levels[index];
            popup.getMenu().add(Menu.NONE, size, index, label);
        }
        popup.setOnMenuItemClickListener(item -> {
            int gridSize = item.getItemId();
            trainingViewModel.setGridSize(gridSize);
            String[] levelNames = {"初级", "简单", "标准", "中级", "进阶", "高级", "专家", "大师"};
            gridSizeSelector.setText(gridSize + "×" + gridSize + " " + levelNames[gridSize - 3]);
            return true;
        });
        popup.show();
    }
}