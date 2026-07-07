package com.schulte.table.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.schulte.table.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setUserInputEnabled(false);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                int[] navIds = {R.id.nav_home, R.id.nav_guide, R.id.nav_records};
                bottomNav.setSelectedItemId(navIds[position]);
            }
        });

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) viewPager.setCurrentItem(0, false);
            else if (itemId == R.id.nav_guide) viewPager.setCurrentItem(1, false);
            else if (itemId == R.id.nav_records) viewPager.setCurrentItem(2, false);
            return true;
        });
    }
}
