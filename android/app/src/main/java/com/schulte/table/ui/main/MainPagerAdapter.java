package com.schulte.table.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.schulte.table.ui.home.HomeFragment;
import com.schulte.table.ui.guide.GuideFragment;
import com.schulte.table.ui.records.RecordsFragment;

public class MainPagerAdapter extends FragmentStateAdapter {

    public MainPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new HomeFragment();
            case 1: return new GuideFragment();
            case 2: return new RecordsFragment();
            default: throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
