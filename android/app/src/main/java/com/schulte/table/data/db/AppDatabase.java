package com.schulte.table.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.schulte.table.data.db.dao.PlanDao;
import com.schulte.table.data.db.dao.RecordDao;
import com.schulte.table.data.entity.PlanEntity;
import com.schulte.table.data.entity.RecordEntity;

@Database(entities = {RecordEntity.class, PlanEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract RecordDao recordDao();
    public abstract PlanDao planDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "schulte_table.db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
