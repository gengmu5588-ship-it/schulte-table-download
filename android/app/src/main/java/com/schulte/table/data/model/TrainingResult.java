package com.schulte.table.data.model;

public class TrainingResult {
    public final int gridSize;
    public final long elapsedMillis;
    public final int mistakes;

    public TrainingResult(int gridSize, long elapsedMillis, int mistakes) {
        this.gridSize = gridSize;
        this.elapsedMillis = elapsedMillis;
        this.mistakes = mistakes;
    }
}
