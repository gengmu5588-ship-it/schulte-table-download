package com.schulte.table;

import android.app.Application;

import com.schulte.table.data.db.AppDatabase;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Pre-initialize database
        AppDatabase.getInstance(this);
    }
}
