package com.schulte.table.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.schulte.table.ui.home.training.TrainingFragment;
import com.schulte.table.ui.home.plan.PlanFragment;

public class HomeSubPagerAdapter extends FragmentStateAdapter {

    public HomeSubPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new TrainingFragment();
            case 1: return new PlanFragment();
            default: throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
