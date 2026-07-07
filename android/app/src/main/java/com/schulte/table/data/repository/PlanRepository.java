package com.schulte.table.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.schulte.table.data.db.AppDatabase;
import com.schulte.table.data.db.dao.PlanDao;
import com.schulte.table.data.entity.PlanEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlanRepository {

    private final PlanDao planDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public PlanRepository(Context context) {
        planDao = AppDatabase.getInstance(context).planDao();
    }

    public LiveData<List<PlanEntity>> getActivePlans() {
        return planDao.getActivePlans();
    }

    public void insertPlan(PlanEntity plan) {
        executor.execute(() -> planDao.insert(plan));
    }

    public void updatePlan(PlanEntity plan) {
        executor.execute(() -> planDao.update(plan));
    }

    public void deletePlan(long id) {
        executor.execute(() -> planDao.delete(id));
    }
}
