package com.schulte.table.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.schulte.table.data.entity.PlanEntity;

import java.util.List;

@Dao
public interface PlanDao {

    @Insert
    long insert(PlanEntity plan);

    @Update
    void update(PlanEntity plan);

    @Query("SELECT * FROM plans WHERE is_active = 1 ORDER BY grid_size ASC")
    LiveData<List<PlanEntity>> getActivePlans();

    @Query("SELECT * FROM plans WHERE is_active = 1 ORDER BY grid_size ASC")
    List<PlanEntity> getActivePlansSync();

    @Query("SELECT * FROM plans WHERE grid_size = :gridSize AND is_active = 1 LIMIT 1")
    PlanEntity getActivePlanSync(int gridSize);

    @Query("DELETE FROM plans WHERE id = :id")
    void delete(long id);
}
