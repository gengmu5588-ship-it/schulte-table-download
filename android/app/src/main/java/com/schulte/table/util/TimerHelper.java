package com.schulte.table.util;

import android.os.SystemClock;

public class TimerHelper {

    private long startMillis;
    private long accumulatedMillis;
    private boolean running;

    public void start() {
        startMillis = SystemClock.elapsedRealtime();
        running = true;
    }

    public long getElapsedMillis() {
        if (running) {
            return accumulatedMillis + (SystemClock.elapsedRealtime() - startMillis);
        }
        return accumulatedMillis;
    }

    public void stop() {
        if (running) {
            accumulatedMillis += SystemClock.elapsedRealtime() - startMillis;
            running = false;
        }
    }

    public void reset() {
        startMillis = 0;
        accumulatedMillis = 0;
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
