package com.schulte.table.ui.home.plan;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.schulte.table.data.entity.PlanEntity;
import com.schulte.table.data.repository.PlanRepository;
import com.schulte.table.data.repository.RecordRepository;

import java.util.Calendar;
import java.util.List;

public class PlanViewModel extends AndroidViewModel {

    private final PlanRepository planRepository;
    private final RecordRepository recordRepository;
    private final MutableLiveData<Integer> todayCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> streakDays = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> dailyTarget = new MutableLiveData<>(5);
    private final MutableLiveData<boolean[]> weekCompleted = new MutableLiveData<>(new boolean[7]);

    public PlanViewModel(@NonNull Application application) {
        super(application);
        planRepository = new PlanRepository(application);
        recordRepository = new RecordRepository(application);
        loadTodayData();
    }

    public LiveData<Integer> getTodayCount() { return todayCount; }
    public LiveData<Integer> getStreakDays() { return streakDays; }
    public LiveData<Integer> getDailyTarget() { return dailyTarget; }
    public LiveData<boolean[]> getWeekCompleted() { return weekCompleted; }

    public void setDailyTarget(int target) {
        dailyTarget.setValue(target);
        PlanEntity plan = new PlanEntity();
        plan.gridSize = 0; // global plan
        plan.dailyTarget = target;
        plan.targetTimeMillis = 0;
        plan.isActive = true;
        plan.createdAt = System.currentTimeMillis();
        planRepository.insertPlan(plan);
    }

    public void refresh() {
        loadTodayData();
    }

    private void loadTodayData() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long todayStart = cal.getTimeInMillis();

        recordRepository.getTodayTotalCount(todayStart, count -> todayCount.postValue(count));
        recordRepository.getStreak(streak -> streakDays.postValue(streak));
    }

    public int getTodayProgressPercent() {
        Integer count = todayCount.getValue();
        Integer target = dailyTarget.getValue();
        if (count == null || target == null || target == 0) return 0;
        return Math.min(100, (count * 100) / target);
    }
}
