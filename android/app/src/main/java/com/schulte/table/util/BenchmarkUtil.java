package com.schulte.table.util;

import com.schulte.table.R;
import com.schulte.table.data.model.BenchmarkLevel;

import java.util.HashMap;
import java.util.Map;

public class BenchmarkUtil {

    private static final Map<Integer, BenchmarkLevel> BENCHMARKS = new HashMap<>();

    static {
        // gridSize -> (expertMax, advancedMax, intermediateMax, beginnerMax) in millis
        BENCHMARKS.put(3,  new BenchmarkLevel(4000,  8000,  15000, 25000));
        BENCHMARKS.put(4,  new BenchmarkLevel(10000, 18000, 30000, 50000));
        BENCHMARKS.put(5,  new BenchmarkLevel(20000, 35000, 60000, 90000));
        BENCHMARKS.put(6,  new BenchmarkLevel(30000, 55000, 90000, 150000));
        BENCHMARKS.put(7,  new BenchmarkLevel(45000, 75000, 120000, 210000));
        BENCHMARKS.put(8,  new BenchmarkLevel(60000, 120000, 180000, 300000));
        BENCHMARKS.put(9,  new BenchmarkLevel(90000, 150000, 240000, 420000));
        BENCHMARKS.put(10, new BenchmarkLevel(120000, 210000, 360000, 600000));
    }

    public static String getLevel(int gridSize, long elapsedMillis) {
        BenchmarkLevel bl = BENCHMARKS.get(gridSize);
        if (bl == null) return "初学者";
        if (elapsedMillis <= bl.expertMax) return "专家";
        if (elapsedMillis <= bl.advancedMax) return "高级";
        if (elapsedMillis <= bl.intermediateMax) return "中级";
        return "初学者";
    }

    public static int getLevelColor(int gridSize, long elapsedMillis) {
        String level = getLevel(gridSize, elapsedMillis);
        switch (level) {
            case "专家": return 0xFF27AE60; // green
            case "高级": return 0xFF2980B9; // blue
            case "中级": return 0xFFF39C12; // orange
            default:     return 0xFFE74C3C; // red
        }
    }

    public static String getLevelDescription(int gridSize) {
        BenchmarkLevel bl = BENCHMARKS.get(gridSize);
        if (bl == null) return "";
        return String.format("专家 <%ds | 高级 <%ds | 中级 <%ds | 初学者 ≥%ds",
                bl.expertMax / 1000,
                bl.advancedMax / 1000,
                bl.intermediateMax / 1000,
                bl.intermediateMax / 1000);
    }

    public static BenchmarkLevel getBenchmark(int gridSize) {
        return BENCHMARKS.get(gridSize);
    }
}
