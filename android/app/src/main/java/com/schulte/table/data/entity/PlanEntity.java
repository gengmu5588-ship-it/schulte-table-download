package com.schulte.table.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "plans")
public class PlanEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "grid_size")
    public int gridSize;

    @ColumnInfo(name = "daily_target")
    public int dailyTarget;

    @ColumnInfo(name = "target_time_millis")
    public long targetTimeMillis;

    @ColumnInfo(name = "is_active")
    public boolean isActive;

    @ColumnInfo(name = "created_at")
    public long createdAt;
}
