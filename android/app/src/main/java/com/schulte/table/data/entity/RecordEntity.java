package com.schulte.table.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "records")
public class RecordEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "grid_size")
    public int gridSize;

    @ColumnInfo(name = "total_numbers")
    public int totalNumbers;

    @ColumnInfo(name = "elapsed_millis")
    public long elapsedMillis;

    @ColumnInfo(name = "completed_at")
    public long completedAt;

    @ColumnInfo(name = "is_best")
    public boolean isBest;

    @ColumnInfo(name = "mistakes")
    public int mistakes;
}
