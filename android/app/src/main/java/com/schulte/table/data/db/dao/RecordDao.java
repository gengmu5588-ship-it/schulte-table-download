package com.schulte.table.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.schulte.table.data.entity.RecordEntity;

import java.util.List;

@Dao
public interface RecordDao {

    @Insert
    long insert(RecordEntity record);

    @Update
    void update(RecordEntity record);

    @Query("SELECT * FROM records ORDER BY completed_at DESC")
    LiveData<List<RecordEntity>> getAllRecords();

    @Query("SELECT * FROM records ORDER BY completed_at DESC")
    List<RecordEntity> getAllRecordsSync();

    @Query("SELECT * FROM records WHERE grid_size = :gridSize ORDER BY completed_at DESC")
    LiveData<List<RecordEntity>> getRecordsByGridSize(int gridSize);

    @Query("SELECT * FROM records WHERE grid_size = :gridSize ORDER BY completed_at DESC")
    List<RecordEntity> getRecordsByGridSizeSync(int gridSize);

    @Query("SELECT MIN(elapsed_millis) FROM records WHERE grid_size = :gridSize")
    Long getBestTimeSync(int gridSize);

    @Query("SELECT * FROM records WHERE is_best = 1 AND grid_size = :gridSize LIMIT 1")
    RecordEntity getBestRecordSync(int gridSize);

    @Query("SELECT COUNT(*) FROM records WHERE grid_size = :gridSize AND completed_at >= :sinceMillis")
    int getTodayCountSync(int gridSize, long sinceMillis);

    @Query("SELECT COUNT(*) FROM records WHERE completed_at >= :sinceMillis")
    int getTodayTotalCountSync(long sinceMillis);

    @Query("SELECT DISTINCT date(completed_at / 1000, 'unixepoch', 'localtime') FROM records ORDER BY completed_at DESC")
    List<String> getDistinctDatesSync();

    @Transaction
    default void insertAndMarkBest(RecordEntity record) {
        RecordEntity oldBest = getBestRecordSync(record.gridSize);
        if (oldBest != null && record.elapsedMillis < oldBest.elapsedMillis) {
            oldBest.isBest = false;
            update(oldBest);
            record.isBest = true;
        } else if (oldBest == null) {
            record.isBest = true;
        }
        insert(record);
    }
}
