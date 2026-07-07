package com.schulte.table.data.model;

public class BenchmarkLevel {
    public final long expertMax;
    public final long advancedMax;
    public final long intermediateMax;
    public final long beginnerMax;

    public BenchmarkLevel(long expertMax, long advancedMax, long intermediateMax, long beginnerMax) {
        this.expertMax = expertMax;
        this.advancedMax = advancedMax;
        this.intermediateMax = intermediateMax;
        this.beginnerMax = beginnerMax;
    }
}
