package com.schulte.table.ui.home.plan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.schulte.table.R;

import java.util.Calendar;
import java.util.Locale;

public class PlanFragment extends Fragment {

    private TextView tvTodayProgress;
    private ProgressBar progressToday;
    private TextView tvStreakIcon;
    private TextView tvStreakDays;
    private LinearLayout weekCalendar;
    private NumberPicker npDailyTarget;
    private PlanViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTodayProgress = view.findViewById(R.id.tv_today_progress);
        progressToday = view.findViewById(R.id.progress_today);
        tvStreakIcon = view.findViewById(R.id.tv_streak_icon);
        tvStreakDays = view.findViewById(R.id.tv_streak_days);
        weekCalendar = view.findViewById(R.id.week_calendar);
        npDailyTarget = view.findViewById(R.id.np_daily_target);

        viewModel = new ViewModelProvider(this).get(PlanViewModel.class);

        // Setup NumberPicker
        npDailyTarget.setMinValue(1);
        npDailyTarget.setMaxValue(20);
        npDailyTarget.setValue(viewModel.getDailyTarget().getValue() != null ? viewModel.getDailyTarget().getValue() : 5);
        npDailyTarget.setOnValueChangedListener((picker, oldVal, newVal) -> viewModel.setDailyTarget(newVal));

        // Streak icon (fire emoji as text)
        tvStreakIcon.setText("\uD83D\uDD25");

        // Observe data
        viewModel.getTodayCount().observe(getViewLifecycleOwner(), count -> {
            Integer target = viewModel.getDailyTarget().getValue();
            int t = target != null ? target : 5;
            int c = count != null ? count : 0;
            tvTodayProgress.setText(getString(R.string.today_progress, c, t));
            progressToday.setMax(t * 100);
            progressToday.setProgress(c * 100);
        });

        viewModel.getStreakDays().observe(getViewLifecycleOwner(), days -> {
            int d = days != null ? days : 0;
            tvStreakDays.setText(getString(R.string.streak_days, d));
        });

        viewModel.getDailyTarget().observe(getViewLifecycleOwner(), target -> {
            if (target != null) npDailyTarget.setValue(target);
        });

        setupWeekCalendar();
    }

    private void setupWeekCalendar() {
        weekCalendar.removeAllViews();
        String[] dayNames = {"一", "二", "三", "四", "五", "六", "日"};
        Calendar cal = Calendar.getInstance();
        int todayDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        // Convert to Monday=0 index
        int todayIndex = (todayDayOfWeek + 5) % 7; // Calendar.SUNDAY=1

        for (int i = 0; i < 7; i++) {
            LinearLayout dayLayout = new LinearLayout(getContext());
            dayLayout.setOrientation(LinearLayout.VERTICAL);
            dayLayout.setGravity(android.view.Gravity.CENTER);

            TextView dayLabel = new TextView(getContext());
            dayLabel.setText(dayNames[i]);
            dayLabel.setTextSize(12);
            dayLabel.setTextColor(getResources().getColor(R.color.text_secondary));

            View dot = new View(getContext());
            int dotSize = (int) (24 * getResources().getDisplayMetrics().density);
            LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(dotSize, dotSize);
            dotParams.setMargins(0, 4, 0, 0);
            dot.setLayoutParams(dotParams);

            if (i < todayIndex) {
                // Past days - assume completed (green)
                dot.setBackgroundResource(R.drawable.bg_grid_cell_correct);
            } else if (i == todayIndex) {
                // Today - accent color
                dot.setBackgroundResource(R.drawable.bg_grid_cell_next);
            } else {
                // Future days - gray
                dot.setBackgroundResource(R.drawable.bg_grid_cell);
            }

            dayLayout.addView(dayLabel);
            dayLayout.addView(dot);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            dayLayout.setLayoutParams(params);
            weekCalendar.addView(dayLayout);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refresh();
    }
}
